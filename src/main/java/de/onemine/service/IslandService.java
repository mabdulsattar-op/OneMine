package de.onemine.service;

import de.onemine.OneMinePlugin;
import de.onemine.model.Island;
import de.onemine.model.IslandBiome;
import de.onemine.model.IslandState;
import de.onemine.persistence.DataStore;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class IslandService {
    private final OneMinePlugin plugin;
    private final DataStore dataStore;

    public IslandService(OneMinePlugin plugin, DataStore dataStore) {
        this.plugin = plugin;
        this.dataStore = dataStore;
    }

    public List<Island> getAllIslands() {
        return dataStore.getAllIslands();
    }

    public Optional<Island> getIslandById(String id) {
        return getAllIslands().stream().filter(i -> i.id().equals(id)).findFirst();
    }

    public Optional<Island> getIslandAt(Location loc) {
        return getAllIslands().stream()
            .filter(i -> i.worldName().equals(loc.getWorld().getName()) && i.isInBounds(loc.getBlockX(), loc.getBlockZ()))
            .findFirst();
    }

    public void claimIsland(String islandId, String clanId) {
        Optional<Island> island = getIslandById(islandId);
        if (island.isPresent()) {
            Island old = island.get();
            Island updated = new Island(
                old.id(), old.displayName(), old.biome(), old.worldName(),
                old.minX(), old.minZ(), old.maxX(), old.maxZ(),
                old.centerX(), old.centerZ(),
                clanId, IslandState.OWNED, 0
            );
            dataStore.updateIsland(updated);
            plugin.getLogger().info("🏝 Insel " + islandId + " von Clan " + clanId + " beansprucht");
        }
    }

    public void startConquest(String islandId) {
        Optional<Island> island = getIslandById(islandId);
        if (island.isPresent()) {
            Island old = island.get();
            Island updated = new Island(
                old.id(), old.displayName(), old.biome(), old.worldName(),
                old.minX(), old.minZ(), old.maxX(), old.maxZ(),
                old.centerX(), old.centerZ(),
                old.ownerClanId(), IslandState.UNDER_ATTACK, System.currentTimeMillis()
            );
            dataStore.updateIsland(updated);
        }
    }
}
