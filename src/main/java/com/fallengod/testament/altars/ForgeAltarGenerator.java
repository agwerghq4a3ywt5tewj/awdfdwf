package com.fallengod.testament.altars;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ForgeAltarGenerator {
    
    public static void createForgeAltar(Player player) {
        Location center = player.getLocation();
        
        // Create 3x3 obsidian altar
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location altarLoc = center.clone().add(x, 0, z);
                altarLoc.getBlock().setType(Material.OBSIDIAN);
            }
        }
        
        player.sendMessage("Forge Altar created");
    }
}