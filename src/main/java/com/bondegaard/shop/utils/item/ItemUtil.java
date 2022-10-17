package com.bondegaard.shop.utils.item;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {

    public static ItemStack getConfigItem(String path, ConfigurationSection config) {

        ConfigurationSection section = config.getConfigurationSection(path);

        //If the item is a custom skull
        if (section.contains("skull") && section.getBoolean("skull")) {
            ItemBuilder itemBuilder = new ItemBuilder(SkullCreator.itemFromBase64(section.getString("skullTexture")));
            itemBuilder.name(section.getString("name"));
            itemBuilder.addLore(section.getStringList("lore"));
            itemBuilder.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
            return itemBuilder.build();

            //If the item is a regular item
        } else {
            String[] itemID = section.getString("type").split(":");
            try {
                int amount = 1;
                if (section.contains("amount"))
                    amount = section.getInt("amount");

                ItemBuilder itemBuilder = new ItemBuilder(
                    /* Material */ Material.getMaterial(Integer.parseInt(itemID[0])),
                    /* Amount */ amount,
                    /* Data */ Short.parseShort(itemID[1])
                );
                String name = "";
                if (section.contains("name"))
                    name = section.getString("name");
                List<String> lore = new ArrayList<>();
                if (section.contains("lore"))
                    lore = section.getStringList("lore");

                itemBuilder.name(name);
                itemBuilder.addLore(lore);

                itemBuilder.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
                if (section.contains("unbreaking") && section.getInt("unbreaking") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.DURABILITY, section.getInt("unbreaking"));
                if (section.contains("protection") && section.getInt("protection") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, section.getInt("protection"));
                if (section.contains("thorns") && section.getInt("thorns") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.THORNS, section.getInt("thorns"));
                if (section.contains("sharpness") && section.getInt("sharpness") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, section.getInt("sharpness"));
                if (section.contains("knockback") && section.getInt("knockback") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.KNOCKBACK, section.getInt("knockback"));
                if (section.contains("power") && section.getInt("power") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, section.getInt("power"));
                if (section.contains("punch") && section.getInt("punch") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, section.getInt("punch"));
                if (section.contains("infinity") && section.getInt("infinity") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
                if (section.contains("flame") && section.getInt("flame") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.ARROW_FIRE, section.getInt("flase"));
                if (section.contains("efficiency") && section.getInt("efficiency") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.DIG_SPEED, section.getInt("efficiency"));
                if (section.contains("fortune") && section.getInt("fortune") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.LUCK, section.getInt("fortune"));
                if (section.contains("depthstrider") && section.getInt("depthstrider") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, section.getInt("depthstrider"));
                if (section.contains("falldamage") && section.getInt("falldamage") > 0)
                    itemBuilder.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, section.getInt("falldamage"));
                if (section.contains("glowing") && section.getBoolean("glowing")) itemBuilder.makeGlowing();
                itemBuilder.colorizeAll();
                return itemBuilder.build();

            } catch (Exception err) {}
        }
        return null;
    }

    public static ItemStack cloneWithNbt(ItemStack item, String compound, String key, String value) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.addCompound(compound)
            .setString(key, value);
        return nbtItem.getItem();
    }

    public static boolean hasNbtKey(ItemStack item, String key) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasNBTData() && nbtItem.hasKey(key);
    }

    public static String getNbtValue(ItemStack item, String compound, String key) {
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound nbtCompound = nbtItem.getCompound(compound);
        return nbtCompound.getString(key);
    }
    public static String getNbtValue(ItemStack item, String key) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getString(key);
    }
    public static ItemStack clearNbtKey(ItemStack item, String key) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.removeKey(key);
        return nbtItem.getItem();
    }

    public static ItemStack clearNbt(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.clearCustomNBT();
        return nbtItem.getItem();
    }
    public static ItemStack setNbt(ItemStack item, String key, String value) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString(key, value);
        return nbtItem.getItem();
    }

}
