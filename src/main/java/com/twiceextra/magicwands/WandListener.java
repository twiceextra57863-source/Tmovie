package com.twiceextra.magicwands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class WandListener implements Listener {

    private final WandManager manager;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public WandListener(WandManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null || !event.getItem().hasItemMeta()) return;

        String type = event.getItem().getItemMeta().getPersistentDataContainer().get(manager.WAND_KEY, PersistentDataType.STRING);
        if (type == null) return;

        Player player = event.getPlayer();
        if (isOnCooldown(player)) {
            player.sendActionBar(Component.text("Wand is recharging...", NamedTextColor.RED));
            return;
        }

        switch (type) {
            case "phoenix" -> castPhoenix(player);
            case "void" -> castVoid(player);
            case "mjolnir" -> castMjolnir(player);
        }
        
        setCooldown(player, 3000); // 3-second global cooldown
    }

    private void castPhoenix(Player player) {
        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setYield(2.0f);
        fireball.setIsIncendiary(true);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.FLAME, player.getEyeLocation(), 20, 0.5, 0.5, 0.5, 0.1);
    }

    private void castVoid(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 1.5f);
        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, player.getEyeLocation(), 50, 2, 2, 2, 0.2);
        
        for (Entity entity : player.getNearbyEntities(7, 7, 7)) {
            if (entity instanceof LivingEntity && entity != player) {
                Vector push = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(2.0).setY(1.0);
                entity.setVelocity(push);
            }
        }
    }

    private void castMjolnir(Player player) {
        Entity target = getTargetEntity(player, 30);
        Location strikeLoc = (target != null) ? target.getLocation() : player.getTargetBlock(null, 30).getLocation();
        
        player.getWorld().strikeLightning(strikeLoc);
        player.getWorld().spawnParticle(Particle.FLASH, strikeLoc, 5);
        player.getWorld().playSound(strikeLoc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2.0f, 0.5f);
    }

    private Entity getTargetEntity(Player player, int range) {
        return player.getNearbyEntities(range, range, range).stream()
                .filter(e -> e instanceof LivingEntity && e != player)
                .findFirst().orElse(null);
    }

    private void setCooldown(Player player, long ms) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + ms);
    }

    private boolean isOnCooldown(Player player) {
        return cooldowns.containsKey(player.getUniqueId()) && cooldowns.get(player.getUniqueId()) > System.currentTimeMillis();
    }
}