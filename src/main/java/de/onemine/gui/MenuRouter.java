package de.onemine.gui;

import de.onemine.OneMinePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuRouter {
    public static void openMainMenu(Player player, OneMinePlugin plugin) {
        Inventory inv = Bukkit.createInventory(null, 54, "🏴 OneMine Hauptmenü");
        
        // Map (Slot 10)
        ItemStack mapItem = new ItemStack(Material.MAP);
        ItemMeta mapMeta = mapItem.getItemMeta();
        mapMeta.setDisplayName("🗺️ Weltkarte");
        mapItem.setItemMeta(mapMeta);
        inv.setItem(10, mapItem);
        
        // Clan (Slot 12)
        ItemStack clanItem = new ItemStack(Material.BANNER);
        ItemMeta clanMeta = clanItem.getItemMeta();
        clanMeta.setDisplayName("🏴 Piratenbande");
        clanItem.setItemMeta(clanMeta);
        inv.setItem(12, clanItem);
        
        // Bounty (Slot 14)
        ItemStack bountyItem = new ItemStack(Material.SKULL_ITEM);
        ItemMeta bountyMeta = bountyItem.getItemMeta();
        bountyMeta.setDisplayName("💀 Kopfgeld");
        bountyItem.setItemMeta(bountyMeta);
        inv.setItem(14, bountyItem);
        
        // Ships (Slot 16)
        ItemStack shipItem = new ItemStack(Material.OAK_BOAT);
        ItemMeta shipMeta = shipItem.getItemMeta();
        shipMeta.setDisplayName("⛵ Schiffe & Shop");
        shipItem.setItemMeta(shipMeta);
        inv.setItem(16, shipItem);
        
        // Berrys (Slot 28)
        ItemStack berryItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta berryMeta = berryItem.getItemMeta();
        long berrys = plugin.getEconomyService().getBalance(player.getUniqueId());
        berryMeta.setDisplayName("💰 Berrys: " + berrys);
        berryItem.setItemMeta(berryMeta);
        inv.setItem(28, berryItem);
        
        // Kosmetik (Slot 32)
        ItemStack cosmeticItem = new ItemStack(Material.NAME_TAG);
        ItemMeta cosmeticMeta = cosmeticItem.getItemMeta();
        cosmeticMeta.setDisplayName("✨ Kosmetik");
        cosmeticItem.setItemMeta(cosmeticMeta);
        inv.setItem(32, cosmeticItem);
        
        player.openInventory(inv);
    }
}
