package ifarded.lol.ifu.listeners;

import java.util.List;

import ifarded.lol.ifu.IFUtilities;
import ifarded.lol.ifu.util.IFDeco;
import net.kyori.adventure.text.Component;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onLoginOpCheck(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if (p.isOp() && IFUtilities.getPlugin().getConfig().getBoolean("op-protection")) {
            List<String> allowedOperatorList = IFUtilities.getPlugin().getConfig().getStringList("allowed-operators");
            if (!allowedOperatorList.contains(p.getName())) {
                String message = IFUtilities.getColoredConfigString("unauthorized-operator-kick-message");
                e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                e.getPlayer().kick(Component.text(message));
                IFUtilities.getPlugin().getServer().getConsoleSender().sendMessage(message);
                p.setOp(false);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage(Component.text("Welcome to the iFarded Industries Minecraft server!")
                .color(IFDeco.GREEN)
                .decorate(IFDeco.BOLD)
                .appendNewline()
                .append(Component.text("Remember that "))
                .append(
                    Component.text("griefing is a bannable offense!")
                    .color(IFDeco.AQUA)
                ));
                e.getPlayer().sendPlayerListHeaderAndFooter(
                    Component.text("iFarded Industries Minecraft")
                    .color(IFDeco.GREEN),
                    Component.text("ifarded.lol")
                    .color(IFDeco.AQUA)
                    );
    }
}
