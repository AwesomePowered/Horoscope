package net.poweredbyawesome.horoscope;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Lax on 6/22/2017.
 */
public class CommandHoroscope implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (!sender.hasPermission("horoscope.horoscope")) {
            return false;
        }
        if (args.length == 0) {
            String sign = Horoscope.instance.getConfig().getString("signs."+p.getUniqueId().toString());
            if (sign != null) {
                Horoscope.instance.sendHoroscope(p, sign);
            } else {
                p.sendMessage(ChatColor.RED + "Usage: /horoscope SIGN");
            }
        }

        if (args.length >= 1) {
            if (Horoscope.instance.isValidSign(args[0])) {
                Horoscope.instance.sendHoroscope(p, args[0]);
            }
            if (args[0].equalsIgnoreCase("set") && args[1] != null) {
                if (Horoscope.instance.isValidSign(args[1])) {
                    Horoscope.instance.getConfig().set("signs."+p.getUniqueId().toString(), args[1]);
                    Horoscope.instance.saveConfig();
                    p.sendMessage(ChatColor.GREEN + "Sign is set!");
                } else {
                    p.sendMessage(ChatColor.RED + "Invalid Sign!");
                }
            }
        }
        return false;
    }
}
