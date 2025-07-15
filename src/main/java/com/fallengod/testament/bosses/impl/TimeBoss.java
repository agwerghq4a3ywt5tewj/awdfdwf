package com.fallengod.testament.bosses.impl;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimeBoss extends GodBoss {
    
    private final Map<UUID, Double> playerHealthHistory;
    private boolean timeRewindActive = false;
    
    public TimeBoss(TestamentPlugin plugin) {
        super(plugin, BossType.TIME_KEEPER, GodType.TIME);
        this.playerHealthHistory = new HashMap<>();
    }
    
    @Override
    protected void setupBoss() {
        if (entity instanceof Shulker shulker) {
            shulker.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
            shulker.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
            shulker.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0));
            shulker.setCustomName("§d§lChronos Incarnate");
            shulker.setCustomNameVisible(true);
        }
        
        // Start time tracking
        startTimeTracking();
    }
    
    @Override
    protected void useSpecialAbility() {
        switch (phase) {
            case 1 -> timeDilation();
            case 2 -> temporalRewind();
            case 3 -> ageAcceleration();
        }
    }
    
    private void timeDilation() {
        // Slows all players to 10% speed
        double radius = getScaledRadius(20.0);
        
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= radius) {
                int duration = getScaledEffectDuration(200); // 10 seconds
                int level = getScaledEffectLevel(4); // Slowness V
                
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, level));
                player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, duration, 3));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration, 2));
                
                player.sendMessage("§d§lTime Dilation! §7Time crawls to a halt around you!");
                
                // Time distortion particles
                player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(0, 1, 0), 20, 1, 1, 1, 0.1);
                player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 15, 0.5, 0.5, 0.5, 0.05);
            }
        }
        
        entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 2.0f, 0.5f);
        
        // Announce
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= radius + 10) {
                player.sendMessage("§d§lTime Dilation! §7The flow of time bends to Chronos' will!");
            }
        }
    }
    
    private void temporalRewind() {
        // Resets boss health to previous state
        if (!timeRewindActive && entity.getHealth() < entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue() * 0.7) {
            timeRewindActive = true;
            
            double maxHealth = entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
            double rewindHealth = Math.min(maxHealth, entity.getHealth() + (maxHealth * 0.3));
            
            entity.setHealth(rewindHealth);
            
            // Temporal rewind effects
            entity.getWorld().spawnParticle(Particle.END_ROD, entity.getLocation(), 100, 3, 3, 3, 0.2);
            entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 50, 2, 2, 2, 0.1);
            entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2.0f, 2.0f);
            
            // Also rewind player health
            for (Player player : entity.getWorld().getPlayers()) {
                if (player.getLocation().distance(entity.getLocation()) <= 25) {
                    Double previousHealth = playerHealthHistory.get(player.getUniqueId());
                    if (previousHealth != null && previousHealth > player.getHealth()) {
                        player.setHealth(Math.min(player.getMaxHealth(), previousHealth));
                        player.sendMessage("§d§l⏰ Temporal Rewind! §7Your wounds heal as time reverses!");
                    }
                }
            }
            
            // Announce
            for (Player player : entity.getWorld().getPlayers()) {
                if (player.getLocation().distance(entity.getLocation()) <= 30) {
                    player.sendMessage("§d§lTemporal Rewind! §7Chronos un-does the damage!");
                }
            }
            
            // Reset rewind flag after cooldown
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                timeRewindActive = false;
            }, 600L); // 30 seconds
        }
    }
    
    private void ageAcceleration() {
        // Instantly grows crops and damages armor/weapons
        Location center = entity.getLocation();
        double radius = getScaledRadius(15.0);
        
        // Grow crops in area
        for (int x = -15; x <= 15; x++) {
            for (int z = -15; z <= 15; z++) {
                Location cropLoc = center.clone().add(x, -1, z);
                if (cropLoc.distance(center) <=  radius) {
                    Material block = cropLoc.getBlock().getType();
                    
                    // Age crops
                    if (block == Material.WHEAT || block == Material.CARROTS || 
                        block == Material.POTATOES || block == Material.BEETROOTS) {
                        
                        // Set to fully grown
                        org.bukkit.block.data.Ageable ageable =  (org.bukkit.block.data.Ageable) cropLoc.getBlock().getBlockData();
                        ageable.setAge(ageable.getMaximumAge());
                        cropLoc.getBlock().setBlockData(ageable);
                        
                        // Growth particles
                        cropLoc.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, cropLoc.add(0, 1, 0), 3, 0.3, 0.3, 0.3, 0.1);
                    }
                }
            }
        }
        
        // Age player equipment
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(center) <= radius) {
                // Damage all equipment
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.getType().getMaxDurability() > 0) {
                        int damage = (int) (item.getType().getMaxDurability() * 0.1); // 10% durability loss
                        item.setDurability((short) Math.min(item.getType().getMaxDurability(),  item.getDurability() + damage));
                    }
                }
                
                player.damage(getScaledDamage(8.0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 
                    getScaledEffectDuration(200), getScaledEffectLevel(2)));
                player.sendMessage("§d§lAge Acceleration! §7Time ravages your equipment and body!");
                
                // Aging particles
                player.getWorld().spawnParticle(Particle.SMOKE, player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);
            }
        }
        
        entity.getWorld().playSound(center, Sound.BLOCK_COMPOSTER_FILL_SUCCESS, 2.0f, 0.8f);
    }
    
    private void startTimeTracking() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isAlive()) {
                    cancel();
                    return;
                }
                
                // Track player health for rewind ability
                for (Player player : entity.getWorld().getPlayers()) {
                    if (player.getLocation().distance(entity.getLocation()) <= 50) {
                        playerHealthHistory.put(player.getUniqueId(), player.getHealth());
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 100L); // Every 5 seconds
    }
    
    @Override
    protected void enterPhase2() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        
        // Time effects become more severe
        entity.getWorld().spawnParticle(Particle.END_ROD, entity.getLocation(), 100, 5, 5, 5, 0.2);
        entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 50, 3, 3, 3, 0.1);
        
        // Announce phase change
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 30) {
                player.sendMessage("§d§lChronos grows stronger! Time itself bends to his will!");
            }
        }
    }
    
    @Override
    protected void enterPhase3() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,  Integer.MAX_VALUE, 2));
        
        // Constant time distortion
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isAlive()) {
                    cancel();
                    return;
                }
                
                // Random time effects
                if (Math.random() < 0.4) {
                    for (Player player : entity.getWorld().getPlayers()) {
                        if (player.getLocation().distance(entity.getLocation()) <= 20) {
                            // Random time effect
                            if (Math.random() < 0.5) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 3));
                            } else {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 3));
                            }
                            
                            player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(0, 1, 0), 5, 0.5, 0.5, 0.5, 0.1);
                            break;
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 40L); // Every 2 seconds
        
        entity.getWorld().spawnParticle(Particle.END_ROD, entity.getLocation(), 200, 8, 8, 8, 0.3);
    }
    
    @Override
    protected void onDeath() {
        Location loc = entity.getLocation();
        
        // Temporal collapse
        entity.getWorld().spawnParticle(Particle.END_ROD, loc, 300, 20, 20, 20, 0.5);
        entity.getWorld().spawnParticle(Particle.PORTAL, loc, 200, 15, 15, 15, 0.3);
        entity.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_DEATH, 3.0f, 0.5f);
        entity.getWorld().playSound(loc, Sound.BLOCK_BEACON_DEACTIVATE, 2.0f, 2.0f);
        
        // Time implosion effect
        for (int i = 0; i < 20; i++) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                Location timeLoc = loc.clone().add(
                    (Math.random() - 0.5) * 20,
                    Math.random() * 10,
                    (Math.random() - 0.5) * 20
                );
                
                loc.getWorld().spawnParticle(Particle.END_ROD, timeLoc, 5, 0.5, 0.5, 0.5, 0.1);
                loc.getWorld().playSound(timeLoc, Sound.BLOCK_AMETHYST_CLUSTER_HIT, 1.0f, 2.0f);
            }, i * 3L);
        }
        
        // Drop time loot
        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.CLOCK, 10));
        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.AMETHYST_CLUSTER, 15));
        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.NETHER_STAR, 3));
        
        // Drop Time God Fragment 7 (final fragment)
        loc.getWorld().dropItemNaturally(loc, com.fallengod.testament.items.FragmentItem.createFragment(GodType.TIME, 7));
        
        // Announce death
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 200) {
                player.sendTitle("§d§lCHRONOS INCARNATE DEFEATED", "§7Time flows normally once more", 20, 80, 20);
                player.sendMessage("§6§l⚔ Chronos Incarnate has been defeated! Time returns to its natural flow! ⚔");
            }
        }
        
        // Clean up tracking
        playerHealthHistory.clear();
    }
}