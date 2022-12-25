package tkachgeek.damageindicator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import tkachgeek.damageindicator.config.Cfg;

public class Hologram {
  public static void showDamageHologram(Entity entity, double damage, EntityDamageEvent.DamageCause cause, boolean isCritical, boolean isMob) {
    
    Location loc = entity.getLocation().add(Math.random() - 0.5, Math.random() + entity.getHeight() / 1.5, Math.random() - 0.5);
    
    StringBuilder stringBuilder = new StringBuilder();
    if (!isMob) {
      if (isCritical) stringBuilder.append("§e✧ ");
      stringBuilder.append(Cfg.colors.getColor(cause).toString());
      stringBuilder.append("♡");
    } else {
      stringBuilder.append("§c♡");
    }
    stringBuilder.append(Math.round(damage));
    
    showText(stringBuilder.toString(), 20, loc);
  }
  
  public static void showText(String text, int ticksToRemove, Location loc) {
    loc.getWorld().spawn(loc, ArmorStand.class, armorStand -> {
      armorStand.setVisible(false);
      armorStand.setCustomName(text);
      armorStand.setCustomNameVisible(true);
      armorStand.setMarker(true);
      armorStand.setCanMove(false);
      armorStand.addScoreboardTag("toRemove");
      armorStand.setCanTick(false);
      Bukkit.getScheduler().scheduleSyncDelayedTask(DamageIndicator.plugin, armorStand::remove, ticksToRemove);
    });
  }
}
