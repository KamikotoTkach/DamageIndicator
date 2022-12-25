package tkachgeek.damageindicator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EventListener implements Listener {
  
  @EventHandler
  public static void EntityDamage(EntityDamageEvent event) {
    if(!(event.getEntity() instanceof LivingEntity)) return;
    boolean isCritical = false;
    boolean isMob = false;
    if (event instanceof EntityDamageByEntityEvent event1) {
      Entity damager = event1.getDamager();
      if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Player) {
        damager = (Entity) projectile.getShooter();
      }
      if (damager instanceof Player && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
        DamageBar.set((Player) damager, event.getEntity());
        if (damager.getFallDistance() > 0.0 && !damager.isOnGround() && !damager.isInWater() && !damager.isInsideVehicle()) {
          isCritical = true;
        }
      }
    }
    DamageBar.recalculateProgress(event.getEntity(), -event.getFinalDamage());
    Hologram.showDamageHologram(event.getEntity(), event.getFinalDamage(), event.getCause(), isCritical, isMob);
  }
  
  @EventHandler
  public static void EntityRegen(EntityRegainHealthEvent event) {
    Entity entity = event.getEntity();
    if (DamageBar.hasEntity(entity)) {
      DamageBar.recalculateProgress(entity, event.getAmount());
    }
    Hologram.showDamageHologram(event.getEntity(), event.getAmount(), EntityDamageEvent.DamageCause.CUSTOM, false, false);
  }
  
  @EventHandler
  void onMobKill(EntityDeathEvent event) {
    if (DamageBar.entityBossBar.containsKey(event.getEntity())) {
      try {
        Player p = DamageBar.entityBossBar.get(event.getEntity()).getPlayers().get(0);
        if (DamageBar.playerBossBar.containsKey(p)) DamageBar.playerBossBar.get(p).removeAll();
      } catch (Exception ignored) {
      }
      DamageBar.entityBossBar.remove(event.getEntity());
    }
  }
}
