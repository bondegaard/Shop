package com.bondegaard.shop.data;

import com.bondegaard.shop.utils.item.ItemBuilder;
import com.bondegaard.shop.utils.item.LightItem;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemConverter {

    public static LightItem parseLightItem(ConfigurationSection lightItemConfig) {
        return LightItem.from(parseItem(lightItemConfig));
    }

    public static ItemStack parseItem(ConfigurationSection itemConfig) {
        Material itemType = Material.matchMaterial(itemConfig.getString("type"));
        short data = (short) itemConfig.getInt("data");
        ItemBuilder itemBuilder = new ItemBuilder(itemType, itemConfig.getInt("amount", 1), data);

        if (itemConfig.contains("name"))
            itemBuilder.name(itemConfig.getString("name"));

        if (itemConfig.contains("lore"))
            itemBuilder.lore(itemConfig.getStringList("lore"));

        if (itemConfig.getBoolean("glowing"))
            itemBuilder.makeGlowing();

        if (itemConfig.getBoolean("unbreakable"))
            itemBuilder.unbreakable(true);

        itemBuilder.addUnsafeEnchantments(parseEnchants(itemConfig.getStringList("enchants")));
        itemBuilder.addItemFlags(itemConfig.getStringList("flags"));

        ItemStack parsedItem = itemBuilder.colorizeAll()
            .build();


        return parsedItem;
    }

    public static Map<Enchantment, Integer> parseEnchants(List<String> levelsByEnchant) {
        Map<Enchantment, Integer> parsedEnchants = new HashMap<>();

        for (String line : levelsByEnchant) {
            String[] separation = line.split(":");
            if (separation.length == 0) continue;

            Enchantment enchant = Enchantment.getByName(separation[0].toUpperCase());
            if (enchant == null) continue;

            int level = separation.length > 1 ? NumberUtils.toInt(separation[1], 1) : 1;
            parsedEnchants.put(enchant, level);
        }
        return parsedEnchants;
    }

}
