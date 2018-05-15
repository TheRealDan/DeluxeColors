package me.therealdan.dc.color;

import me.therealdan.dc.DeluxeColors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Daniel on 14/01/2017.
 */
public class ColorPicker {

    private HashMap<Player, String> uiOpen = new HashMap<>();

    public HashMap<String, Boolean> permReq = new HashMap<>();
    public HashMap<Integer, String> slotColor = new HashMap<>();
    public HashMap<String, Integer> colorSlot = new HashMap<>();
    public HashMap<String, String> colorCode = new HashMap<>();
    public HashMap<String, Short> colorDurability = new HashMap<>();

    public String title = "Pick a Color";
    public int size = 9;

    public String lockedName = "%code%%color%";
    public List<String> lockedLore = new ArrayList<>();
    public String selectName = "%code%%color%";
    public List<String> selectLore = new ArrayList<>();

    public String lockedMessage = "&7You don't have permission to use %code%%color%&7!";
    public String selectMessage = "&7You have selected %code%%color%&7!";

    public void onClick(DeluxeColors m, InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!uiOpen.containsKey(player)) return;
        event.setCancelled(true);

        if (slotColor.containsKey(event.getSlot() + 1)) {
            String color = slotColor.get(event.getSlot() + 1);
            if (unlocked(m, player, color)) {
                m.chatHandler.setColorCode(player, colorCode.get(color));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', selectMessage
                        .replace("%code%", colorCode.get(color))
                        .replace("%color%", color)));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', lockedMessage
                        .replace("%code%", colorCode.get(color))
                        .replace("%color%", color)));
            }
            player.closeInventory();
        }
    }

    public void onClose(DeluxeColors m, InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        uiOpen.remove(player);
    }

    public void open(DeluxeColors m, Player player) {
        Inventory inventory = Bukkit.createInventory(null, size, title);
        for (String color : colorCode.keySet())
            inventory.setItem(colorSlot.get(color) - 1, getIcon(m, color, unlocked(m, player, color)));
        uiOpen.put(player, "");
        player.openInventory(inventory);
    }

    public void load(DeluxeColors m) {
        // Messages
        lockedMessage = m.Config.getString("Locked_Message");
        selectMessage = m.Config.getString("Select_Message");

        // Color UI
        title = m.Config.getString("Color_UI.Title");
        size = m.Config.getInt("Color_UI.Size");
        if (!"9-18-27-36-45-54".contains(Integer.toString(size))) size = 18;
        lockedName = m.Config.getString("Color_UI.Locked_Name");
        lockedLore = m.Config.getStringList("Color_UI.Locked_Lore");
        selectName = m.Config.getString("Color_UI.Select_Name");
        selectLore = m.Config.getStringList("Color_UI.Select_Lore");

        // Colors
        permReq = new HashMap<>();
        slotColor = new HashMap<>();
        colorSlot = new HashMap<>();
        colorCode = new HashMap<>();
        colorDurability = new HashMap<>();
        for (String color : m.Config.getConfigurationSection("Colors").getKeys(false)) {
            if (m.Config.getBoolean("Colors." + color + ".Enabled")) {
                permReq.put(color, m.Config.getBoolean("Colors." + color + ".Permission_Required"));
                slotColor.put(m.Config.getInt("Colors." + color + ".Slot"), color);
                colorSlot.put(color, m.Config.getInt("Colors." + color + ".Slot"));
                colorCode.put(color, m.Config.getString("Colors." + color + ".Code"));
                colorDurability.put(color, (short) m.Config.getInt("Colors." + color + ".Durability"));
            }
        }
    }

    public boolean unlocked(DeluxeColors m, Player player, String color) {
        if (!permReq.get(color)) return true;
        if (player.hasPermission("deluxecolors.colors.*")) return true;
        return player.hasPermission("deluxecolors.colors." + color.toLowerCase());
    }

    private ItemStack getIcon(DeluxeColors m, String color, boolean unlocked) {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
        itemStack.setDurability(colorDurability.get(color));
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (unlocked) {
            for (String line : selectLore) {
                lore.add(ChatColor.translateAlternateColorCodes('&', line
                        .replace("%code%", colorCode.get(color))
                        .replace("%color%", color)));
            }
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', selectName
                    .replace("%code%", colorCode.get(color))
                    .replace("%color%", color)));
        } else {
            for (String line : lockedLore) {
                lore.add(ChatColor.translateAlternateColorCodes('&', line
                        .replace("%code%", colorCode.get(color))
                        .replace("%color%", color)));
            }
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', lockedName
                    .replace("%code%", colorCode.get(color))
                    .replace("%color%", color)));
        }
        if (lore.size() > 0) itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}