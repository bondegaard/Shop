package com.bondegaard.shop.utils;

import com.bondegaard.shop.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class FileUtils {
    public static boolean isYamlFile(String paramString) {
        return StringUtil.endsWith(paramString, new String[] { "yml", "yaml" });
    }

    public static void createYamlFile(File file) {
        createYamlFile(file, file.getPath());
    }

    public static void createYamlFile(File file, String resourcePath) {
            if (file.exists()) return;

            InputStream defaultConfig = Main.getInstance().getResource(resourcePath);
            if (defaultConfig != null) {
                Main.getInstance().saveResource(resourcePath, false);
                return;
            }

            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
                Main.getInstance().getLogger().severe("Could not create yaml file for " + resourcePath + "!");
            }
    }

    public static YamlFile saveResourceIfNotAvailable(JavaPlugin plugin, String fileName) throws InvalidConfigurationException {
        return saveResourceIfNotAvailable(plugin, fileName, fileName);
    }

    public static YamlFile saveResourceIfNotAvailable(JavaPlugin plugin, String fileName, String sourceName) throws InvalidConfigurationException{
        File file = getResourceFile(plugin, fileName, sourceName);

        YamlFile yamlFile = new YamlFile(file);
        try {
            yamlFile.load();
        }catch (IOException | InvalidConfigurationException ex){
            Bukkit.getLogger().severe("[UnicCore] Failed to load " + fileName + ", there might be an error in the yaml syntax.");
            if (ex instanceof InvalidConfigurationException){
                throw (InvalidConfigurationException) ex;
            }

            ex.printStackTrace();
            return null;
        }

        return yamlFile;
    }

    public static File getResourceFile(JavaPlugin plugin, String fileName, String sourceName) {
        File file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()){
            // save resource
            plugin.saveResource(sourceName, false);
        }

        if (!fileName.equals(sourceName)){
            File sourceFile = new File(plugin.getDataFolder(), sourceName);
            sourceFile.renameTo(file);
        }

        if (!file.exists()){
            Bukkit.getLogger().severe("[UnicCore] Failed to save file: " + fileName);
        }

        return file;
    }
}