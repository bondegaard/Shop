package com.bondegaard.shop.playerdata;

import com.bondegaard.shop.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class PlayerDataHandler implements Listener {

    public Map<OfflinePlayer, PlayerDataYAML> playerData = new HashMap<>();

    public PlayerDataHandler() {

        //Load playerdata of all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!playerData.containsKey(player)) {
                playerData.put(player, new PlayerDataYAML(player));
            }
        }

        //Register events
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
        timer();
    }

    public PlayerDataYAML getData(Player player) {
        if (player.isOnline()) {
            return playerData.get(player);
        }
        return new PlayerDataYAML(player);
    }

    public PlayerDataYAML getDataFromUUID(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        if (player.isOnline()) {
            return playerData.get(player);
        }
        return new PlayerDataYAML(player);
    }

    private void timer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Entry<OfflinePlayer, PlayerDataYAML> key: playerData.entrySet()) {
                    key.getValue().save();
                }
            }
        }, 200L, 36000L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        OfflinePlayer player = event.getPlayer();
        if (!playerData.containsKey(player)) {
            playerData.put(player, new PlayerDataYAML(player));
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                OfflinePlayer player = event.getPlayer();
                if (playerData.containsKey(player)) {
                    playerData.get(player).save();
                    playerData.remove(player);
                }
            }
        }, 20L);
    }
}
