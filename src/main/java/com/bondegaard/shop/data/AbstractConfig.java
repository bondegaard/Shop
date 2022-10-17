package com.bondegaard.shop.data;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AbstractConfig {

    protected final Plugin plugin;

    @Getter
    private final String resourcePath;

    private final File configFile;

    @Getter
    protected FileConfiguration fileConfiguration;

    public AbstractConfig(Plugin plugin, String resourcePath) {
        this.plugin = plugin;
        this.resourcePath = resourcePath;
        this.configFile = new File(plugin.getDataFolder(), resourcePath);
    }

    public void setupConfigFile(boolean copyDefaults) {
        this.saveDefaultConfig();

        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);

        if (copyDefaults) this.copyDefaults();
    }

    public void saveConfigToDisk() {
        try {
            this.fileConfiguration.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
            this.plugin.getLogger().severe("Could not save config file for " + this.resourcePath + "!");
        }
    }

    public void saveDefaultConfig() {
        if (this.configFile.exists()) return;

        InputStream defaultConfig = this.plugin.getResource(this.resourcePath);
        if (defaultConfig != null) {
            this.plugin.saveResource(this.resourcePath, false);
            return;
        }

        try {
            this.configFile.getParentFile().mkdirs();
            this.configFile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
            this.plugin.getLogger().severe("Could not create config file for " + this.resourcePath + "!");
        }
    }

    public void reloadConfig(boolean copyDefaults) {
        this.saveDefaultConfig();

        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);

        if (copyDefaults) this.copyDefaults();
    }

    private void copyDefaults() {
        InputStream defaultResourceConfig = this.plugin.getResource(this.resourcePath);
        if (defaultResourceConfig != null) {
            YamlConfiguration defaultYamlConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultResourceConfig));
            this.fileConfiguration.setDefaults(defaultYamlConfig);
        }

        this.fileConfiguration.options().copyDefaults(true);

        this.saveConfigToDisk();
    }

}
