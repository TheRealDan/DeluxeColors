package me.therealdan.deluxecolors;

import me.therealdan.deluxecolors.color.Color;
import me.therealdan.deluxecolors.color.ColorHandler;
import me.therealdan.deluxecolors.color.ColorPicker;
import me.therealdan.deluxecolors.commands.ColorCommand;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class DeluxeColors extends JavaPlugin {

    private static DeluxeColors deluxeColors;
    public static String MAIN, SECOND;

    private ColorHandler colorHandler;
    private ColorPicker colorPicker;

    @Override
    public void onEnable() {
        deluxeColors = this;

        saveDefaultConfig();

        MAIN = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Color.Main"));
        SECOND = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Color.Secondary"));

        Color.load();

        getServer().getPluginManager().registerEvents(getColorHandler(), this);
        getServer().getPluginManager().registerEvents(getColorPicker(), this);

        ColorCommand colorCommand = new ColorCommand();
        getCommand("Color").setExecutor(colorCommand);
    }

    @Override
    public void onDisable() {
        getColorPicker().unload();

        Color.unload();
    }

    public ColorHandler getColorHandler() {
        if (colorHandler == null) colorHandler = new ColorHandler();
        return colorHandler;
    }

    public ColorPicker getColorPicker() {
        if (colorPicker == null) colorPicker = new ColorPicker();
        return colorPicker;
    }

    public static DeluxeColors getInstance() {
        return deluxeColors;
    }
}