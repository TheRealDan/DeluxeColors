package me.therealdan.deluxecolors.color;

import me.therealdan.deluxecolors.DeluxeColors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class ColorPicker implements Listener {

    private static File file;
    private static FileConfiguration data;
    private static String path = "data/playerColors.yml";

    private HashMap<UUID, Color> playerColors = new HashMap<>();
    private HashSet<UUID> uiOpen = new HashSet<>();

    private String title;
    private String noPermission, selected;

    public ColorPicker() {
        if (getData().contains("Players"))
            for (String uuid : getData().getConfigurationSection("Players").getKeys(false))
                this.playerColors.put(UUID.fromString(uuid), Color.byID(getData().getString("Players." + uuid)));

        this.title = ChatColor.translateAlternateColorCodes('&', getData().getString("ColorPicked_UI.Title"));
        this.noPermission = ChatColor.translateAlternateColorCodes('&', getData().getString("Messages.No_Permission"));
        this.selected = ChatColor.translateAlternateColorCodes('&', getData().getString("Messages.Selected"));

        Color.locked = getData().getStringList("ColorPicker_UI.Locked");
        Color.unlocked = getData().getStringList("ColorPicker_UI.Unlocked");
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!hasUIOpen(player)) return;
        event.setCancelled(true);

        Color color = Color.bySlot(event.getSlot());
        if (color == null) return;

        boolean change = color.hasPermission(player);

        if (change) setColor(player, color);
        player.sendMessage(change ? selected : noPermission);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        uiOpen.remove(event.getPlayer().getUniqueId());
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, Color.getInventorySize(), title);

        for (Color color : Color.values())
            inventory.setItem(color.getSlot(), color.getIcon(player));

        player.openInventory(inventory);
        uiOpen.add(player.getUniqueId());
    }

    public void setColor(Player player, Color color) {
        playerColors.put(player.getUniqueId(), color);
    }

    public void unload() {
        getData().set("Players", null);

        for (UUID uuid : playerColors.keySet())
            getData().set("Players." + uuid.toString(), playerColors.get(uuid).getID());

        saveData();
    }

    public boolean hasUIOpen(Player player) {
        return uiOpen.contains(player.getUniqueId());
    }

    public Color getColor(Player player) {
        return playerColors.getOrDefault(player.getUniqueId(), Color.DEFAULT);
    }

    private static void saveData() {
        try {
            getData().save(getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FileConfiguration getData() {
        if (data == null) data = YamlConfiguration.loadConfiguration(getFile());
        return data;
    }

    private static File getFile() {
        if (file == null) file = new File(DeluxeColors.getInstance().getDataFolder(), path);
        return file;
    }
}