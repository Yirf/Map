package me.yirf.mapRefresh;

import me.yirf.mapRefresh.commands.PluginCommand;
import me.yirf.mapRefresh.map.GameMap;
import me.yirf.mapRefresh.map.LocalGameMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MapRefresh extends JavaPlugin {

    private GameMap map;

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();

        File gameMapsFolder = new File(getDataFolder(), "gameMaps");
        if (!gameMapsFolder.exists()) {
            gameMapsFolder.mkdirs();
        }

        map = new LocalGameMap(gameMapsFolder, "Map1", true);

        getCommand("maprefresh").setExecutor(new PluginCommand(map));

    }

    @Override
    public void onDisable() {
        map.unload();
    }
}
