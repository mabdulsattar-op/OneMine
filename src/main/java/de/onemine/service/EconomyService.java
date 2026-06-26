package de.onemine.service;

import de.onemine.OneMinePlugin;
import de.onemine.model.PlayerData;
import de.onemine.persistence.DataStore;

import java.util.UUID;

public class EconomyService {
    private final OneMinePlugin plugin;
    private final DataStore dataStore;

    public EconomyService(OneMinePlugin plugin, DataStore dataStore) {
        this.plugin = plugin;
        this.dataStore = dataStore;
    }

    public long getBalance(UUID uuid) {
        return dataStore.getPlayerData(uuid).getBerrys();
    }

    public void setBalance(UUID uuid, long amount) {
        PlayerData data = dataStore.getPlayerData(uuid);
        data.setBerrys(amount);
        dataStore.savePlayerData(data);
    }

    public boolean withdraw(UUID uuid, long amount) {
        PlayerData data = dataStore.getPlayerData(uuid);
        if (data.getBerrys() >= amount) {
            data.removeBerrys(amount);
            dataStore.savePlayerData(data);
            return true;
        }
        return false;
    }

    public void deposit(UUID uuid, long amount) {
        PlayerData data = dataStore.getPlayerData(uuid);
        data.addBerrys(amount);
        dataStore.savePlayerData(data);
    }

    public boolean hasEnough(UUID uuid, long amount) {
        return getBalance(uuid) >= amount;
    }
}
