package de.onemine.service;

import de.onemine.OneMinePlugin;
import de.onemine.model.BountyEntry;
import de.onemine.model.PlayerData;
import de.onemine.persistence.DataStore;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BountyService {
    private final OneMinePlugin plugin;
    private final DataStore dataStore;
    private final EconomyService economyService;

    public BountyService(OneMinePlugin plugin, DataStore dataStore) {
        this.plugin = plugin;
        this.dataStore = dataStore;
        this.economyService = null; // wird später gesetzt
    }

    public List<BountyEntry> getTopWanted(int size) {
        return dataStore.getAllPlayerData().stream()
            .sorted((a, b) -> Long.compare(b.getTotalBounty(), a.getTotalBounty()))
            .limit(size)
            .map(p -> new BountyEntry(p.getUuid(), p.getLastName(), p.getTotalBounty()))
            .collect(Collectors.toList());
    }

    public void addGlobalBounty(UUID uuid, long amount) {
        PlayerData data = dataStore.getPlayerData(uuid);
        data.addGlobalBounty(amount);
        dataStore.savePlayerData(data);
    }

    public void setBountyOnPlayer(UUID setter, UUID target, long amount) {
        PlayerData data = dataStore.getPlayerData(target);
        data.addPlayerBounty(amount);
        dataStore.savePlayerData(data);
        plugin.getLogger().info("💀 " + amount + " Berrys Kopfgeld auf " + target + " gesetzt");
    }
}
