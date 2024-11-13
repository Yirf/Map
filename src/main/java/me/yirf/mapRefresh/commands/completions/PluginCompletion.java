package me.yirf.mapRefresh.commands.completions;

import me.yirf.mapRefresh.MapRefresh;
import me.yirf.mapRefresh.utils.FileUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class PluginCompletion implements TabCompleter {

    File gameMapsFolder;


    public PluginCompletion(MapRefresh plugin) {
        gameMapsFolder = plugin.gameMapsFolder;
    }

    // so disgusting
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Ensure the sender has the required permission
        if (!sender.hasPermission("server.admin")) {return List.of();}

        return List.of("load", "unload");
    }
}
