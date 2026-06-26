package de.onemine.persistence;

import de.onemine.OneMinePlugin;
import de.onemine.model.*;

import java.sql.*;
import java.util.*;

public class SqliteDataStore implements DataStore {
    private final OneMinePlugin plugin;
    private Connection connection;

    public SqliteDataStore(OneMinePlugin plugin) throws SQLException {
        this.plugin = plugin;
        String dbPath = plugin.getDataFolder() + "/onemine.db";
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        createTables();
    }

    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS islands (" +
                "id TEXT PRIMARY KEY, display_name TEXT, biome TEXT, world TEXT, " +
                "min_x INT, min_z INT, max_x INT, max_z INT, center_x INT, center_z INT, " +
                "owner_clan_id TEXT, state TEXT, attack_started_at INTEGER)");
            
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS clans (" +
                "id TEXT PRIMARY KEY, name TEXT UNIQUE, tag TEXT UNIQUE, leader_id TEXT, " +
                "flag_color INT, created_at INTEGER)");
            
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS clan_members (" +
                "clan_id TEXT, player_id TEXT, PRIMARY KEY(clan_id, player_id))");
            
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS players (" +
                "uuid TEXT PRIMARY KEY, name TEXT, berrys INTEGER, global_bounty INTEGER, " +
                "player_bounty INTEGER, clan_id TEXT, active_title TEXT, cosmetic_effect_id INT)");
            
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ships (" +
                "owner_uuid TEXT, entity_uuid TEXT PRIMARY KEY, ship_type TEXT, purchased_at INTEGER)");
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Erstellen von Tabellen: " + e.getMessage());
        }
    }

    @Override
    public List<Island> getAllIslands() {
        List<Island> islands = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM islands")) {
            while (rs.next()) {
                islands.add(new Island(
                    rs.getString("id"),
                    rs.getString("display_name"),
                    IslandBiome.valueOf(rs.getString("biome")),
                    rs.getString("world"),
                    rs.getInt("min_x"), rs.getInt("min_z"), rs.getInt("max_x"), rs.getInt("max_z"),
                    rs.getInt("center_x"), rs.getInt("center_z"),
                    rs.getString("owner_clan_id"),
                    IslandState.valueOf(rs.getString("state")),
                    rs.getLong("attack_started_at")
                ));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Laden von Inseln: " + e.getMessage());
        }
        return islands;
    }

    @Override
    public void updateIsland(Island island) {
        try (PreparedStatement pstmt = connection.prepareStatement(
            "INSERT OR REPLACE INTO islands VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, island.id());
            pstmt.setString(2, island.displayName());
            pstmt.setString(3, island.biome().name());
            pstmt.setString(4, island.worldName());
            pstmt.setInt(5, island.minX());
            pstmt.setInt(6, island.minZ());
            pstmt.setInt(7, island.maxX());
            pstmt.setInt(8, island.maxZ());
            pstmt.setInt(9, island.centerX());
            pstmt.setInt(10, island.centerZ());
            pstmt.setString(11, island.ownerClanId());
            pstmt.setString(12, island.state().name());
            pstmt.setLong(13, island.attackStartedAt());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Updaten von Insel: " + e.getMessage());
        }
    }

    @Override
    public List<Clan> getAllClans() {
        List<Clan> clans = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM clans")) {
            while (rs.next()) {
                Clan clan = new Clan(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("tag"),
                    UUID.fromString(rs.getString("leader_id")),
                    rs.getInt("flag_color")
                );
                clans.add(clan);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Laden von Clans: " + e.getMessage());
        }
        return clans;
    }

    @Override
    public void saveClan(Clan clan) {
        try (PreparedStatement pstmt = connection.prepareStatement(
            "INSERT OR REPLACE INTO clans VALUES(?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, clan.getId());
            pstmt.setString(2, clan.getName());
            pstmt.setString(3, clan.getTag());
            pstmt.setString(4, clan.getLeaderId().toString());
            pstmt.setInt(5, clan.getFlagColor());
            pstmt.setLong(6, clan.getCreatedAt());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Speichern von Clan: " + e.getMessage());
        }
    }

    @Override
    public PlayerData getPlayerData(UUID uuid) {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                PlayerData data = new PlayerData(uuid, rs.getString("name"));
                data.setBerrys(rs.getLong("berrys"));
                data.setGlobalBounty(rs.getLong("global_bounty"));
                data.setPlayerBounty(rs.getLong("player_bounty"));
                data.setClanId(rs.getString("clan_id"));
                data.setActiveTitle(rs.getString("active_title"));
                data.setCosmeticEffectId(rs.getInt("cosmetic_effect_id"));
                return data;
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Laden von PlayerData: " + e.getMessage());
        }
        return new PlayerData(uuid, "Unknown");
    }

    @Override
    public void savePlayerData(PlayerData data) {
        try (PreparedStatement pstmt = connection.prepareStatement(
            "INSERT OR REPLACE INTO players VALUES(?, ?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, data.getUuid().toString());
            pstmt.setString(2, data.getLastName());
            pstmt.setLong(3, data.getBerrys());
            pstmt.setLong(4, data.getGlobalBounty());
            pstmt.setLong(5, data.getPlayerBounty());
            pstmt.setString(6, data.getClanId());
            pstmt.setString(7, data.getActiveTitle());
            pstmt.setInt(8, data.getCosmeticEffectId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Speichern von PlayerData: " + e.getMessage());
        }
    }

    @Override
    public List<PlayerData> getAllPlayerData() {
        List<PlayerData> players = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM players")) {
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                PlayerData data = new PlayerData(uuid, rs.getString("name"));
                data.setBerrys(rs.getLong("berrys"));
                data.setGlobalBounty(rs.getLong("global_bounty"));
                data.setPlayerBounty(rs.getLong("player_bounty"));
                data.setClanId(rs.getString("clan_id"));
                data.setActiveTitle(rs.getString("active_title"));
                data.setCosmeticEffectId(rs.getInt("cosmetic_effect_id"));
                players.add(data);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Laden aller PlayerData: " + e.getMessage());
        }
        return players;
    }

    @Override
    public List<ShipInstance> getAllShips() {
        List<ShipInstance> ships = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM ships")) {
            while (rs.next()) {
                ships.add(new ShipInstance(
                    UUID.fromString(rs.getString("owner_uuid")),
                    ShipType.valueOf(rs.getString("ship_type")),
                    UUID.fromString(rs.getString("entity_uuid")),
                    rs.getLong("purchased_at")
                ));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Laden von Ships: " + e.getMessage());
        }
        return ships;
    }

    @Override
    public void saveShip(ShipInstance ship) {
        try (PreparedStatement pstmt = connection.prepareStatement(
            "INSERT OR REPLACE INTO ships VALUES(?, ?, ?, ?)")) {
            pstmt.setString(1, ship.ownerId().toString());
            pstmt.setString(2, ship.entityUuid().toString());
            pstmt.setString(3, ship.type().name());
            pstmt.setLong(4, ship.purchasedAt());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Speichern von Ship: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Schließen der DB: " + e.getMessage());
        }
    }
}
