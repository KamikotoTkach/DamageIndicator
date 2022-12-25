package tkachgeek.damageindicator.config;

import org.bukkit.plugin.java.JavaPlugin;

public class Cfg {
  
  public static Colors colors;
  
  public static void load(JavaPlugin plugin) {
    colors = new Colors("colors", plugin);
  }
}
