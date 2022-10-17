package com.bondegaard.shop.utils;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class PlayerUtils {

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(StringUtil.colorize(message));
    }

    public static void sendTextComponent(Player player, TextComponent textComponent) {
        textComponent.setText(StringUtil.colorize(textComponent.getText()));
        player.spigot().sendMessage(textComponent);
    }

    public static void sendParsedPlaceholder(CommandSender sender, PlaceholderString placeholderString) {
        sender.sendMessage(placeholderString.parse());
    }

    public static OfflinePlayer matchOfflinePlayer(String paramString) {
        try {
            UUID uuid = UUID.fromString(paramString);
            OfflinePlayer offlinePlayer1 = Bukkit.getOfflinePlayer(uuid);
            if (offlinePlayer1.hasPlayedBefore() || offlinePlayer1.isOnline())
                return offlinePlayer1;
        } catch (IllegalArgumentException illegalArgumentException) {}
        Player player = Bukkit.getServer().getPlayerExact(paramString);
        if (player != null)
            return (OfflinePlayer)player;
        OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(paramString);
        if (offlinePlayer.hasPlayedBefore())
            return offlinePlayer;
        return (OfflinePlayer)Bukkit.getServer().getPlayer(paramString);
    }
}
