package com.fallengod.testament.commands;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BountyCommand implements CommandExecutor {

    private final TestamentPlugin plugin;

    public BountyCommand(TestamentPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "claim" -> claimBountyRewards(player);
            case "list" -> listPendingRewards(player);
            default -> showHelp(player);
        }

        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage("§6§l=== Bounty Commands ===");
        player.sendMessage("§e/bounty claim §7- Claim all pending bounty rewards");
        player.sendMessage("§e/bounty list §7- List pending bounty rewards");
    }

    private void claimBountyRewards(Player player) {
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        Map<UUID, List<ItemStack>> rewards = data.getPendingBountyRewards();

        if (rewards.isEmpty()) {
            player.sendMessage(ChatColor.RED + "You have no bounty rewards to claim.");
            return;
        }

        int totalRewards = 0;
        StringBuilder rewardSummary = new StringBuilder();
        rewardSummary.append("§6§l💰 Bounty Rewards Claimed! §r\n");

        for (Map.Entry<UUID, List<ItemStack>> entry : rewards.entrySet()) {
            UUID victimId = entry.getKey();
            List<ItemStack> victimRewards = entry.getValue();

            // Get victim name if possible
            Player victim = plugin.getServer().getPlayer(victimId);
            String victimName = victim != null ? victim.getName() : "Unknown Player";

            rewardSummary.append("§7From killing §c").append(victimName).append("§7:\n");

            for (ItemStack item : victimRewards) {
                // Add item to player inventory
                if (player.getInventory().firstEmpty() == -1) {
                    // Drop at player location if inventory is full
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                    player.sendMessage("§6Some rewards were dropped at your feet! (Inventory full)");
                } else {
                    player.getInventory().addItem(item);
                }

                // Add to summary
                String itemName = item.getType().name().toLowerCase().replace("_", " ");
                rewardSummary.append("§7  • §f").append(item.getAmount()).append("x ").append(itemName).append("\n");
                totalRewards++;
            }

            // Clear rewards for this victim
            data.clearBountyRewards(victimId);
        }

        // Save player data
        plugin.getPlayerDataManager().savePlayerData(player.getUniqueId());

        // Send summary message
        player.sendMessage(rewardSummary.toString());
        player.sendMessage("§6Total items claimed: §f" + totalRewards);

        // Epic effects
        player.getWorld().spawnParticle(org.bukkit.Particle.FIREWORK, player.getLocation(), 20, 1, 1, 1, 0.1);
        player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
    }

    private void listPendingRewards(Player player) {
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        Map<UUID, List<ItemStack>> rewards = data.getPendingBountyRewards();

        if (rewards.isEmpty()) {
            player.sendMessage("§7You have no pending bounty rewards.");
            return;
        }

        player.sendMessage("§6§l=== Pending Bounty Rewards ===");

        for (Map.Entry<UUID, List<ItemStack>> entry : rewards.entrySet()) {
            UUID victimId = entry.getKey();
            List<ItemStack> victimRewards = entry.getValue();

            // Get victim name if possible
            Player victim = plugin.getServer().getPlayer(victimId);
            String victimName = victim != null ? victim.getName() : "Unknown Player";

            player.sendMessage("§7From killing §c" + victimName + "§7:");

            for (ItemStack item : victimRewards) {
                String itemName = item.getType().name().toLowerCase().replace("_", " ");
                player.sendMessage("§7  • §f" + item.getAmount() + "x " + itemName);
            }
        }

        player.sendMessage("§7Use §e/bounty claim §7to collect all rewards!");
    }
}