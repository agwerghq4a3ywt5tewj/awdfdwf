package com.fallengod.testament.listeners.weapon;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TimeDilationListener implements Listener {
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_AIR && 
            event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
            if (item.getItemMeta().getCustomModelData() == 100062) { // CHRONOS_STAFF
                player.sendMessage("Time Dilation activated");
                event.setCancelled(true);
            }
        }
    }
}