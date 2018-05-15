package me.therealdan.dc;

import me.therealdan.dc.api.API;
import me.therealdan.dc.api.DeluxeColorsAPI;
import me.therealdan.dc.color.ColorPicker;
import me.therealdan.dc.events.ChatHandler;
import me.therealdan.dc.events.InventoryHandler;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by Daniel on 13/01/2017.
 */
public class DeluxeColors extends JavaPlugin {

    // API
    private static DeluxeColorsAPI api;

    // Classes
    public ColorPicker colorPicker = new ColorPicker();

    // Events
    public ChatHandler chatHandler = new ChatHandler(this);

    // Configuration
    public FileConfiguration PlayerData, Config = getConfig();
    public File PlayerDataFile = new File(getDataFolder(), "PlayerData.yml");

    public String color, color2;

    public void onEnable() {
        getCommand("Color").setExecutor(new CommandsHandler(this));
        getCommand("Colors").setExecutor(new CommandsHandler(this));

        getServer().getPluginManager().registerEvents(new ChatHandler(this), this);
        getServer().getPluginManager().registerEvents(new InventoryHandler(this), this);

        saveDefaultConfig();
        PlayerData = YamlConfiguration.loadConfiguration(PlayerDataFile);
        savePlayerData();

        color = ChatColor.translateAlternateColorCodes('&', Config.getString("Color.1"));
        color2 = ChatColor.translateAlternateColorCodes('&', Config.getString("Color.2"));

        chatHandler.useUnlockedCodes = Config.getBoolean("Use_Unlocked_Codes_In_Chat");

        colorPicker.load(this);

        if (api == null) api = new API(this);

        getLogger().info("Custom plugin by TheRealDan");
    }

    public void savePlayerData() {
        try {
            PlayerData.save(PlayerDataFile);
        } catch (Exception e) {
            //
        }
    }

    public static DeluxeColorsAPI getApi() {
        return api;
    }

}