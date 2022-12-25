package tkachgeek.damageindicator.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public abstract class ConfigInstance {
  YamlConfiguration config;
  File file;
  String configName;
  JavaPlugin currentPlugin;
  public static final HashMap<String, ConfigInstance> loadedConfigs = new HashMap<>();


  public void load(String name, JavaPlugin plugin) {
    configName = name;
    currentPlugin = plugin;
    file = new File(plugin.getDataFolder().toString() + File.separatorChar + name + ".yml");
    config = YamlConfiguration.loadConfiguration(file);
    setDefaults();
    loadedConfigs.put(name, this);
  }

  public void dropConfig() {
    file.delete();
    load(configName, currentPlugin);
  }

  public void updateHeader() {
    config.options().header("Configuration file '" + configName + "' for '" + currentPlugin.getName() + "'\nLast save/load in " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
  }

  public double get(String path, double default_value) {
    if (config.contains(path)) return config.getDouble(path);
    return default_value;
  }

  public String get(String path, String default_value) {
    if (config.contains(path)) return config.getString(path);
    return default_value;
  }

  public int get(String path, int default_value) {
    if (config.contains(path)) return config.getInt(path);
    return default_value;
  }

  public List<String> getStringList(String path) {
    return config.getStringList(path);
  }

  public void set(String path, Object value) {
    config.set(path, value);
  }

  public void setDefaults() {
    String path = "/configs" + "/" + configName + ".yml";
    if (ConfigInstance.class.getResourceAsStream(path) != null) {
      try {
        YamlConfiguration defaultConfig = new YamlConfiguration();
        defaultConfig.load(new InputStreamReader(ConfigInstance.class.getResourceAsStream(path), StandardCharsets.UTF_8));

        config.addDefaults(defaultConfig);
        config.options().copyDefaults(true);


      } catch (IOException | InvalidConfigurationException e) {
        e.printStackTrace();
      }
    }
    save();
  }

  public boolean has(String path) {
    return config.contains(path);
  }

  public boolean save() {
    try {
      updateHeader();
      config.save(file);
      Bukkit.getLogger().fine("Config " + configName + " successfully saved");
      return true;
    } catch (IOException e) {
      Bukkit.getLogger().fine("Can`t save config " + configName);
      e.printStackTrace();
      return false;
    }
  }

  public String getName() {
    return configName;
  }

  public boolean reload() {
    if (!save()) return false;
    load(configName, currentPlugin);
    return true;
  }
}
