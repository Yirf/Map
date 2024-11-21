package me.yirf.mapRefresh.map;

import me.yirf.mapRefresh.utils.ConfigUtil;
import me.yirf.mapRefresh.utils.FileUtil;
import me.yirf.mapRefresh.utils.WorldUtil;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public class LocalGameMap implements GameMap {
    private final File sourceWorldFolder;
    private final FileConfiguration config;
    private File activeWorldFolder;

    private World bukkitWorld;
    private Location spawn;

    public LocalGameMap(File worldFolder, String worldName, FileConfiguration config, boolean loadOnInit) {
        this.sourceWorldFolder = new File(
                worldFolder,
                worldName
        );

        this.config = config;
        if (loadOnInit) load();
    }

    @Override
    public boolean load() {
        if (isLoaded()) return true;

        this.activeWorldFolder = new File(
                Bukkit.getWorldContainer().getParentFile(),
                sourceWorldFolder.getName()
                        + "_active_"
                        + System.currentTimeMillis()
        );

        try {
            FileUtil.copy(sourceWorldFolder, activeWorldFolder);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to load GameMap from source folder" + sourceWorldFolder);
            e.printStackTrace();
            return false;
        }

        try {
            this.bukkitWorld = Bukkit.createWorld(
                    new WorldCreator(activeWorldFolder.getName())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bukkitWorld == null) {
            Bukkit.getLogger().severe("Failed to create world from: " + activeWorldFolder.getName());
            return false;
        } else {
            this.bukkitWorld.setAutoSave(false);
            bukkitWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            bukkitWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        }
        Location spwn = ConfigUtil.getLocation(config, "enabled." + sourceWorldFolder.getName() + ".spawn");
        this.spawn = new Location(this.bukkitWorld, spwn.x(), spwn.y(), spwn.z(), spwn.getYaw(), spwn.getPitch());

        return isLoaded();
    }

    @Override
    public void unload() {
        WorldUtil.unloadWorld(bukkitWorld);
        if (activeWorldFolder != null) FileUtil.delete(activeWorldFolder);

        bukkitWorld = null;
        activeWorldFolder = null;
    }

    @Override
    public boolean restoreFromSource() {
        unload();
        return load();
    }

    @Override
    public boolean isLoaded() { return getWorld() != null;}

    @Override
    public @Nullable World getWorld() {
        return bukkitWorld;
    }

    @Override
    public Location spawn() {
        return spawn;
    }
}
