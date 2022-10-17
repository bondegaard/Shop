package com.bondegaard.shop.pluginmodule;

import com.bondegaard.shop.Main;
import com.bondegaard.shop.data.FieldLoaderConfig;
import com.bondegaard.shop.utils.PlaceholderString;
import com.bondegaard.shop.utils.PlayerUtils;
import com.bondegaard.shop.utils.StringUtil;
import lombok.*;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor( access = AccessLevel.PROTECTED )
public abstract class PluginModule implements Listener {

    @Getter
    protected final Main plugin;

    @Getter
    private final String name;

    @Getter
    private FieldLoaderConfig moduleConfig;

    @Getter
    @Setter( AccessLevel.PACKAGE )
    private boolean enabled;

    private List<Integer> trackedBukkitTasks = new ArrayList<>();

    void enableModule() {
        if (this.enabled) return;
        this.enabled = true;

        this.loadConfig();

        if (!this.getRootConfig().getBoolean("enabled", true)) {
            Bukkit.getConsoleSender().sendMessage(String.format("Module §6\"%s\" §fhas explicitly been §cdisabled§f.", this.name));
            return;
        }

        this.onEnable();

        this.loadEvents();

        Bukkit.getConsoleSender().sendMessage(String.format("Module §6\"%s\" §fhas been §aenabled§f.", this.name));
    }

    void disableModule() {
        this.disableModule(false);
    }

    void disableModule(boolean saveConfig) {
        if (!this.enabled) return;
        this.enabled = false;

        this.unloadEvents();
        this.unloadTasks();

        this.onDisable();

        if (saveConfig)
            this.saveConfig();

        Bukkit.getConsoleSender().sendMessage(String.format("Module §6\"%s\" §fhas been §cdisabled§f.", this.name));
    }

    void reloadModule() {
        this.disableModule();
        this.enableModule();
    }

    private void loadConfig() {
        File configFile = new File("modules", this.name + ".yml");
        this.moduleConfig = new FieldLoaderConfig(this, this.plugin, configFile.getPath());

        this.moduleConfig.setupConfigFile(false);
        this.moduleConfig.loadConfigOptionFields();
    }

    private void loadEvents() {
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    private void unloadEvents() {
        HandlerList.unregisterAll(this.plugin);
    }

    private void unloadTasks() {
        this.trackedBukkitTasks.forEach(taskId -> Bukkit.getScheduler().cancelTask(taskId));
        this.trackedBukkitTasks.clear();
    }

    public void trackBukkitTask(BukkitTask task) {
        this.trackedBukkitTasks.add(task.getTaskId());
    }

    @SneakyThrows
    public void consoleLog(String message, Object... variables) {
        PlayerUtils.sendMessage(Bukkit.getConsoleSender(), String.format("[%s] %s", this.name, String.format(message, variables)));
    }

    protected boolean hasBasePermission(Player player) {
        return player.hasPermission("unicmc.base." + this.name);
    }

    protected boolean hasAdminPermission(Player player) {
        return player.hasPermission("unicmc.admin." + this.name);
    }

    protected boolean hasModulePermission(Player player, String permissionNode) {
        return player.hasPermission(String.format("unicmc.module.%s.%s", this.name, permissionNode));
    }

    public FileConfiguration getRootConfig() {
        return this.moduleConfig.getFileConfiguration();
    }

    public ConfigurationSection getOptionsConfig() {
        return this.getRootConfig().getConfigurationSection("options");
    }

    public void saveConfig() {
        this.moduleConfig.saveConfigToDisk();
    }

    protected abstract void onEnable();

    protected abstract void onDisable();

    protected abstract void reload();

    public void reloadAll() {
        moduleConfig.reloadConfig(false);
        reload();
        Bukkit.getConsoleSender().sendMessage(String.format("Module §6\"%s\" §fhas been §ereloaded§f.", this.name));
    }

    public void sendLangMessage(CommandSender sender, String path) {
        PlayerUtils.sendMessage(sender, this.getLang(path));
    }

    public PlaceholderString getLangAsPlaceholder(String path, String... placeholderNames) {
        return new PlaceholderString(this.getLang(path), placeholderNames);
    }

    public String getLang(String path) {
        String langPath = "lang." + path;

        if (!this.getOptionsConfig().contains(langPath)) return "";

        String message = this.getOptionsConfig().getString("lang." + path);
        return StringUtil.colorize(message);
    }

    public String formatDuration(Duration duration) {
        return DurationFormatUtils.formatDurationWords(duration.toMillis(), true, true)
            .replace("days", "dage")
            .replace("day", "dag")
            .replace("hours", "timer")
            .replace("hour", "time")
            .replace("minutes", "minutter")
            .replace("minute", "minut")
            .replace("seconds", "sekunder")
            .replace("second", "sekund");
    }

    public void sendLang(Player player, String type) {
        List<String> message = getOptionsConfig().getStringList("lang."+type);
        for (String msg : message)
            player.sendMessage(StringUtil.colorize(msg));
    }
    public void broadcastLang(String type) {
        List<String> message = getOptionsConfig().getStringList("lang."+type);
        for (String msg : message)
            Bukkit.broadcastMessage(StringUtil.colorize(msg));
    }
    public String getLang(Player player, String type) {
        return StringUtil.colorize(getOptionsConfig().getString("lang."+type));
    }
}
