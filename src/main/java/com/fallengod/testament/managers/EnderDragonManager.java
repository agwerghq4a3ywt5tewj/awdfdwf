/**
 * EnderDragonManager.java
 * 
 * A custom boss manager for enhancing the Ender Dragon fight.
 * Transforms the vanilla Ender Dragon into a challenging, multi-phase raid encounter.
 * Inspired by Soulsborne/Sekiro boss design, this system introduces minions, hazards,
 * phases, a resurrection mechanic, and powerful debuffs. 
 * 
 * Core Features:
 * - Multi-phase dragon fight with conditional spawning behavior.
 * - Healing phase blocked by destructible anchors.
 * - Resurrection into a second, harder form after death.
 * - Debuffs, lightning, fireballs, vortexes, and beam attacks.
 * - Custom Wither minions called "Void Heralds" with AI.
 * - Boss name and attributes dynamically modified.
 * - Compatible with automatic summoning or vanilla dragon events.
 * - Includes Soul of the Ender drop after final death.
 * - Dragon resists high-damage mace hits and targets mace users.
 */

package com.fallengod.testament.bosses;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EnderDragonManager implements Listener {

    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(EnderDragonManager.class);

    private static final int MAX_HEALTH = 600;
    private static final int RESURRECTED_MAX_HEALTH = 1200;

    private static final Random random = new Random();

    private static boolean healPhaseTriggered = false;
    private static boolean resurrectionTriggered = false;
    private static boolean voidChainsBroken = false;

    private static final Map<UUID, BukkitRunnable> fireballTasks = new ConcurrentHashMap<>();
    private static final Map<UUID, List<Entity>> healingAnchors = new ConcurrentHashMap<>();

    // === Initialize or buff the dragon with custom stats and start monitoring ===
    public static void buffDragon(EnderDragon dragon) {
        dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(MAX_HEALTH);
        dragon.setHealth(MAX_HEALTH);
        dragon.setCustomName("§5Fallen Aspect of the End");
        dragon.setCustomNameVisible(true);
        startFightMonitor(dragon);
        startFireballBarrage(dragon);
        healingAnchors.put(dragon.getUniqueId(), new ArrayList<>());
    }

    // Scan the world for vanilla dragons and convert them to custom dragons
    public static void ensureCustomDragon(World world) {
        List<EnderDragon> dragons = world.getEntitiesByClass(EnderDragon.class);
        for (EnderDragon dragon : dragons) {
            if (!"§5Fallen Aspect of the End".equals(dragon.getCustomName()) && !resurrectionTriggered) {
                buffDragon(dragon);
            }
        }
    }

    // Cap damage taken to max 18 damage per hit
    public static void handleDamageResistance(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof EnderDragon dragon)) return;
        String name = dragon.getCustomName();
        if ("§5Fallen Aspect of the End".equals(name) || "§5Resurrected Ender Dragon".equals(name)) {
            double maxAllowed = 18.0;
            if (event.getDamage() > maxAllowed) {
                event.setDamage(maxAllowed);
                Bukkit.broadcastMessage("§5[Dragon] §7The void shield dampens the force of the blow!");
            }
        }
    }

    // Dragon aggressively targets players holding Maces
    public static void handleMaceTargeting(LivingEntity boss) {
        List<Player> maceUsers = boss.getWorld().getPlayers().stream()
                .filter(p -> Arrays.stream(p.getInventory().getContents())
                        .filter(Objects::nonNull)
                        .anyMatch(item -> item.getType().name().contains("MACE")))
                .toList();

        if (!maceUsers.isEmpty() && boss instanceof Mob mob) {
            Player target = maceUsers.get(random.nextInt(maceUsers.size()));
            mob.setTarget(target);
        }
    }

    // === Main boss fight loop ===
    private static void startFightMonitor(EnderDragon dragon) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!dragon.isValid() || dragon.isDead()) {
                    cancel();
                    fireballTasks.values().forEach(BukkitRunnable::cancel);
                    fireballTasks.clear();
                    return;
                }

                handleMaceTargeting(dragon);
                double health = dragon.getHealth();
                double maxHealth = dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

                applyAreaHazards(dragon);
                applyDebuffs(dragon);

                if (health < maxHealth * 0.85 && health > maxHealth * 0.6) {
                    spawnVoidEchoes(dragon.getLocation());
                } else if (health < maxHealth * 0.6 && health > maxHealth * 0.3) {
                    spawnPhantoms(dragon.getLocation());
                    createFireballVortex(dragon);
                } else if (health <= maxHealth * 0.3 && !healPhaseTriggered) {
                    healPhaseTriggered = true;
                    startHealPhase(dragon);
                }

                if (health <= maxHealth * 0.15 && !voidChainsBroken) {
                    voidChainsBroken = true;
                    triggerVoidChains(dragon);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Run every second
    }

    // === Area hazards - lightning storms and void explosions ===
    private static void applyAreaHazards(EnderDragon dragon) {
        World world = dragon.getWorld();
        Location loc = dragon.getLocation();

        // Random lightning storm with particles
        if (random.nextDouble() < 0.15) {
            Location strikeLoc = loc.clone().add(random.nextInt(12) - 6, 0, random.nextInt(12) - 6);
            world.strikeLightningEffect(strikeLoc);
            world.spawnParticle(Particle.PORTAL, strikeLoc, 50, 1, 2, 1, 0.5);
            world.playSound(strikeLoc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2f, 0.8f);
        }

        // Random void explosions that don't destroy blocks but hurt players
        if (random.nextDouble() < 0.1) {
            Location explosionLoc = loc.clone().add(random.nextInt(15) - 7, 0, random.nextInt(15) - 7);
            world.spawnParticle(Particle.SMOKE_LARGE, explosionLoc, 40, 1, 1, 1, 0.05);
            world.playSound(explosionLoc, Sound.ENTITY_GENERIC_EXPLODE, 3f, 0.5f);

            // Damage players close to explosion
            for (Player player : world.getPlayers()) {
                if (player.getLocation().distance(explosionLoc) <= 4) {
                    player.damage(8, dragon);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
                }
            }
        }
    }

    // === Apply debuffs like blindness, slowness and confusion to nearby players ===
    private static void applyDebuffs(EnderDragon dragon) {
        World world = dragon.getWorld();
        Location loc = dragon.getLocation();

        for (Player player : world.getPlayers()) {
            if (player.getLocation().distance(loc) <= 20) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 1));
            }
        }
    }

    // === Spawn Wither minions "Void Heralds" with custom AI ===
    private static void spawnVoidEchoes(Location loc) {
        World world = loc.getWorld();
        for (int i = 0; i < 3; i++) {
            Location spawnLoc = loc.clone().add(random.nextInt(8) - 4, 0, random.nextInt(8) - 4);
            Wither wither = world.spawn(spawnLoc, Wither.class);
            wither.setCustomName("§8Void Herald");
            wither.setCustomNameVisible(true);
            wither.setHealth(80);
            wither.setAI(true);
            wither.setInvulnerable(false);
            wither.setMetadata("VoidHerald", new FixedMetadataValue(plugin, true));

            // Target random player and shoot fireballs occasionally
            BukkitRunnable ai = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!wither.isValid() || wither.isDead()) {
                        cancel();
                        return;
                    }
                    List<Player> players = wither.getWorld().getPlayers();
                    if (!players.isEmpty()) {
                        Player target = players.get(random.nextInt(players.size()));
                        wither.setTarget(target);
                        // 20% chance to shoot Wither Skull fireball at player
                        if (random.nextDouble() < 0.2) {
                            WitherSkull skull = wither.launchProjectile(WitherSkull.class);
                            skull.setShooter(wither);
                        }
                    }
                }
            };
            ai.runTaskTimer(plugin, 0L, 40L);
        }
    }

    // === Spawn Phantom minions that swoop in for attacks ===
    private static void spawnPhantoms(Location loc) {
        World world = loc.getWorld();
        for (int i = 0; i < 4; i++) {
            Location spawnLoc = loc.clone().add(random.nextInt(10) - 5, 15, random.nextInt(10) - 5);
            Phantom phantom = world.spawn(spawnLoc, Phantom.class);
            phantom.setCustomName("§dPhantom Shade");
            phantom.setCustomNameVisible(true);
            phantom.setHealth(30);
            phantom.setAI(true);

            BukkitRunnable ai = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!phantom.isValid() || phantom.isDead()) {
                        cancel();
                        return;
                    }
                    List<Player> players = phantom.getWorld().getPlayers();
                    if (!players.isEmpty()) {
                        Player target = players.get(random.nextInt(players.size()));
                        phantom.setTarget(target);
                    }
                }
            };
            ai.runTaskTimer(plugin, 0L, 30L);
        }
    }

    // === Create a fireball vortex attack around the dragon ===
    private static void createFireballVortex(EnderDragon dragon) {
        BukkitRunnable vortexTask = new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (!dragon.isValid() || dragon.isDead() || ticks > 200) {
                    cancel();
                    fireballTasks.remove(dragon.getUniqueId());
                    return;
                }

                Location center = dragon.getLocation();
                double radius = 4.0;
                double angle = ticks * Math.PI / 10;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);

                Location fireballLoc = center.clone().add(x, 2, z);
                Fireball fb = dragon.getWorld().spawn(fireballLoc, Fireball.class);
                fb.setShooter(dragon);
                fb.setYield(2);
                fb.setIsIncendiary(false);
                fb.setVelocity(center.toVector().subtract(fireballLoc.toVector()).normalize().multiply(0.6));
                ticks++;
            }
        };
        vortexTask.runTaskTimer(plugin, 0L, 5L);
        fireballTasks.put(dragon.getUniqueId(), vortexTask);
    }

    // === Start the healing phase where dragon becomes stationary and heals ===
    private static void startHealPhase(EnderDragon dragon) {
        dragon.setAI(false);
        dragon.setInvulnerable(true);
        dragon.setGlowing(true);
        dragon.getWorld().playSound(dragon.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 2f, 1f);
        dragon.getWorld().spawnParticle(Particle.HEART, dragon.getLocation(), 100, 2, 2, 2, 0.5);

        // Spawn healing anchors players must destroy
        List<Entity> anchors = healingAnchors.get(dragon.getUniqueId());
        for (int i = 0; i < 4; i++) {
            Location anchorLoc = dragon.getLocation().clone().add(5 * Math.cos(i * Math.PI / 2), 2, 5 * Math.sin(i * Math.PI / 2));
            ArmorStand anchor = dragon.getWorld().spawn(anchorLoc, ArmorStand.class, stand -> {
                stand.setInvisible(true);
                stand.setInvulnerable(true);
                stand.setMarker(true);
                stand.setCustomName("§aHealing Anchor");
                stand.setCustomNameVisible(true);
                stand.setGravity(false);
                stand.setPersistent(true);
            });
            anchors.add(anchor);
        }

        // Start healing ticks while anchors alive
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!dragon.isValid() || dragon.isDead()) {
                    cancel();
                    return;
                }

                if (anchors.stream().anyMatch(Entity::isDead)) {
                    // At least one anchor destroyed, cancel heal phase
                    cancelHealPhase(dragon);
                    cancel();
                    return;
                }

                double health = dragon.getHealth();
                double maxHealth = dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

                if (health >= maxHealth) {
                    // Fully healed
                    cancelHealPhase(dragon);
                    cancel();
                    return;
                }

                // Heal a bit every tick
                dragon.setHealth(Math.min(health + 3, maxHealth));
                dragon.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, dragon.getLocation(), 20, 1, 2, 1, 0.2);
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    // === Cancel the healing phase, remove anchors, and resume fight ===
    private static void cancelHealPhase(EnderDragon dragon) {
        dragon.setAI(true);
        dragon.setInvulnerable(false);
        dragon.setGlowing(false);

        List<Entity> anchors = healingAnchors.get(dragon.getUniqueId());
        for (Entity anchor : anchors) {
            if (!anchor.isDead()) {
                anchor.remove();
            }
        }
        anchors.clear();

        dragon.getWorld().playSound(dragon.getLocation(), Sound.ENTITY_WITHER_DEATH, 3f, 1f);
    }

    // === Trigger breaking void chains that make the dragon vulnerable ===
    private static void triggerVoidChains(EnderDragon dragon) {
        dragon.getWorld().playSound(dragon.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 4f, 0.7f);
        dragon.getWorld().spawnParticle(Particle.CRIT_MAGIC, dragon.getLocation(), 80, 3, 3, 3, 0.3);

        // Buff dragon and start resurrection phase
        if (!resurrectionTriggered) {
            resurrectionTriggered = true;
            startResurrection(dragon);
        }
    }

    // === Resurrection phase: dragon "dies", then revives as stronger form ===
    private static void startResurrection(EnderDragon dragon) {
        dragon.setHealth(0);
        dragon.getWorld().playSound(dragon.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 5f, 0.5f);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!dragon.isValid()) {
                    // Spawn resurrected dragon
                    Location loc = dragon.getLocation();
                    EnderDragon resurrected = (EnderDragon) loc.getWorld().spawnEntity(loc, EntityType.ENDER_DRAGON);
                    resurrected.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(RESURRECTED_MAX_HEALTH);
                    resurrected.setHealth(RESURRECTED_MAX_HEALTH);
                    resurrected.setCustomName("§5Resurrected Ender Dragon");
                    resurrected.setCustomNameVisible(true);

                    // Buffs: stronger attacks, faster movement
                    resurrected.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
                    resurrected.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

                    buffDragon(resurrected);
                    cancel();
                }
            }
        }.runTaskLater(plugin, 100L); // delay 5 seconds to simulate death animation
    }

    // === On final dragon death, drop special loot ===
    public static void onDragonFinalDeath(EnderDragon dragon) {
        if ("§5Resurrected Ender Dragon".equals(dragon.getCustomName())) {
            Location loc = dragon.getLocation();
            World world = loc.getWorld();

            ItemStack soulOfTheEnder = new ItemStack(Material.DRAGON_EGG);
            Item dropped = world.dropItemNaturally(loc, soulOfTheEnder);
            dropped.setCustomName("§6Soul of the Ender");
            dropped.setCustomNameVisible(true);
            world.playSound(loc, Sound.ENTITY_WITHER_SPAWN, 3f, 0.8f);
            world.spawnParticle(Particle.DRAGON_BREATH, loc, 50, 2, 2, 2, 0.4);

            Bukkit.broadcastMessage("§5The Fallen Aspect of the End has been vanquished! The Soul of the Ender is yours.");
        }
    }

}