package com.bondegaard.shop.utils;

import com.bondegaard.shop.utils.item.ItemBuilder;
import com.bondegaard.shop.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GUI implements Listener {

    protected Player player;
    protected Inventory menu;

    public Map<Integer, ItemStack> layout = new HashMap<>();

    public final Long clickCooldown = 100L;
    private Long currentCooldown = 0L;

    private boolean canClick;


    public GUI(Player player, int rows, String name) {
        this.player = player;
        this.menu = Bukkit.createInventory(null, (rows*9), name);
        canClick = true;
    }

    public void render() {
        menu.clear();

        for (Map.Entry<Integer, ItemStack> layoutItem : layout.entrySet()) {
            try {
                menu.setItem(layoutItem.getKey(), layoutItem.getValue());
            } catch (NullPointerException ignored) {}
        }
    }

    public enum GUIItem {

        EXIT_MENU(new ItemBuilder(Material.BARRIER,1, (byte) 0,"§c§lLuk Menuen", "").build()),
        GLASS_FILL(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 0, "§r").build()),
        ORANGE_GLASS_FILL(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 1, "§r").build()),
        CYAN_GLASS_FILL(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 9, "§r").build()),
        YELLOW_GLASS_FILL(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 4, "§r").build()),
        GREEN_GLASS_FILL(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 13, "§r").build()),
        GRAY_GLASS_FILL(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7, "§r").build()),
        BACK(new ItemBuilder(Material.ARROW, 1, (byte) 0, "§f§nTilbage","","§7Tryk for at gå tilbage","§7til den forrige menu.").build());

        private final ItemStack item;
        GUIItem(ItemStack item) {
            this.item = item;
        }
        public ItemStack getItem() {
            return this.item;
        }
    }

    //This is the event called when clicking.
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) { }
    public void closed() {}


    //Click cooldown.
    private void setCooldown() {
        this.currentCooldown = System.currentTimeMillis();
    }
    private boolean hasCooldown() {
        return System.currentTimeMillis() < currentCooldown + clickCooldown;
    }

    //Open the menu to a player here
    public void open() {
        this.render();
        player.openInventory(menu);
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    //EVENT LISTENERS
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!event.getInventory().equals(menu)) return;

        event.setCancelled(true);

        if(hasCooldown()) return;
        setCooldown();

        if(event.getClickedInventory() == null) return;
        if (!canClick) return;
        this.click(event.getRawSlot(), event.getCurrentItem(), event.getClick(), event.getClickedInventory().getType());
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        if (!event.getInventory().equals(menu)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        if (event.getInventory().equals(menu)) {
            canClick = false;
            HandlerList.unregisterAll(this);
            this.closed();
        }
    }

}
