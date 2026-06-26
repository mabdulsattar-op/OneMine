package de.onemine.model;

import org.bukkit.Material;

public enum ShipType {
    SMALL_BOAT(500, 1, Material.OAK_BOAT, "Kleines Boot"),
    MEDIUM_SHIP(2000, 4, Material.SPRUCE_BOAT, "Mittleres Schiff"),
    CLAN_GALLEON(10000, 8, Material.DARK_OAK_BOAT, "Clan-Galeone");

    private final long cost;
    private final int maxPassengers;
    private final Material material;
    private final String displayName;

    ShipType(long cost, int maxPassengers, Material material, String displayName) {
        this.cost = cost;
        this.maxPassengers = maxPassengers;
        this.material = material;
        this.displayName = displayName;
    }

    public long getCost() { return cost; }
    public int getMaxPassengers() { return maxPassengers; }
    public Material getMaterial() { return material; }
    public String getDisplayName() { return displayName; }
}
