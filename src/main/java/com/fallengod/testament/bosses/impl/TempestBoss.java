package com.fallengod.testament.bosses.impl;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Stray;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TempestBoss extends GodBoss {

    public TempestBoss(TestamentPlugin plugin) {
        super(plugin, BossType.TEMPEST_DRAGON, GodType.TEMPEST);
    }

    @Override
    protected void setupBoss() {
        if (entity instanceof Stray stray) {
            if (entity.getAttribute(Attribute.MAX_HEALTH) != null) {
                entity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(bossType.getMaxHealth());
                entity.setHealth(bossType.getMaxHealth());
            }
            if (entity.getAttribute(Attribute.ATTACK_DAMAGE) != null) {
                entity.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(bossType.getDamage());
            }
            if (entity.getAttribute(Attribute.MOVEMENT_SPEED) != null) {
                entity.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.3);
            }

            stray.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            stray.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
            stray.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
            stray.setCustomName("§e§lStorm Sovereign");
            stray.setCustomNameVisible(true);
        }
    }

    @Override
    protected void useSpecialAbility() {
        switch (phase) {
            case 1 -> lightningStorm();
            case 2 -> windShear();
            case 3 -> tornado();
        }
    }

    private void lightningStorm() {
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 40) {
                for (int i = 0; i < 5; i++) {
                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        Location strikeLoc = player.getLocation().add(
                            (Math.random() - 0.5) * 20,
                            0,
                            (Math.random() - 0.5) * 20
                        );

                        strikeLoc.getWorld().strikeLightning(strikeLoc);

                        for (Player nearbyPlayer : entity.getWorld().getPlayers()) {
                            if (nearbyPlayer.getLocation().distance(strikeLoc) <= 5) {
                                nearbyPlayer.damage(15.0);
                                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0));
                                nearbyPlayer.sendMessage("§e§lLightning Storm! §7Thunder and lightning rain down!");
                            }
                        }
                    }, i * 20L);
                }
            }
        }

        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 3.0f, 1.0f);
    }

    private void windShear() {
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 25) {
                Vector knockback = player.getLocation().subtract(entity.getLocation()).toVector().normalize();
                knockback.multiply(3.0);
                knockback.setY(1.5);

                player.setVelocity(knockback);
                player.damage(10.0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 1));
                player.sendMessage("§e§lWind Shear! §7Powerful winds hurl you through the air!");

                player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 20, 2, 2, 2, 0.2);
                player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, player.getLocation(), 10, 1, 1, 1, 0.1);
            }
        }

        entity.getWorld().playSound(entity.getLocation(), Sound.ITEM_ELYTRA_FLYING, 2.0f, 0.5f);
    }

    private void tornado() {
        Location center = entity.getLocation();

        for (int duration = 0; duration < 200; duration++) {
            final int tick = duration;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                double angle = tick * 0.1;
                Location tornadoCenter = center.clone().add(
                    Math.cos(angle) * 10,
                    0,
                    Math.sin(angle) * 10
                );

                for (int height = 0; height <= 15; height++) {
                    for (double spiralAngle = 0; spiralAngle < 360; spiralAngle += 30) {
                        double radius = 3 - (height * 0.15);
                        double x = tornadoCenter.getX() + radius * Math.cos(Math.toRadians(spiralAngle + tick * 10));
                        double z = tornadoCenter.getZ() + radius * Math.sin(Math.toRadians(spiralAngle + tick * 10));

                        Location particleLoc = new Location(tornadoCenter.getWorld(), x, tornadoCenter.getY() + height, z);
                        tornadoCenter.getWorld().spawnParticle(Particle.CLOUD, particleLoc, 1);
                    }
                }

                for (Player player : entity.getWorld().getPlayers()) {
                    if (player.getLocation().distance(tornadoCenter) <= 8) {
                        Vector pullVector = tornadoCenter.subtract(player.getLocation()).toVector().normalize().multiply(0.5);
                        pullVector.setY(0.3);
                        player.setVelocity(pullVector);

                        if (player.getLocation().distance(tornadoCenter) <= 3) {
                            player.damage(5.0);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 100, 1));
                        }
                    }
                }

                if (tick % 20 == 0) {
                    tornadoCenter.getWorld().playSound(tornadoCenter, Sound.ENTITY_PHANTOM_FLAP, 1.0f, 0.5f);
                }
            }, duration);
        }

        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(center) <= 50) {
                player.sendMessage("§e§lTornado! §7A devastating vortex tears across the battlefield!");
            }
        }
    }

    @Override
    protected void enterPhase2() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));

        if (entity.getAttribute(Attribute.MOVEMENT_SPEED) != null) {
            entity.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.4);
        }

        entity.getWorld().spawnParticle(Particle.CLOUD, entity.getLocation(), 100, 5, 5, 5, 0.2);
        entity.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, entity.getLocation(), 50, 3, 3, 3, 0.1);
    }

    @Override
    protected void enterPhase3() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));

        if (entity.getAttribute(Attribute.MOVEMENT_SPEED) != null) {
            entity.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.5);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isAlive()) {
                    cancel();
                    return;
                }

                if (Math.random() < 0.3) {
                    for (Player player : entity.getWorld().getPlayers()) {
                        if (player.getLocation().distance(entity.getLocation()) <= 30) {
                            Location strikeLoc = player.getLocation().add(
                                (Math.random() - 0.5) * 10,
                                0,
                                (Math.random() - 0.5) * 10
                            );
                            strikeLoc.getWorld().strikeLightning(strikeLoc);
                            break;
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 40L);

        entity.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, entity.getLocation(), 200, 8, 8, 8, 0.3);
    }

    @Override
    protected void onDeath() {
        Location loc = entity.getLocation();

        entity.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 200, 15, 15, 15, 0.5);
        entity.getWorld().spawnParticle(Particle.CLOUD, loc, 100, 10, 10, 10, 0.3);
        entity.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 3.0f, 0.5f);

        for (int i = 0; i < 10; i++) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                Location strikeLoc = loc.clone().add(
                    (Math.random() - 0.5) * 30,
                    0,
                    (Math.random() - 0.5) * 30
                );
                strikeLoc.getWorld().strikeLightning(strikeLoc);
            }, i * 5L);
        }

        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.LIGHTNING_ROD, 5));
        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.ELYTRA, 1));
        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.NETHER_STAR, 3));

        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 200) {
                player.sendTitle("§e§lSTORM SOVEREIGN DEFEATED", "§7The tempest has been calmed", 20, 80, 20);
            }
        }
    }
}