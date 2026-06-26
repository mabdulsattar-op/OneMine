package de.onemine.listener;

import de.onemine.OneMinePlugin;
import de.onemine.model.Clan;
import de.onemine.model.Island;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;
import java.util.UUID;

public class IslandProtectionListener implements Listener {
    private final OneMinePlugin plugin;

    public IslandProtectionListener(OneMinePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        checkProtection(event.getPlayer(), event.getBlock().getLocation(), "platzieren");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        checkProtection(event.getPlayer(), event.getBlock().getLocation(), "abbauen");
    }

    private void checkProtection(Player player, Location loc, String action) {
        if (!plugin.getPluginConfig().isProtectionEnabled()) return;
        if (player.hasPermission("onemine.admin")) return; // Admin bypass
        
        Optional<Island> island = plugin.getIslandService().getIslandAt(loc);
        if (island.isEmpty() || island.get().isFree()) return;
        
        String clanId = island.get().ownerClanId();
        Optional<Clan> clan = plugin.getClanService().getClanById(clanId);
        
        if (clan.isEmpty() || !clan.get().isMember(player.getUniqueId())) {
            player.sendMessage("✗ Du darfst hier nicht " + action + "!");
            // Event wird nicht cancelled, Bukkit macht das
        }
    }
}
