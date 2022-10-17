package com.bondegaard.shop.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Utils {

    public static String locationToString(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
    }

    public static Location stringToLocation(String string) {
        String[] parts = string.split(";");
        if (parts.length != 6) return new Location(Bukkit.getWorld("world"), 0, 0, 0);

        World world = Bukkit.getWorld(parts[0]);
        float x = Float.parseFloat(parts[1]);
        float y = Float.parseFloat(parts[2]);
        float z = Float.parseFloat(parts[3]);
        float yaw = Float.parseFloat(parts[4]);
        float pitch = Float.parseFloat(parts[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }
    public static int generate(int min,int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    //check the amount the player can put inside its inventory of a specifik item..
    public static int amountForItemInInventory(ItemStack itemToAdd, Player player){
        int freeSpace = 0;
        for (ItemStack i : player.getInventory()) {
            if (i == null) {
                freeSpace+=itemToAdd.getType().getMaxStackSize();
            } else if (i.getType() == itemToAdd.getType()) {
                freeSpace+=i.getType().getMaxStackSize() - i.getAmount();
            }
        }
        return freeSpace;
    }

    public static int amountOfItemInInventory(ItemStack itemToAdd, Player player) {
        int currentInv = 0;
        for (ItemStack i : player.getInventory()) {
            if(i!=null) {
                if (i.getType() == itemToAdd.getType()) {
                    if (i.isSimilar(itemToAdd))
                        currentInv += i.getAmount();
                }
            }
        }
        return currentInv;
    }
    public static void giveAmount(ItemStack item, Player p, int amount){
        PlayerInventory inventory = p.getInventory();
        item.setAmount(amount);
        inventory.addItem(item);
    }
    public static void removeItemsFromInventory(Inventory inventory, ItemStack item, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (item.isSimilar(is)) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }

    public static void removeItemsFromInventory(Inventory inventory, ItemStack item) {
        int amount = item.getAmount();
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (item.isSimilar(is)) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }
}
