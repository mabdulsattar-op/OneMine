package de.onemine.service;

import de.onemine.OneMinePlugin;
import de.onemine.persistence.DataStore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConquestService {
    private final OneMinePlugin plugin;
    private final DataStore dataStore;
    private Map<String, Long> conquestTimers = new HashMap<>();

    public ConquestService(OneMinePlugin plugin, DataStore dataStore) {
        this.plugin = plugin;
        this.dataStore = dataStore;
    }

    public boolean isIslandUnderConquest(String islandId) {
        Long start = conquestTimers.get(islandId);
        if (start == null) return false;
        
        long elapsed = System.currentTimeMillis() - start;
        long duration = plugin.getPluginConfig().getConquestDurationSeconds() * 1000L;
        
        return elapsed < duration;
    }

    public void startConquest(String islandId) {
        conquestTimers.put(islandId, System.currentTimeMillis());
        plugin.getIslandService().startConquest(islandId);
    }
}
