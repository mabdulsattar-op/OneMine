package de.onemine.listener;

import de.onemine.OneMinePlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MarineListener implements Listener {
    private final OneMinePlugin plugin;

    public MarineListener(OneMinePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMarineDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        
        if (!entity.hasMetadata("marine")) return;
        
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;
        
        long reward = (long) (Math.random() * 
            (plugin.getPluginConfig().getMarineKillMax() - plugin.getPluginConfig().getMarineKillMin()) +
            plugin.getPluginConfig().getMarineKillMin());
        
        plugin.getEconomyService().deposit(killer.getUniqueId(), reward);
        killer.sendMessage("✓ Marine getötet: +" + reward + " Berrys!");
        
        plugin.getMarineService().removeMarineCount();
    }
}
