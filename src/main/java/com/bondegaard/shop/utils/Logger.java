package com.bondegaard.shop.utils;

import org.bukkit.Bukkit;

public class Logger {
    public static void info(String... messages) {
        for (String message : messages)
            Bukkit.getLogger().info(message);
    }

    public static void warning(String... messages) {
        warn(messages);
    }

    public static void warn(String... messages) {
        for (String message : messages)
            Bukkit.getLogger().warning(message);
    }

    public static void severe(String... messages) {
        for (String message : messages)
            Bukkit.getLogger().severe(message);
    }
}
