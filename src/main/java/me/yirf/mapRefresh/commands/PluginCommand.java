package me.yirf.mapRefresh.commands;

import me.yirf.mapRefresh.map.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PluginCommand implements CommandExecutor {

    private GameMap map;

    public PluginCommand(GameMap map) {
        this.map = map;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("player command only!");
            return true;
        }

        Player p = (Player) sender;

        switch (args[0]) {
            case "load":
                map.load();
                sender.sendMessage("loading map, teleporting to ", map.getWorld().getName());
                p.teleport(map.getWorld().getSpawnLocation());
                return true;
            case "unload":
                map.unload();
                sender.sendMessage("unloading map, teleporting to world.");
                p.teleport(Bukkit.getWorld("world").getSpawnLocation());
                return true;
        }

        return true;
    }
}
