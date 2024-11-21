package me.yirf.mapRefresh.Listener;

import me.yirf.mapRefresh.MapRefresh;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(MapRefresh.spawnLocation);
    }
}
