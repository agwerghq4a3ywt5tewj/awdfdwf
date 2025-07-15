package com.fallengod.testament.data;

import com.fallengod.testament.enums.GodType;
import com.fallengod.testament.enums.PlayerTitle;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerData {
    private final UUID playerId;
    private final Map<GodType, Set<Integer>> fragments;
    private final Set<GodType> completedTestaments;
    private int chestsOpened;
    private long lastChestFragment;
    private long lastMobFragment;
    private PlayerTitle playerTitle;
    private final Map<UUID, List<ItemStack>> pendingBountyRewards;
    
    public PlayerData(UUID playerId) {
        this.playerId = playerId;
        this.fragments = new HashMap<>();
        this.completedTestaments = new HashSet<>();
        this.chestsOpened = 0;
        this.lastChestFragment = 0;
        this.lastMobFragment = 0;
        this.playerTitle = PlayerTitle.NONE;
        this.pendingBountyRewards = new HashMap<>();
        
        // Initialize fragment sets for each god
        for (GodType god : GodType.values()) {
            fragments.put(god, new HashSet<>());
        }
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public Set<Integer> getFragments(GodType god) {
        return fragments.get(god);
    }
    
    public void addFragment(GodType god, int fragmentNumber) {
        fragments.get(god).add(fragmentNumber);
    }
    
    public boolean hasFragment(GodType god, int fragmentNumber) {
        return fragments.get(god).contains(fragmentNumber);
    }
    
    public boolean hasAllFragments(GodType god) {
        return fragments.get(god).size() == 7;
    }
    
    public void removeAllFragments(GodType god) {
        fragments.get(god).clear();
    }
    
    public Set<GodType> getCompletedTestaments() {
        return completedTestaments;
    }
    
    public void completeTestament(GodType god) {
        completedTestaments.add(god);
    }
    
    public boolean hasCompletedTestament(GodType god) {
        return completedTestaments.contains(god);
    }
    
    public int getChestsOpened() {
        return chestsOpened;
    }
    
    public void incrementChestsOpened() {
        chestsOpened++;
    }
    
    public long getLastChestFragment() {
        return lastChestFragment;
    }
    
    public void setLastChestFragment(long time) {
        lastChestFragment = time;
    }
    
    public long getLastMobFragment() {
        return lastMobFragment;
    }
    
    public void setLastMobFragment(long time) {
        lastMobFragment = time;
    }
    
    public boolean canReceiveChestFragment(long cooldownHours) {
        long cooldownMs = cooldownHours * 60 * 60 * 1000;
        return System.currentTimeMillis() - lastChestFragment >= cooldownMs;
    }
    
    public boolean canReceiveMobFragment(long cooldownHours) {
        long cooldownMs = cooldownHours * 60 * 60 * 1000;
        return System.currentTimeMillis() - lastMobFragment >= cooldownMs;
    }
    
    public int getTotalFragments() {
        return fragments.values().stream().mapToInt(Set::size).sum();
    }
    
    public int getCompletionPercentage() {
        int totalPossible = GodType.values().length * 7;
        return (getTotalFragments() * 100) / totalPossible;
    }
    
    public PlayerTitle getPlayerTitle() {
        return playerTitle;
    }
    
    public void setPlayerTitle(PlayerTitle title) {
        this.playerTitle = title;
    }
    
    public void addBountyReward(UUID victimId, ItemStack reward) {
        pendingBountyRewards.computeIfAbsent(victimId, k -> new ArrayList<>()).add(reward);
    }
    
    public Map<UUID, List<ItemStack>> getPendingBountyRewards() {
        return new HashMap<>(pendingBountyRewards);
    }
    
    public void clearBountyRewards(UUID victimId) {
        pendingBountyRewards.remove(victimId);
    }
    
    public boolean hasPendingBountyRewards() {
        return !pendingBountyRewards.isEmpty();
    }
}