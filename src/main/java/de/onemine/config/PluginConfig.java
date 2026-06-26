package de.onemine.config;

import de.onemine.OneMinePlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class PluginConfig {
    private final OneMinePlugin plugin;
    private FileConfiguration config;

    public PluginConfig(OneMinePlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    // Economy
    public long getStartBerrys() {
        return config.getLong("economy.start-berrys", 100);
    }

    public long getClanCreateCost() {
        return config.getLong("economy.clan-create-cost", 5000);
    }

    public long getPvpKillReward() {
        return config.getLong("economy.pvp-kill-reward", 100);
    }

    public long getMarineKillMin() {
        return config.getLong("economy.marine-kill-min", 50);
    }

    public long getMarineKillMax() {
        return config.getLong("economy.marine-kill-max", 200);
    }

    // Bounty
    public long getPvpIncrement() {
        return config.getLong("bounty.pvp-increase", 100);
    }

    public long getMarineIncrement() {
        return config.getLong("bounty.marine-increase", 25);
    }

    public int getTopListSize() {
        return config.getInt("bounty.top-list-size", 10);
    }

    // Islands
    public boolean isProtectionEnabled() {
        return config.getBoolean("islands.protection-enabled", true);
    }

    public int getConquestDurationSeconds() {
        return config.getInt("islands.conquest-duration-seconds", 120);
    }

    public int getConquestMinAttackers() {
        return config.getInt("islands.conquest-min-attackers", 2);
    }

    // Marines
    public int getMarineSpawnIntervalTicks() {
        return config.getInt("marines.spawn-interval-ticks", 600);
    }

    public int getMaxMarinesPerIsland() {
        return config.getInt("marines.max-per-island", 5);
    }

    public int getMaxGlobalMarines() {
        return config.getInt("marines.max-global", 50);
    }

    public double getOfficerChance() {
        return config.getDouble("marines.officer-chance", 0.05);
    }

    // Ships
    public long getSmallBoatCost() {
        return config.getLong("ships.small-boat-cost", 500);
    }

    public long getMediumShipCost() {
        return config.getLong("ships.medium-ship-cost", 2000);
    }

    public long getClanGalleonCost() {
        return config.getLong("ships.clan-galleon-cost", 10000);
    }
}
