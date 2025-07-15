package com.fallengod.testament.bosses.impl;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class BloodBoss extends GodBoss {
    
    public BloodBoss(TestamentPlugin plugin) {
        super(plugin, BossType.BLOOD_BERSERKER, GodType.BLOOD);
    }
    
    @Override
    protected void setupBoss() {
        entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(600.0);
        entity.setHealth(600.0);
    }
    
    @Override
    protected void useSpecialAbility() {
        double healthPercent = entity.getHealth() / entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
        
        if (healthPercent < 0.5) {
            plugin.getLogger().info("Blood Frenzy triggered");
        }
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