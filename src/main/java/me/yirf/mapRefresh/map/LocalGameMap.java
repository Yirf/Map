package me.yirf.mapRefresh.map;

import me.yirf.mapRefresh.utils.FileUtil;
import me.yirf.mapRefresh.utils.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public class LocalGameMap implements GameMap {
    private final File sourceWorldFolder;
    private File activeWorldFolder;

    private World bukkitWorld;

    public LocalGameMap(File worldFolder, String worldName, boolean loadOnInit) {
        this.sourceWorldFolder = new File(
                worldFolder,
                worldName
        );

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
            Bukkit.getLogger().severe("Unable to create world");
            e.printStackTrace();
            return false;
        }


        this.bukkitWorld.setAutoSave(false);

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
}
