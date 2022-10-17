package com.bondegaard.shop.utils;

import com.bondegaard.shop.Main;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Balance {
    RegisteredServiceProvider<Economy> rsp;
    private Economy economy = null;

    @Getter
    private static Balance instance;


    public Balance() {
        instance = this;
        rsp = Main.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null)
            economy = rsp.getProvider();
    }

    public double getBalance(Player p) {
        if(economy.hasAccount(p)){
            return economy.getBalance(p);
        }
        return 0.0;
    }

    public boolean hasBalance(Player p, double amount) {
        if(economy.hasAccount(p)){
            return economy.getBalance(p) >= amount;
        }
        return false;
    }

    public boolean removeBalance(Player p, double amount) {
        if(economy.hasAccount(p)){
            if (economy.getBalance(p) >= amount) {
                economy.withdrawPlayer(p, amount);
                return true;
            }
        }
        return false;
    }
    public boolean addBalance(Player p, double amount) {
        if(economy.hasAccount(p)){
            economy.depositPlayer(p, amount);
            return true;
        }
        return false;
    }

    public boolean setBalance(Player p, double amount) {
        if(economy.hasAccount(p)) {
            economy.withdrawPlayer(p, economy.getBalance(p));
            economy.depositPlayer(p, amount);
            return true;
        }
        return false;
    }
}
