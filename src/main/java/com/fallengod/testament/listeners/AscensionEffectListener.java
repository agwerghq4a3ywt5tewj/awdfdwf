package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class AscensionEffectListener implements Listener {
    
    private final TestamentPlugin plugin;
    
    public AscensionEffectListener(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Reapply ascension effects on join
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getAscensionManager().applyAscensionEffects(player);
            plugin.getTitleManager().updatePlayerDisplayName(player, 
                plugin.getTitleManager().getPlayerTitle(player.getUniqueId()));
        }, 10L);
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        // Reapply ascension effects after respawn
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getAscensionManager().applyAscensionEffects(player);
        }, 5L);
    }
}