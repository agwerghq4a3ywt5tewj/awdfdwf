# Boss Scaling Guide - Fallen God Testament

## 🏔️ Overview

The Boss Scaling System ensures that all god bosses provide an appropriate challenge regardless of your server's difficulty setting. Bosses automatically detect the world difficulty and scale their health, damage, and abilities accordingly.

## ⚙️ How Scaling Works

### **Automatic Detection**
When a boss spawns, it automatically:
1. Detects the world's current difficulty setting
2. Applies appropriate scaling multipliers
3. Adjusts health, damage, and ability parameters
4. Logs scaling information for debugging

### **Scaling Parameters**
Each difficulty level has specific multipliers:

| Difficulty | Health | Damage | Effects | Cooldowns | Radius | Minions |
|------------|--------|--------|---------|-----------|--------|---------|
| Peaceful   | 50%    | 50%    | Weaker  | Longer    | Smaller| Fewer   |
| Easy       | 75%    | 75%    | Reduced | Slightly+ | Reduced| Less    |
| Normal     | 100%   | 100%   | Baseline| Normal    | Normal | Normal  |
| Hard       | 150%   | 150%   | Enhanced| Shorter   | Larger | More    |

## 🔧 Configuration

Boss scaling is configured in `config.yml`:

```yaml
boss_scaling:
  enabled: true
  
  peaceful:
    health_multiplier: 0.5
    damage_multiplier: 0.5
    ability_modifiers:
      effect_duration_multiplier: 0.5
      effect_level_modifier: -1
      cooldown_multiplier: 1.5
      radius_multiplier: 0.75
      minion_spawn_multiplier: 0.5
      ability_damage_multiplier: 0.5
      
  # Similar sections for easy, normal, hard
```

### **Scaling Parameters Explained**

#### **Basic Scaling**
- **`health_multiplier`**: Scales boss maximum health
- **`damage_multiplier`**: Scales basic attack damage

#### **Ability Modifiers**
- **`effect_duration_multiplier`**: How long potion effects last
- **`effect_level_modifier`**: Adds/subtracts from effect levels (e.g., Strength I → II)
- **`cooldown_multiplier`**: How fast abilities recharge (lower = faster)
- **`radius_multiplier`**: Size of AoE abilities
- **`minion_spawn_multiplier`**: Number of spawned minions
- **`ability_damage_multiplier`**: Damage from special abilities

## 🎯 Practical Examples

### **Fallen Warden Boss on Different Difficulties**

#### **Peaceful Mode**
- **Health**: 250 HP (50% of 500)
- **Soul Drain**: 2 damage instead of 4, affects 11 blocks instead of 15
- **Death Pulse**: Wither I for 50 ticks instead of Wither II for 100 ticks
- **Necrotics**: Spawns 2-3 skeletons instead of 5
- **Cooldowns**: Abilities every 7.5 seconds instead of 5

#### **Hard Mode**
- **Health**: 750 HP (150% of 500)
- **Soul Drain**: 6 damage instead of 4, affects 19 blocks instead of 15
- **Death Pulse**: Wither III for 150 ticks instead of Wither II for 100 ticks
- **Necrotics**: Spawns 7-8 skeletons instead of 5
- **Cooldowns**: Abilities every 3.75 seconds instead of 5

## 🎮 Impact on Gameplay

### **Peaceful/Easy Servers**
- **Accessible**: New players can experience boss content
- **Learning**: Reduced complexity allows learning mechanics
- **Forgiving**: Mistakes are less punishing
- **Progression**: Still challenging enough to feel rewarding

### **Normal Servers**
- **Balanced**: Intended difficulty experience
- **Standard**: Reference point for all balance decisions
- **Versatile**: Suitable for most player skill levels
- **Consistent**: Matches other Minecraft difficulty scaling

### **Hard Servers**
- **Intense**: Maximum challenge for experienced players
- **Coordinated**: May require team strategies
- **Rewarding**: Greater sense of accomplishment
- **Endgame**: Suitable for veteran players seeking challenge

## 🛠️ Implementation Details

### **Scaling Application**
Scaling is applied when bosses spawn through these helper methods:

```java
// Damage scaling
double scaledDamage = getScaledDamage(baseDamage);

// Radius scaling  
double scaledRadius = getScaledRadius(baseRadius);

// Effect scaling
int duration = getScaledEffectDuration(baseDuration);
int level = getScaledEffectLevel(baseLevel);

// Minion scaling
int minionCount = getScaledMinionCount(baseCount);
```

### **Boss-Specific Adaptations**
Each boss type applies scaling to their unique abilities:

- **Fallen Warden**: Soul drain range, death pulse radius, minion spawning
- **Banishment Blaze**: Fireball count, meteor frequency, fire ring size
- **Abyssal Guardian**: Tidal wave range, whirlpool strength, pressure damage
- **Crystal Resonator**: Sonic boom range, crystal prison size, resonance wave power

## 📊 Monitoring and Debugging

### **Logging Information**
The system logs scaling application:
```
[INFO] Boss scaling applied for HARD: Health=1.5, Damage=1.5, Effects=1.5
```

### **Admin Commands**
- **`/boss info <god>`**: Shows current boss stats including scaling
- **`/boss list`**: View all active bosses with their scaled health
- **Debug logs**: Enable in config for detailed scaling information

## 🎯 Best Practices

### **For Server Owners**
- **Test Scaling**: Spawn bosses on different difficulties to verify balance
- **Monitor Feedback**: Watch player reactions to boss difficulty
- **Adjust Gradually**: Make small tweaks rather than dramatic changes
- **Document Changes**: Keep track of custom scaling modifications

### **For Players**
- **Understand Difficulty**: Know your server's difficulty setting
- **Prepare Accordingly**: Bring appropriate gear for the difficulty level
- **Team Strategy**: Higher difficulties may require coordination
- **Learn Patterns**: Boss mechanics remain the same, just scaled

## 🔄 Customization Options

### **Custom Scaling**
Server owners can create custom scaling by:
1. Modifying multipliers in `config.yml`
2. Creating difficulty-specific strategies
3. Balancing for their player base
4. Testing with different player group sizes

### **Selective Scaling**
You can disable specific aspects:
```yaml
boss_scaling:
  enabled: true
  hard:
    health_multiplier: 1.0      # No health scaling
    damage_multiplier: 1.5      # But keep damage scaling
```

## 🔮 Future Enhancements

Planned improvements include:
- **Player Count Scaling**: Bosses scale with number of nearby players
- **Dynamic Difficulty**: Bosses adjust during combat based on performance
- **Custom Modifiers**: Per-boss scaling overrides
- **Seasonal Events**: Special scaling during events
- **Achievement Integration**: Scaling affects based on player achievements

The Boss Scaling System ensures that every player, regardless of their server's difficulty setting, faces appropriately challenging and rewarding boss encounters that match their skill level and expectations!