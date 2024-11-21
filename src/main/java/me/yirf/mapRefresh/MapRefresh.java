package me.yirf.mapRefresh;

import me.yirf.mapRefresh.Listener.PlayerJoin;
import me.yirf.mapRefresh.commands.PluginCommand;
import me.yirf.mapRefresh.commands.SpawnCommand;
import me.yirf.mapRefresh.commands.completions.PluginCompletion;
import me.yirf.mapRefresh.map.GameMap;
import me.yirf.mapRefresh.map.LocalGameMap;
import me.yirf.mapRefresh.utils.ConfigUtil;
import me.yirf.mapRefresh.utils.TimeUtil;
import me.yirf.mapRefresh.utils.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
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

        WorldUtil.loadWorld(getConfig().getString("spawn.world"));
        spawnLocation = ConfigUtil.getLocation(getConfig(), "spawn");
        getDataFolder().mkdirs();

        gameMapsFolder = new File(getDataFolder(), "maps");
        if (!gameMapsFolder.exists()) {
            gameMapsFolder.mkdirs();
        }

        ConfigurationSection enabledSection = getConfig().getConfigurationSection("enabled");
        if (enabledSection != null) {
            for (String key : enabledSection.getKeys(false)) {

                GameMap map = new LocalGameMap(gameMapsFolder, key, getConfig(), true);
                maps.put(key, map);
            }
        } else {
            Bukkit.broadcastMessage("No 'enabled' section found in the configuration.");
        }

        getCommand("map").setExecutor(new PluginCommand(maps));
        getCommand("map").setTabCompleter(new PluginCompletion(this));
        getCommand("spawn").setExecutor(new SpawnCommand());
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

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

    public Map<String, GameMap> getMaps() {
        return maps;
    }

    public Location getSpawn() {
        return spawnLocation;
    }
}
