package com.newcombat;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class PlayerSnapshot {
    private final Location location;
    private final double health;
    private final int food;

    public PlayerSnapshot(Player player) {
        this.location = player.getLocation().clone();
        this.health = player.getHealth();
        this.food = player.getFoodLevel();
    }

    public void restore(Player player) {
        player.teleport(location);
        player.setHealth(Math.min(health, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        player.setFoodLevel(food);
    }
}
