package com.fallengod.testament.bosses.impl;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.scheduler.BukkitRunnable;

public class ShadowBoss extends GodBoss {
    
    public ShadowBoss(TestamentPlugin plugin) {
        super(plugin, BossType.SHADOW_ASSASSIN, GodType.SHADOW);
    }
    
    @Override
    protected void setupBoss() {
        entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(450.0);
        entity.setHealth(450.0);
        
        // Start shadow strike task
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isAlive()) {
                    cancel();
                    return;
                }
                
                plugin.getLogger().info("Shadow Strike");
            }
        }.runTaskTimer(plugin, 0L, 300L); // Every 15 seconds
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