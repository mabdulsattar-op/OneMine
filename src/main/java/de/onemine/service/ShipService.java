package de.onemine.service;

import de.onemine.OneMinePlugin;
import de.onemine.model.ShipInstance;
import de.onemine.model.ShipType;
import de.onemine.persistence.DataStore;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShipService {
    private final OneMinePlugin plugin;
    private final DataStore dataStore;

    public ShipService(OneMinePlugin plugin, DataStore dataStore) {
        this.plugin = plugin;
        this.dataStore = dataStore;
    }

    public List<ShipInstance> getPlayerShips(UUID uuid) {
        return dataStore.getAllShips().stream()
            .filter(s -> s.ownerId().equals(uuid))
            .collect(Collectors.toList());
    }

    public void buyShip(UUID uuid, ShipType type) {
        ShipInstance ship = new ShipInstance(uuid, type, UUID.randomUUID(), System.currentTimeMillis());
        dataStore.saveShip(ship);
        plugin.getLogger().info("⛵ " + uuid + " kauft " + type.getDisplayName());
    }
}
