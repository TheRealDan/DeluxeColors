package me.therealdan.deluxecolors.color;

import me.therealdan.deluxecolors.DeluxeColors;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ColorHandler implements Listener {

    private boolean addColorToMessage;

    private boolean useChatFormat;
    private String chatFormat;

    public ColorHandler() {
        addColorToMessage = DeluxeColors.getInstance().getConfig().getBoolean("Chat.Add_Color_To_Message");

        useChatFormat = DeluxeColors.getInstance().getConfig().getBoolean("Chat.Format.Override");
        chatFormat = ChatColor.translateAlternateColorCodes('&', DeluxeColors.getInstance().getConfig().getString("Chat.Format.Format"));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Color color = Color.byPlayer(player);

        if (useChatFormat) {
            event.setFormat(chatFormat
                    .replace("%player%", player.getName())
                    .replace("%message%", event.getMessage())
                    .replace("%colorcode%", color.getColorCode())
                    .replace("%colorname%", color.getID())
            );
            return;
        }

        if (addColorToMessage) {
            event.setMessage(color.getColorCode() + event.getMessage());
        }
    }
}