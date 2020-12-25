package net.imjudass.flare.cmds;

import net.imjudass.flare.Main;
import net.imjudass.flare.events.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class FlareCommand implements CommandExecutor {

    private Main main;

    public FlareCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
        if (commandSender instanceof Player) {
            final Player p = (Player) commandSender;
            if (!p.hasPermission(main.getMesssage("Setup.permission"))) {
                p.sendMessage(main.getMesssage("Messages.no-permission"));
                return true;
            }

            if (args.length == 0 || args.length == 1 || args.length > 2) {
                p.sendMessage(main.getMesssage("Messages.command-use"));
            }

            if (args.length == 2) {
                final Player t = Bukkit.getPlayer(args[0]);
                if (t == null) {
                    return false;
                }

                for (int i = 0; i < Integer.valueOf(args[1]); i++) {
                    t.getInventory().addItem(new ItemBuilder(Material.TORCH).setName(main.getMesssage("Messages.name-item")).addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1).toItemStack());
                }
                p.sendMessage(main.getMesssage("Messages.succes"));
            }

        }
        return false;
    }


}