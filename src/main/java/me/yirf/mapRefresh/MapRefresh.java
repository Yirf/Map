package me.yirf.mapRefresh;

import me.yirf.mapRefresh.commands.PluginCommand;
import me.yirf.mapRefresh.commands.SpawnCommand;
import me.yirf.mapRefresh.commands.completions.PluginCompletion;
import me.yirf.mapRefresh.map.GameMap;
import me.yirf.mapRefresh.map.LocalGameMap;
import me.yirf.mapRefresh.utils.ConfigUtil;
import me.yirf.mapRefresh.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class MapRefresh extends JavaPlugin {

    //private GameMap map;
    private Map<String, GameMap> maps = new HashMap<>();
    public File gameMapsFolder;
    public static Location spawnLocation;
    public static long refreshTimer;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        spawnLocation = ConfigUtil.getLocation(getConfig(), "spawn");
        getDataFolder().mkdirs();

        gameMapsFolder = new File(getDataFolder(), "maps");
        if (!gameMapsFolder.exists()) {
            gameMapsFolder.mkdirs();
        }

        for (String name : getConfig().getStringList("enabled")) {
            GameMap map = new LocalGameMap(gameMapsFolder, name, true);
            maps.put(name, map);
        }
        //map = new LocalGameMap(gameMapsFolder, "Map1", true);

        getCommand("map").setExecutor(new PluginCommand(maps));
        getCommand("map").setTabCompleter(new PluginCompletion(this));
        getCommand("spawn").setExecutor(new SpawnCommand());

        new MapExpansion(getConfig().getString("placeholders.prefix")).register();

        createWorlds();
        createScheduler();
    }

    @Override
    public void onDisable() {
        for (GameMap map : maps.values()) {
            map.unload();
        }
    }

    private void createWorlds() {
        refreshTimer = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(getConfig().getInt("timer"));
        for (GameMap map : maps.values()) {
            map.load();
        }
    }

    private void refreshWorlds() {
        refreshTimer = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(getConfig().getInt("timer"));
        for (GameMap map : maps.values()) {
            map.restoreFromSource();
        }
    }

    private void createScheduler() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (TimeUtil.compare(refreshTimer) < 1) {
                refreshWorlds();
                Bukkit.broadcastMessage(ChatColor.GREEN +  "Maps have been reset!");

            }
        }, 0L, 20L);
    }
}
