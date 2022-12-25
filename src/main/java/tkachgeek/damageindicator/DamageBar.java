package tkachgeek.damageindicator;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import tkachgeek.tkachutils.numbers.NumbersUtils;

import java.util.WeakHashMap;

public class DamageBar {
  public static final WeakHashMap<Entity, BossBar> entityBossBar = new WeakHashMap<>();
  public static final WeakHashMap<Player, BossBar> playerBossBar = new WeakHashMap<>();
  
  public static void set(Player player, Entity entity) {
    if (!entityBossBar.containsKey(entity)) {
      recalculateProgress(entity, 0);
    }
      if (!entityBossBar.get(entity).getPlayers().contains(player)) {
        if (playerBossBar.containsKey(player)) playerBossBar.get(player).removePlayer(player);
        entityBossBar.get(entity).addPlayer(player);
        playerBossBar.put(player, entityBossBar.get(entity));
      }
  }
  
  public static void updateBossBar(Entity entity, double progress, double offset) {
    if (!entityBossBar.containsKey(entity)) {
       entityBossBar.put(entity, Bukkit.createBossBar("name", BarColor.RED, BarStyle.SEGMENTED_12));
    }
    entityBossBar.get(entity).setProgress(progress);
    String name;
    if (entity instanceof Player) {
      name = "Игрок " + entity.getName() + ") §c♥" +  NumbersUtils.evalToHalf(((LivingEntity) entity).getHealth()+offset) + "/"+ ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    } else if (((LivingEntity) entity).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
      name = entity.getName() + " §c♥" +  NumbersUtils.evalToHalf(((LivingEntity) entity).getHealth()+offset) + "/" + ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + " §4⚔" + ((LivingEntity) entity).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue();
    } else {
      name = entity.getName() + " §c♥" + NumbersUtils.evalToHalf(((LivingEntity) entity).getHealth()+offset) + "/" + ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    }
    if (entity instanceof Player) {
      entityBossBar.get(entity).setStyle(BarStyle.SOLID);
    } else {
      entityBossBar.get(entity).setStyle(BarStyle.SEGMENTED_12);
    }
    entityBossBar.get(entity).setTitle(name);
  }
  
  public static boolean hasEntity(Entity entity) {
    return entityBossBar.containsKey(entity);
  }
  
  public static void recalculateProgress(Entity entity, double offset) {
    double progress = (((LivingEntity) entity).getHealth() + offset) / ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    if (progress > 1.0) progress = 1.0;
    if (progress < 0.0) progress = 0.0;
    updateBossBar(entity, progress, offset);
  }
}
