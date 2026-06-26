package de.onemine.model;

public record Island(
    String id,
    String displayName,
    IslandBiome biome,
    String worldName,
    int minX, int minZ, int maxX, int maxZ,
    int centerX, int centerZ,
    String ownerClanId,
    IslandState state,
    long attackStartedAt
) {
    public boolean isInBounds(int x, int z) {
        return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
    }

    public boolean isFree() {
        return ownerClanId == null || ownerClanId.isEmpty();
    }
}
