package com.newcombat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class NewCombat extends JavaPlugin implements Listener {

    private final HashMap<UUID, Long> dashCooldown = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("NewCombat enabled!");
    }

    // Custom hitbox
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Location loc = event.getDamager().getLocation();
            Location hitLoc = event.getEntity().getLocation().add(0, 1.6, 0); // head height
            if (loc.getY() > hitLoc.getY()) {
                event.setDamage(event.getDamage() * 1.5); // headshot
            }
        }
    }

    // Dash (Shift + Right Click)
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (p.isSneaking() && event.getAction().toString().contains("RIGHT")) {
            if (!dashCooldown.containsKey(p.getUniqueId()) ||
                    System.currentTimeMillis() - dashCooldown.get(p.getUniqueId()) > 3000) {

                dashCooldown.put(p.getUniqueId(), System.currentTimeMillis());
                Location dashLoc = p.getLocation().add(p.getLocation().getDirection().multiply(2));
                p.teleport(dashLoc);
                p.sendMessage("§bYou dashed!");
            } else {
                p.sendMessage("§cDash masih cooldown!");
            }
        }
    }

    // Slide (run + shift)
    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        Player p = event.getPlayer();
        if (p.isSprinting() && event.isSneaking()) {
            p.setVelocity(p.getLocation().getDirection().multiply(1.5).setY(-0.3));
            p.sendMessage("§aYou slide!");
        }
    }

    // Double Jump
    @EventHandler
    public void onJump(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (p.isOnGround()) {
            p.setAllowFlight(true);
        } else if (p.getAllowFlight() && p.getGameMode().toString().equals("SURVIVAL")) {
            p.setAllowFlight(false);
            p.setVelocity(p.getLocation().getDirection().multiply(0.5).setY(1));
            p.sendMessage("§eDouble Jump!");
        }
    }

    // Parry
    @EventHandler
    public void onParry(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player p && p.isBlocking()) {
            if (Math.random() < 0.3) {
                event.setCancelled(true);
                p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1, 1);
                p.sendMessage("§6Parry berhasil!");
            }
        }
    }
  }
