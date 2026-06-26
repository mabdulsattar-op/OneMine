package de.onemine.service;

import de.onemine.OneMinePlugin;
import de.onemine.model.Island;
import de.onemine.persistence.DataStore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Leather;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;
import java.util.UUID;

public class MarineService {
    private final OneMinePlugin plugin;
    private final DataStore dataStore;
    private BukkitTask spawnTask;
    private Random random = new Random();
    private int marineCount = 0;

    public MarineService(OneMinePlugin plugin, DataStore dataStore) {
        this.plugin = plugin;
        this.dataStore = dataStore;
    }

    public void startSpawnTask() {
        int interval = plugin.getPluginConfig().getMarineSpawnIntervalTicks();
        spawnTask = Bukkit.getScheduler().runTaskTimer(plugin, this::spawnMarines, interval, interval);
        plugin.getLogger().info("⚔️ Marine Spawn Task gestartet (Intervall: " + interval + " Ticks)");
    }

    private void spawnMarines() {
        int maxGlobal = plugin.getPluginConfig().getMaxGlobalMarines();
        if (marineCount >= maxGlobal) return;

        for (Island island : plugin.getIslandService().getAllIslands()) {
            if (marineCount >= maxGlobal) break;
            
            int maxPerIsland = plugin.getPluginConfig().getMaxMarinesPerIsland();
            int toSpawn = random.nextInt(maxPerIsland) + 1;
            
            for (int i = 0; i < toSpawn && marineCount < maxGlobal; i++) {
                spawnMarineOnIsland(island);
            }
        }
    }

    private void spawnMarineOnIsland(Island island) {
        Location spawnLoc = new Location(
            Bukkit.getWorld(island.worldName()),
            island.centerX(), 65, island.centerZ()
        );

        if (spawnLoc.getWorld() == null) return;

        Zombie zombie = (Zombie) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
        zombie.setCustomName("🔴 Marine Offizier");
        zombie.setCustomNameVisible(true);
        zombie.setMaxHealth(20);
        zombie.setHealth(20);
        zombie.setTag("marine", "true");

        // Armor
        zombie.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        zombie.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        zombie.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        zombie.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));

        marineCount++;
    }

    public void removeMarineCount() {
        marineCount = Math.max(0, marineCount - 1);
    }

    public int getMarineCount() {
        return marineCount;
    }
}
