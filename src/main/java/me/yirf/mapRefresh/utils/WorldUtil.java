package me.yirf.mapRefresh.utils;

import me.yirf.mapRefresh.MapRefresh;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class WorldUtil {

    public static boolean unloadWorld(World world) {

        for (Player player : world.getPlayers()) {
            player.teleport(MapRefresh.spawnLocation);
            player.sendMessage(ChatColor.GREEN + "All maps are now resetting!");
        }

        boolean success = Bukkit.getServer().unloadWorld(world, false);
        Bukkit.unloadWorld(world, false);

        return success;

    }

    public static World loadWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            return world;
        }

        WorldCreator worldCreator = new WorldCreator(worldName);
        world = worldCreator.createWorld();

        return world;
    }
}
