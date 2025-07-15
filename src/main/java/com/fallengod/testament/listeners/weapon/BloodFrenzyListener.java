package com.fallengod.testament.listeners.weapon;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class BloodFrenzyListener implements Listener {
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }
        
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null && weapon.hasItemMeta() && weapon.getItemMeta().hasCustomModelData()) {
            if (weapon.getItemMeta().getCustomModelData() == 100063) { // CRIMSON_BLADE
                double healthPercent = player.getHealth() / player.getMaxHealth();
                if (healthPercent < 0.5) {
                    player.sendMessage("Blood Frenzy triggered");
                }
            }
        }
    }
}