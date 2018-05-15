package me.therealdan.dc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Daniel on 14/01/2017.
 */
public class CommandsHandler implements CommandExecutor {

    public DeluxeColors m;

    public CommandsHandler(DeluxeColors deluxeColors) {
        m = deluxeColors;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.equalsIgnoreCase("Color") || command.equalsIgnoreCase("Colors")) {
                m.colorPicker.open(m, player);
            }
        }
        return true;
    }
}
