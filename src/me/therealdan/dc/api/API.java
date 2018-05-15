package me.therealdan.dc.api;

import me.therealdan.dc.DeluxeColors;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Daniel on 13/02/2017.
 */
public final class API implements DeluxeColorsAPI {

    private DeluxeColors deluxeColors;

    public API(DeluxeColors main) {
        deluxeColors = main;
    }

    public String getChatColor(Player player) {
        return ChatColor.translateAlternateColorCodes('&', deluxeColors.chatHandler.getColorCode(deluxeColors, player));
    }
}