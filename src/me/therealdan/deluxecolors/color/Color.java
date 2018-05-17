package me.therealdan.deluxecolors.color;

import me.therealdan.deluxecolors.DeluxeColors;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Color {

    private static File file;
    private static FileConfiguration data;
    private static String path = "colors.yml";

    private static HashSet<Color> colors = new HashSet<>();

    public static Color DEFAULT;
    public static List<String> locked, unlocked;

    private String id, colorCode;
    private int slot;
    private byte durability;
    private boolean requiresPermission;

    private ItemStack icon;

    private Color(String id) {
        this.id = id;
        this.colorCode = getData().getString("Colors." + getID() + ".ColorCode");
        this.slot = getData().getInt("Colors." + getID() + ".Slot");
        this.durability = (byte) getData().getInt("Colors." + getID() + ".Durability");
        this.requiresPermission = getData().getBoolean("Colors." + getID() + ".Permission_Required");

        colors.add(this);
    }

    private void save() {
        getData().set("Colors." + getID() + ".ColorCode", colorCode);
        getData().set("Colors." + getID() + ".Slot", slot);
        getData().set("Colors." + getID() + ".Durability", durability);
        getData().set("Colors." + getID() + ".Permission_Required", requiresPermission);
    }

    public String getID() {
        return id;
    }

    public String getColorCode() {
        return ChatColor.translateAlternateColorCodes('&', colorCode);
    }

    public int getSlot() {
        return slot;
    }

    public byte getDurability() {
        return durability;
    }

    public boolean hasPermission(Player player) {
        if (!requiresPermission) return true;
        return player.hasPermission("deluxecolors.colors.*") ||
                player.hasPermission("deluxecolors.colors." + getID());
    }

    public ItemStack getIcon(Player player) {
        if (icon == null) {
            icon = new ItemStack(Material.STAINED_GLASS_PANE);
            icon.setDurability(durability);
        }

        List<String> lore = new ArrayList<>();
        for (String line : hasPermission(player) ? unlocked : locked) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line
                    .replace("%player%", player.getName())
                    .replace("%colorcode%", getColorCode())
                    .replace("%colorname%", getID())
            ));
        }

        ItemMeta itemMeta = icon.getItemMeta();
        itemMeta.setDisplayName(lore.remove(0));
        itemMeta.setLore(lore);
        icon.setItemMeta(itemMeta);

        return icon;
    }

    public static void load() {
        if (getData().contains("Colors"))
            for (String id : getData().getConfigurationSection("Colors").getKeys(false))
                new Color(id);

        Color.DEFAULT = byID(DeluxeColors.getInstance().getConfig().getString("Default_Color"));
    }

    public static void unload() {
        getData().set("Colors", null);

        for (Color color : values())
            color.save();

        saveData();
    }

    public static int getInventorySize() {
        int lastSlot = 0;
        for (Color color : values())
            if (color.getSlot() > lastSlot)
                lastSlot = color.getSlot();

        int size = 9;
        while (size < lastSlot) size += 9;
        if (size > 54) size = 54;
        return size;
    }

    public static Color byPlayer(Player player) {
        return DeluxeColors.getInstance().getColorPicker().getColor(player);
    }

    public static Color bySlot(int slot) {
        for (Color color : values())
            if (color.getSlot() == slot)
                return color;
        return null;
    }

    public static Color byID(String id) {
        for (Color color : values())
            if (color.getID().equals(id))
                return color;
        return null;
    }

    public static List<Color> values() {
        return new ArrayList<>(colors);
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
        if (file == null) {
            DeluxeColors.getInstance().saveResource(path, false);
            file = new File(DeluxeColors.getInstance().getDataFolder(), path);
        }
        return file;
    }
}