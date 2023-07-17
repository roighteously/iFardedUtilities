package ifarded.lol.ifu;

import ifarded.lol.ifu.events.PlayerSitEvent;
import ifarded.lol.ifu.events.PlayerStopSittingEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class IFPlayer {
   private Player player;
   private IFUtilities ifu;

   public IFPlayer(Player player) {
      this.player = player;
      this.ifu = (IFUtilities)JavaPlugin.getPlugin(IFUtilities.class);
   }

   public Player getBukkitPlayer() {
      return this.player;
   }

   public void setSitting(boolean arg) {
      if (arg && !this.isSitting()) {
         Location location = this.player.getLocation();
         ArmorStand seat = (ArmorStand)location.getWorld().spawn(location.clone().subtract(0.0D, 1.7D, 0.0D), ArmorStand.class);
         seat.setGravity(false);
         seat.setVisible(false);
         PlayerSitEvent playerSitEvent = new PlayerSitEvent(this.player, seat, this.ifu.getSitDownMessage());
         Bukkit.getPluginManager().callEvent(playerSitEvent);
         if (playerSitEvent.isCancelled()) {
            seat.remove();
            return;
         }

         this.player.sendMessage(playerSitEvent.getMessage());
         seat.setPassenger(this.player);
         this.ifu.getSeats().put(this.player.getUniqueId(), seat);
      } else if (!arg && this.isSitting()) {
         ArmorStand seat = (ArmorStand)this.ifu.getSeats().get(this.player.getUniqueId());
         PlayerStopSittingEvent playerStopSittingEvent = new PlayerStopSittingEvent(this.player, seat, this.ifu.getSitUpMessage());
         Bukkit.getPluginManager().callEvent(playerStopSittingEvent);
         this.player.sendMessage(playerStopSittingEvent.getMessage());
         this.ifu.getSeats().remove(this.player.getUniqueId());
         this.player.eject();
         this.player.teleport(seat.getLocation().clone().add(0.0D, 1.7D, 0.0D));
         seat.remove();
      }

   }

   public boolean isSitting() {
      return this.ifu.getSeats().containsKey(this.player.getUniqueId());
   }
}