package de.onemine.persistence;

import de.onemine.model.*;

import java.util.List;
import java.util.UUID;

public interface DataStore {
    // Islands
    List<Island> getAllIslands();
    void updateIsland(Island island);

    // Clans
    List<Clan> getAllClans();
    void saveClan(Clan clan);

    // Players
    PlayerData getPlayerData(UUID uuid);
    void savePlayerData(PlayerData data);
    List<PlayerData> getAllPlayerData();

    // Ships
    List<ShipInstance> getAllShips();
    void saveShip(ShipInstance ship);

    // Lifecycle
    void close();
}
