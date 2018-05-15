package me.therealdan.dc.events;

import me.therealdan.dc.DeluxeColors;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Daniel on 14/01/2017.
 */
public class ChatHandler implements Listener {

    public DeluxeColors m;

    public ChatHandler(DeluxeColors deluxeColors) {
        m = deluxeColors;
    }

    public boolean useUnlockedCodes = true;

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        if (useUnlockedCodes) {
            Player player = event.getPlayer();
            for (String color : m.colorPicker.colorCode.keySet())
                if (m.colorPicker.unlocked(m, player, color))
                    message = message.replace(m.colorPicker.colorCode.get(color), ChatColor.translateAlternateColorCodes('&', m.colorPicker.colorCode.get(color)));
        }
        event.setMessage(ChatColor.translateAlternateColorCodes('&', getColorCode(m, event.getPlayer())) + message);
    }

    public void setColorCode(Player player, String code) {
        m.PlayerData.set("Players." + player.getUniqueId() + ".Code", code);
        m.savePlayerData();
    }

    public String getColorCode(DeluxeColors m, Player player) {
        if (m.PlayerData.contains("Players." + player.getUniqueId() + ".Code")) return m.PlayerData.getString("Players." + player.getUniqueId() + ".Code");
        return m.Config.getString("Colors." + m.Config.getString("Default_Color") + ".Code");
    }

}