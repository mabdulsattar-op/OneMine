package de.onemine;

import de.onemine.command.MenuCommand;
import de.onemine.command.CommandsCommand;
import de.onemine.command.BerrysCommand;
import de.onemine.config.PluginConfig;
import de.onemine.listener.CombatListener;
import de.onemine.listener.IslandProtectionListener;
import de.onemine.listener.MarineListener;
import de.onemine.listener.MenuListener;
import de.onemine.persistence.DataStore;
import de.onemine.persistence.SqliteDataStore;
import de.onemine.service.*;
import org.bukkit.plugin.java.JavaPlugin;

public class OneMinePlugin extends JavaPlugin {
    private static OneMinePlugin instance;
    private PluginConfig config;
    private DataStore dataStore;
    
    // Services
    private IslandService islandService;
    private ClanService clanService;
    private BountyService bountyService;
    private EconomyService economyService;
    private MarineService marineService;
    private ShipService shipService;
    private ConquestService conquestService;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("OneMine Plugin wird geladen...");
        
        // Config laden
        saveDefaultConfig();
        config = new PluginConfig(this);
        
        // Datenbank initialisieren
        try {
            dataStore = new SqliteDataStore(this);
            getLogger().info("✓ SQLite Datenbank verbunden");
        } catch (Exception e) {
            getLogger().severe("✗ Datenbank-Fehler: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        // Services initialisieren
        islandService = new IslandService(this, dataStore);
        clanService = new ClanService(this, dataStore);
        bountyService = new BountyService(this, dataStore);
        economyService = new EconomyService(this, dataStore);
        marineService = new MarineService(this, dataStore);
        shipService = new ShipService(this, dataStore);
        conquestService = new ConquestService(this, dataStore);
        
        getLogger().info("✓ Services initialisiert");
        
        // Commands registrieren
        getCommand("menu").setExecutor(new MenuCommand(this));
        getCommand("menü").setExecutor(new MenuCommand(this));
        getCommand("commands").setExecutor(new CommandsCommand(this));
        getCommand("berrys").setExecutor(new BerrysCommand(this));
        
        // Listener registrieren
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new IslandProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new MarineListener(this), this);
        
        getLogger().info("✓ Listener registriert");
        
        // Marine Spawn Task starten
        marineService.startSpawnTask();
        
        getLogger().info("🏴 OneMine Plugin erfolgreich geladen!");
    }

    @Override
    public void onDisable() {
        if (dataStore != null) {
            dataStore.close();
        }
        getLogger().info("OneMine Plugin deaktiviert.");
    }

    public static OneMinePlugin getInstance() {
        return instance;
    }

    public PluginConfig getPluginConfig() {
        return config;
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public IslandService getIslandService() {
        return islandService;
    }

    public ClanService getClanService() {
        return clanService;
    }

    public BountyService getBountyService() {
        return bountyService;
    }

    public EconomyService getEconomyService() {
        return economyService;
    }

    public MarineService getMarineService() {
        return marineService;
    }

    public ShipService getShipService() {
        return shipService;
    }

    public ConquestService getConquestService() {
        return conquestService;
    }
}
