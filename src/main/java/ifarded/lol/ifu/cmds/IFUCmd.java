package ifarded.lol.ifu.cmds;

import java.util.ArrayList;
import java.util.List;

import ifarded.lol.ifu.IFUtilities;
import ifarded.lol.ifu.listeners.OpListener;
import ifarded.lol.ifu.util.IFDeco;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class IFUCmd implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(IFUtilities.prefixedMessage(Component.text("Not enough arguments!").color(IFDeco.RED)));
            sender.sendMessage(IFUtilities.prefixedMessage(Component.text("Try /ifu help")));
            return true;
        }
        switch (args[0]) {
            case "civilization":
                sender.sendMessage(IFUtilities.prefixedMessage(Component.text("Sending you to the civilization!").color(IFDeco.GREEN)));
                Player p = (Player) sender;
                p.teleport(new Location(p.getWorld(), -80, 64, 92));
                return true;
            case "opme":
                sender.sendMessage(
                    Component.text("[Server: Made " + sender.getName() + " a server operator]")
                    .color(IFDeco.GRAY)
                    .decorate(TextDecoration.ITALIC)
                );
                // "&7&o[Server: Made " + sender.getName() + " a server operator]"
                return true;
            case "rules":
                sender.sendMessage(IFUtilities.prefixedMessage(Component.text("Get trolled, the rules are in #rules in Discord.")));
                return true;
            case "discord":
                sender.sendMessage(IFUtilities.prefixedMessage(Component.text("Join the Discord at discord.gg/fSwGwbVDsK").color(IFDeco.GREEN)));
                return true;
            case "reload":
            case "rl":
                rl(sender);
                return true;
            case "help":
                sender.sendMessage(IFUtilities.prefixedMessage(Component.text("IFU v" + (IFUtilities.getPlugin()).CURRENT_VERSION + ", Config is @v" + (IFUtilities.getPlugin()).CONFIG_VERSION)));
                sender.sendMessage("/ifu civilization Go to the civilization");
                sender.sendMessage("/ifu reload Reloads the config");
                sender.sendMessage("/ifu rules See the server rules");
                sender.sendMessage("/ifu discord Get an invite to our Discord server");
                sender.sendMessage("/ifu opme OP yourself!");
                return true;
        }
        sender.sendMessage(IFUtilities.prefixedMessage(Component.text("Command does not exist!").color(IFDeco.RED)));
        sender.sendMessage("Do /ifu help");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> commands = new ArrayList<>();
            commands.add("rules");
            commands.add("help");
            commands.add("discord");
            commands.add("opme");
            commands.add("civilization");
            if (sender.hasPermission("ifu.reload")) {
                commands.add("rl");
                commands.add("reload");
            }
            return commands;
        }
        return null;
    }

    private static void rl(CommandSender sender) {
        if (sender.hasPermission("ifu.reload")) {
            IFUtilities.getPlugin().createConfig();
            IFUtilities.getPlugin().reloadConfig();
            OpListener.allowedOperators = IFUtilities.getPlugin().getConfig().getStringList("allowed-operators");
            sender.sendMessage(IFUtilities.prefixedMessage(Component.text("Reloaded config!").color(IFDeco.GREEN)));
            IFUtilities.checkGroups();
            Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
        }
    }
}
