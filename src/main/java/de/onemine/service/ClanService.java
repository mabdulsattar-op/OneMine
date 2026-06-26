package de.onemine.service;

import de.onemine.OneMinePlugin;
import de.onemine.model.Clan;
import de.onemine.persistence.DataStore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClanService {
    private final OneMinePlugin plugin;
    private final DataStore dataStore;

    public ClanService(OneMinePlugin plugin, DataStore dataStore) {
        this.plugin = plugin;
        this.dataStore = dataStore;
    }

    public List<Clan> getAllClans() {
        return dataStore.getAllClans();
    }

    public Optional<Clan> getClanById(String id) {
        return getAllClans().stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public Optional<Clan> getClanByName(String name) {
        return getAllClans().stream().filter(c -> c.getName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<Clan> getClanByMember(UUID uuid) {
        return getAllClans().stream().filter(c -> c.isMember(uuid)).findFirst();
    }

    public void createClan(String id, String name, String tag, UUID leaderId, int flagColor) {
        Clan clan = new Clan(id, name, tag, leaderId, flagColor);
        dataStore.saveClan(clan);
        plugin.getLogger().info("🏴 Clan " + name + " gegründet von " + leaderId);
    }

    public void addMember(String clanId, UUID uuid) {
        Optional<Clan> clan = getClanById(clanId);
        clan.ifPresent(c -> {
            c.addMember(uuid);
            dataStore.saveClan(c);
        });
    }

    public void removeMember(String clanId, UUID uuid) {
        Optional<Clan> clan = getClanById(clanId);
        clan.ifPresent(c -> {
            c.removeMember(uuid);
            dataStore.saveClan(c);
        });
    }
}
