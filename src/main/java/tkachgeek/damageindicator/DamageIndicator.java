package tkachgeek.damageindicator;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.damageindicator.config.Cfg;

public final class DamageIndicator extends JavaPlugin {
  
  public static JavaPlugin plugin;
  
  @Override
  public void onEnable() {
    plugin = this;
    Cfg.load(this);
    Bukkit.getPluginManager().registerEvents(new EventListener(), this);
  }
  
  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
