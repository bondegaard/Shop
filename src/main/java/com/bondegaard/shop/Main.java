package com.bondegaard.shop;

import com.bondegaard.shop.license.AdvancedLicense;
import com.bondegaard.shop.metric.Metrics;
import com.bondegaard.shop.playerdata.PlayerDataHandler;
import com.bondegaard.shop.pluginmodule.PluginModuleManager;
import com.bondegaard.shop.storage.H2Storage;
import com.bondegaard.shop.storage.MariaDBStorage;
import com.bondegaard.shop.storage.SqlConnection;
import com.bondegaard.shop.utils.Balance;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter
    private PlayerDataHandler playerDataHandler;

    @Getter
    private PluginModuleManager pluginModuleManager;

    @Getter
    private SqlConnection sqlConnection;

    private boolean license;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        license = (new AdvancedLicense(getConfig().getString("license"), "http://butik.mcsetups.dk/license/verify.php", (Plugin)this)).register();
        if (!license)  return;

        initUtils();

        playerDataHandler = new PlayerDataHandler();
        pluginModuleManager = new PluginModuleManager(this);
        pluginModuleManager.initModules();

        initDB();

        Metrics metrics = new Metrics(this, 16622);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        if (!license) return;
        pluginModuleManager.unloadModules();
    }

    private void initUtils() {
        new Balance();
    }

    private void initDB() {
        String type = Main.getInstance().getConfig().getString("database.type");
        if (type.equalsIgnoreCase("MariaDB"))
            sqlConnection = new MariaDBStorage();
        else
            sqlConnection = new H2Storage();
    }
}
