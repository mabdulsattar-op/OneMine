package de.onemine.listener;

import de.onemine.OneMinePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {
    private final OneMinePlugin plugin;

    public MenuListener(OneMinePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv == null || !inv.getName().contains("OneMine")) return;
        
        event.setCancelled(true);
        
        int slot = event.getRawSlot();
        
        // Menü-Logik hier hinzufügen
        plugin.getLogger().info("Menü-Klick auf Slot: " + slot);
    }
}
