package tkachgeek.damageindicator.config;

import org.bukkit.ChatColor;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Colors extends ConfigInstance {
  public Colors(String name, JavaPlugin plugin) {
    load(name, plugin);
  }

  public ChatColor getColor(EntityDamageEvent.DamageCause cause) {
    return ChatColor.getByChar(get(cause.toString(), get("another", "§f")));
  }
}
