package me.yirf.mapRefresh;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.yirf.mapRefresh.utils.TimeUtil;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;

public class MapExpansion extends PlaceholderExpansion {

    String identifier;

    public MapExpansion(@Nullable String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getAuthor() {
        return "Yirf";
    }
    @Override
    public String getIdentifier() {
        return identifier;
    }
    @Override
    public String getVersion() {
        return "1.0";
    }
    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, String params) {
        if (TimeUtil.compare(MapRefresh.refreshTimer) < 1) {
            return "resetting...";
        }
        return TimeUtil.millisToString(MapRefresh.refreshTimer);
    }
}
