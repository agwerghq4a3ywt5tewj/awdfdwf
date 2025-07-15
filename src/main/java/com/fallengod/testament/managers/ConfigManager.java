package com.fallengod.testament.managers;

import com.fallengod.testament.TestamentPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final TestamentPlugin plugin;
    private FileConfiguration config;
    
    public ConfigManager(TestamentPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
    
    public double getChestSpawnChance() {
        return config.getDouble("testament.fragments.chest_spawn_chance", 0.02);
    }
    
    public double getMobDropChance() {
        return config.getDouble("testament.fragments.mob_drop_chance", 0.001);
    }
    
    public int getMinChestsForFragments() {
        return config.getInt("testament.fragments.min_chests_for_fragments", 50);
    }
    
    public int getChestCooldownHours() {
        return config.getInt("testament.fragments.chest_cooldown_hours", 2);
    }
    
    public int getMobCooldownHours() {
        return config.getInt("testament.fragments.mob_cooldown_hours", 1);
    }
    
    public int getFragmentWeight(int fragmentNumber) {
        return config.getInt("testament.fragment_weights.fragment_" + fragmentNumber, 10);
    }
    
    public boolean isHeartEnabled() {
        return config.getBoolean("heart_of_fallen_god.enabled", true);
    }
    
    public int getHeartExtraHearts() {
        return config.getInt("heart_of_fallen_god.extra_hearts", 15);
    }
    
    public int getHeartStrengthLevel() {
        return config.getInt("heart_of_fallen_god.strength_level", 1);
    }
    
    public int getHeartRegenerationLevel() {
        return config.getInt("heart_of_fallen_god.regeneration_level", 2);
    }
    
    public int getHeartResistanceLevel() {
        return config.getInt("heart_of_fallen_god.resistance_level", 1);
    }
    
    public boolean isVeilEnabled() {
        return config.getBoolean("veil_nullification.enabled", true);
    }
    
    public double getVeilRange() {
        return config.getDouble("veil_nullification.range", 16.0);
    }
    
    public boolean getVeilSlowFalling() {
        return config.getBoolean("veil_nullification.veil_effects.slow_falling", true);
    }
    
    public boolean getVeilNightVision() {
        return config.getBoolean("veil_nullification.veil_effects.night_vision", true);
    }
    
    public int getVeilSpeedLevel() {
        return config.getInt("veil_nullification.veil_effects.speed_level", 1);
    }
    
    // Bounty System Configuration
    public boolean isBountySystemEnabled() {
        return config.getBoolean("bounty_system.enabled", true);
    }
    
    public double getTestamentBountyAmount() {
        return config.getDouble("bounty_system.testament_bounty_amount", 10.0);
    }
    
    public double getConvergenceBountyAmount() {
        return config.getDouble("bounty_system.convergence_bounty_amount", 50.0);
    }
    
    public long getBountyDurationMinutes() {
        return config.getLong("bounty_system.bounty_duration_minutes", 60);
    }
    
    public String getBountyPlacedMessage() {
        return config.getString("bounty_system.messages.bounty_placed", 
            "§c§l⚔ BOUNTY PLACED ⚔ §r§c{PLAYER} has a {AMOUNT} diamond bounty! Reason: {REASON} (Expires in {DURATION} minutes)");
    }
    
    public String getBountyClaimedMessage() {
        return config.getString("bounty_system.messages.bounty_claimed", 
            "§6§l💰 BOUNTY CLAIMED! §r§6{KILLER} has slain {TARGET} and claimed {AMOUNT} diamonds! ({REASON})");
    }
    
    // Broadcast System Configuration
    public boolean isBroadcastEnabled() {
        return config.getBoolean("broadcasts.enabled", true);
    }
    
    public boolean isTestamentCoordsBroadcastEnabled() {
        return config.getBoolean("broadcasts.testament_coords", true);
    }
    
    public boolean isConvergenceCoordsBroadcastEnabled() {
        return config.getBoolean("broadcasts.convergence_coords", true);
    }
    
    public String getTestamentCompletionMessage() {
        return config.getString("broadcasts.messages.testament_completion", 
            "§6⚡ {PLAYER} completed the {GOD} Testament at {X}, {Y}, {Z} in {WORLD}! ⚡");
    }
    
    public String getConvergenceActivationMessage() {
        return config.getString("broadcasts.messages.convergence_activation", 
            "§6✦ {PLAYER} achieved Divine Convergence at {X}, {Y}, {Z} in {WORLD}! ✦");
    }
    
    // Boss Scaling Configuration
    public boolean isBossScalingEnabled() {
        return config.getBoolean("boss_scaling.enabled", true);
    }
    
    public double getHealthMultiplier(org.bukkit.Difficulty difficulty) {
        String path = "boss_scaling." + difficulty.name().toLowerCase() + ".health_multiplier";
        return config.getDouble(path, 1.0);
    }
    
    public double getDamageMultiplier(org.bukkit.Difficulty difficulty) {
        String path = "boss_scaling." + difficulty.name().toLowerCase() + ".damage_multiplier";
        return config.getDouble(path, 1.0);
    }
    
    public double getEffectDurationMultiplier(org.bukkit.Difficulty difficulty) {
        String path = "boss_scaling." + difficulty.name().toLowerCase() + ".ability_modifiers.effect_duration_multiplier";
        return config.getDouble(path, 1.0);
    }
    
    public int getEffectLevelModifier(org.bukkit.Difficulty difficulty) {
        String path = "boss_scaling." + difficulty.name().toLowerCase() + ".ability_modifiers.effect_level_modifier";
        return config.getInt(path, 0);
    }
    
    public double getCooldownMultiplier(org.bukkit.Difficulty difficulty) {
        String path = "boss_scaling." + difficulty.name().toLowerCase() + ".ability_modifiers.cooldown_multiplier";
        return config.getDouble(path, 1.0);
    }
    
    public double getRadiusMultiplier(org.bukkit.Difficulty difficulty) {
        String path = "boss_scaling." + difficulty.name().toLowerCase() + ".ability_modifiers.radius_multiplier";
        return config.getDouble(path, 1.0);
    }
    
    public double getMinionSpawnMultiplier(org.bukkit.Difficulty difficulty) {
        String path = "boss_scaling." + difficulty.name().toLowerCase() + ".ability_modifiers.minion_spawn_multiplier";
        return config.getDouble(path, 1.0);
    }
    
    public double getAbilityDamageMultiplier(org.bukkit.Difficulty difficulty) {
        String path = "boss_scaling." + difficulty.name().toLowerCase() + ".ability_modifiers.ability_damage_multiplier";
        return config.getDouble(path, 1.0);
    }
    
    // PvP Balance Configuration
    public boolean isPvpBalanceEnabled() {
        return config.getBoolean("pvp_balance.enabled", true);
    }
    
    public boolean areConvergenceItemsDisabledInPvp() {
        return config.getBoolean("pvp_balance.convergence_items_disabled", true);
    }
    
    public boolean isVoidRipDisabledInPvp() {
        return config.getBoolean("pvp_balance.disable_void_rip_in_pvp", false);
    }
    
    public double getTitleDamageMultiplier(com.fallengod.testament.enums.PlayerTitle title) {
        String path = "pvp_balance.title_modifiers." + title.name().toLowerCase() + ".damage_multiplier";
        return config.getDouble(path, 1.0);
    }
    
    public double getTitleDefenseMultiplier(com.fallengod.testament.enums.PlayerTitle title) {
        String path = "pvp_balance.title_modifiers." + title.name().toLowerCase() + ".defense_multiplier";
        return config.getDouble(path, 1.0);
    }
    
    // Environmental Bonuses
    public boolean areEnvironmentalBonusesEnabled() {
        return config.getBoolean("environmental_bonuses.enabled", true);
    }
    
    public double getWeaponEnvironmentalModifier(String weapon, String environment) {
        String path = "environmental_bonuses.weapon_modifiers." + weapon + "." + environment;
        return config.getDouble(path, 1.0);
    }
    
    // Boss Rewards
    public int getBossFragmentDropChance() {
        return config.getInt("boss_rewards.fragment_drop_chance", 100);
    }
    
    public int getFragmentsPerBoss() {
        return config.getInt("boss_rewards.fragments_per_boss", 2);
    }
}