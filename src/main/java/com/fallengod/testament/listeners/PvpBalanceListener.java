package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.enums.PlayerTitle;
import com.fallengod.testament.items.ConvergenceItems;
import com.fallengod.testament.items.SpecialItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PvpBalanceListener implements Listener {
    
    private final TestamentPlugin plugin;
    
    public PvpBalanceListener(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker) || 
            !(event.getEntity() instanceof Player victim)) {
            return;
        }
        
        if (!plugin.getConfigManager().isPvpBalanceEnabled()) {
            return;
        }
        
        // Apply title-based damage modifiers
        PlayerTitle attackerTitle = plugin.getTitleManager().getPlayerTitle(attacker.getUniqueId());
        double damageMultiplier = plugin.getConfigManager().getTitleDamageMultiplier(attackerTitle);
        
        // Apply environmental bonuses
        damageMultiplier *= calculateEnvironmentalBonus(attacker);
        
        // Apply the final damage modifier
        event.setDamage(event.getDamage() * damageMultiplier);
        
        // Apply defense modifiers to victim
        PlayerTitle victimTitle = plugin.getTitleManager().getPlayerTitle(victim.getUniqueId());
        double defenseMultiplier = plugin.getConfigManager().getTitleDefenseMultiplier(victimTitle);
        event.setDamage(event.getDamage() * (2.0 - defenseMultiplier)); // Inverse for defense
        
        // Block convergence items in PvP
        if (plugin.getConfigManager().areConvergenceItemsDisabledInPvp()) {
            ItemStack weapon = attacker.getInventory().getItemInMainHand();
            if (ConvergenceItems.isConvergenceItem(weapon)) {
                event.setCancelled(true);
                attacker.sendMessage("§c⚠ Convergence items are disabled in PvP!");
                return;
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.getConfigManager().isPvpBalanceEnabled()) {
            return;
        }
        
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        // Block Void Rip in PvP if configured
        if (plugin.getConfigManager().isVoidRipDisabledInPvp() && 
            item != null && item.hasItemMeta() && 
            item.getItemMeta().hasCustomModelData() && 
            item.getItemMeta().getCustomModelData() == 100061) { // Void Blade
            
            // Check if there are nearby players (PvP context)
            boolean nearbyPlayers = player.getWorld().getPlayers().stream()
                .anyMatch(p -> !p.equals(player) && p.getLocation().distance(player.getLocation()) <= 50);
            
            if (nearbyPlayers) {
                event.setCancelled(true);
                player.sendMessage("§c⚠ Void Rip is disabled in PvP areas!");
            }
        }
    }
    
    private double calculateEnvironmentalBonus(Player player) {
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon == null || !weapon.hasItemMeta()) {
            return 1.0;
        }
        
        if (!plugin.getConfigManager().areEnvironmentalBonusesEnabled()) {
            return 1.0;
        }
        
        String biome = player.getLocation().getBlock().getBiome().name().toLowerCase();
        int lightLevel = player.getLocation().getBlock().getLightLevel();
        int altitude = player.getLocation().getBlockY();
        
        // Abyssal Trident bonuses
        if (SpecialItems.isAbyssalTrident(weapon)) {
            if (biome.contains("ocean") || biome.contains("river") || 
                player.getLocation().getBlock().isLiquid()) {
                return plugin.getConfigManager().getWeaponEnvironmentalModifier("abyssal_trident", "water_bonus");
            } else {
                return plugin.getConfigManager().getWeaponEnvironmentalModifier("abyssal_trident", "land_penalty");
            }
        }
        
        // Sylvan Bow bonuses
        if (SpecialItems.isSylvanBow(weapon)) {
            if (biome.contains("forest") || biome.contains("jungle") || biome.contains("taiga")) {
                return plugin.getConfigManager().getWeaponEnvironmentalModifier("sylvan_bow", "forest_bonus");
            } else if (biome.contains("city") || biome.contains("urban") || lightLevel > 12) {
                return plugin.getConfigManager().getWeaponEnvironmentalModifier("sylvan_bow", "urban_penalty");
            }
        }
        
        // Storm Elytra bonuses
        if (SpecialItems.isStormElytra(weapon)) {
            if (altitude > 100) {
                return plugin.getConfigManager().getWeaponEnvironmentalModifier("storm_elytra", "sky_bonus");
            } else if (altitude < 50) {
                return plugin.getConfigManager().getWeaponEnvironmentalModifier("storm_elytra", "underground_penalty");
            }
        }
        
        // Shadow Mantle bonuses
        if (SpecialItems.isShadowMantle(weapon)) {
            if (lightLevel < 7) {
                return plugin.getConfigManager().getWeaponEnvironmentalModifier("shadow_mantle", "darkness_bonus");
            } else if (lightLevel > 12) {
                return plugin.getConfigManager().getWeaponEnvironmentalModifier("shadow_mantle", "daylight_penalty");
            }
        }
        
        return 1.0;
    }
}