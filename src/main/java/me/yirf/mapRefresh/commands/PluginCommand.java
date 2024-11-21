package me.yirf.mapRefresh.commands;

import me.yirf.mapRefresh.map.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginCommand implements CommandExecutor {

    private Map<String, GameMap> maps = new HashMap<>();

    public PluginCommand(Map<String, GameMap> maps) {
        this.maps = maps;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("server.admin")) {return true;}
        Player p = (Player) sender;
        GameMap map;

        if (args.length < 2 || args[1] == null || args[1].isEmpty() || !maps.containsKey(args[1])) {
            p.sendMessage(ChatColor.RED + "Invalid map, please specify a valid map!");
            return true;
        }

        map = maps.get(args[1]);

        switch (args[0]) {
            case "load":
                map.load();
                p.teleport(map.getWorld().getSpawnLocation());
                return true;
            case "unload":
                map.unload();
                return true;
        }

        return true;
    }
}
