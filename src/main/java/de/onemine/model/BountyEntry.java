package de.onemine.model;

import java.util.UUID;

public record BountyEntry(
    UUID uuid,
    String name,
    long totalBounty
) {}
