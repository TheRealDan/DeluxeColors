package me.therealdan.deluxecolors.commands;

import me.therealdan.deluxecolors.DeluxeColors;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ColorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
        Player target = sender instanceof Player ? (Player) sender : null;

        if (args.length > 0) {
            try {
                target = Bukkit.getPlayer(args[0]);
            } catch (Exception e) {
                target = null;
            }
        }

        if (target == null) {
            sender.sendMessage(DeluxeColors.MAIN + "/Color [Player]");
            return true;
        }

        DeluxeColors.getInstance().getColorPicker().open(target);
        sender.sendMessage(sender == target ?
                DeluxeColors.MAIN + "Opened chat color picker" :
                DeluxeColors.SECOND + target.getName() + DeluxeColors.MAIN + " opened chat color picker");

        return true;
    }
}