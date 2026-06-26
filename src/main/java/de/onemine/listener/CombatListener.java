package de.onemine.listener;

import de.onemine.OneMinePlugin;
import de.onemine.config.PluginConfig;
import de.onemine.model.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CombatListener implements Listener {
    private final OneMinePlugin plugin;

    public CombatListener(OneMinePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        if (killer != null) {
            // PvP Kill
            PluginConfig config = plugin.getPluginConfig();
            long reward = config.getPvpKillReward();
            
            // Berrys zum Killer
            plugin.getEconomyService().deposit(killer.getUniqueId(), reward);
            
            // Bounty erhöhen
            plugin.getBountyService().addGlobalBounty(killer.getUniqueId(), config.getPvpIncrement());
            
            killer.sendMessage("✓ Du hast " + reward + " Berrys verdient!");
            victim.sendMessage("✗ Du wurdest von " + killer.getName() + " getötet!");
            
            plugin.getLogger().info("⚔️ PvP Kill: " + killer.getName() + " tötete " + victim.getName());
        }
    }
}
