package com.fallengod.testament.bosses.impl;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.scheduler.BukkitRunnable;

public class ChronosBoss extends GodBoss {
    
    public ChronosBoss(TestamentPlugin plugin) {
        super(plugin, BossType.TIME_KEEPER, GodType.TIME);
    }
    
    @Override
    protected void setupBoss() {
        entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(500.0);
        entity.setHealth(500.0);
        
        // Start time dilation task
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isAlive()) {
                    cancel();
                    return;
                }
                
                plugin.getLogger().info("Time Dilation");
            }
        }.runTaskTimer(plugin, 0L, 200L); // Every 10 seconds
    }
    
    @Override
    protected void useSpecialAbility() {
        // Minimal implementation
    }
    
    @Override
    protected void enterPhase2() {
        // Minimal implementation
    }
    
    @Override
    protected void enterPhase3() {
        // Minimal implementation
    }
    
    @Override
    protected void onDeath() {
        // Minimal implementation
    }
}