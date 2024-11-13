package me.yirf.mapRefresh.commands;

import me.yirf.mapRefresh.MapRefresh;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {return true;}

        if (MapRefresh.spawnLocation == null) {
            p.sendMessage(ChatColor.RED + "Thats weird? Looks like spawn is unset.");
            return true;
        }

        p.teleport(MapRefresh.spawnLocation);
        p.sendMessage(ChatColor.GREEN + "Teleported to spawn.");
        return true;
    }
}
