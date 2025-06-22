package com.newcombat;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class ReturnByDeath implements Listener {

    private final JavaPlugin plugin;
    private final HashMap<UUID, PlayerSnapshot> snapshots = new HashMap<>();
    private final Random random = new Random();

    public ReturnByDeath(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        startSnapshotTask();
    }

    private void startSnapshotTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                snapshots.put(player.getUniqueId(), new PlayerSnapshot(player));
            }
        }, 0L, 2400L);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();

        if (!snapshots.containsKey(uuid)) return;
        if (random.nextDouble() > 0.65) return;

        PlayerSnapshot snapshot = snapshots.get(uuid);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.spigot().respawn();
            snapshot.restore(player);
            player.setNoDamageTicks(40);
            player.getWorld().playSound(player.getLocation(), "custom.return_by_death", 1f, 1f);
            player.getWorld().spawnParticle(Particle.SOUL, player.getLocation(), 30, 0.8, 0.5, 0.8);
            player.getWorld().spawnParticle(Particle.ASH, player.getLocation(), 40, 0.5, 0.3, 0.5);
            player.sendMessage("§5[Return by Death] §fKamu kembali dari kematian.");
        }, 2L);

        event.getDrops().clear();
        event.setKeepInventory(true);
        event.setKeepLevel(true);
    }
}
