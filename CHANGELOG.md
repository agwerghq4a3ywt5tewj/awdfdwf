# Changelog - Fallen God Testament

## [2.0.0] - Dynamic PvP & Scaling Update

### 🎯 NEW FEATURE: BOUNTY SYSTEM
- **Testament Bounties**: Players who complete testaments automatically receive bounties
- **Convergence Bounties**: Massive bounties for Divine Convergence achievers
- **Automatic Rewards**: Killers receive diamond rewards instantly
- **Server Broadcasting**: Bounty placement and claims announced server-wide
- **Configurable System**: Customizable bounty amounts, duration, and messages
- **Admin Management**: Commands to list and clear active bounties
- **Expiration System**: Bounties automatically expire after configured time
- **Epic Effects**: Special particles and sounds for bounty claims

### 🏔️ NEW FEATURE: DYNAMIC BOSS SCALING
- **Difficulty-Based Scaling**: All bosses scale with server difficulty setting
- **Health & Damage Scaling**: Automatic adjustment from 50% (Peaceful) to 150% (Hard)
- **Ability Scaling**: Boss special abilities scale in power and frequency
- **Effect Scaling**: Potion effects, durations, and levels adjust with difficulty
- **Minion Scaling**: Number of spawned minions scales with difficulty
- **Radius Scaling**: AoE ability ranges adjust based on difficulty
- **Cooldown Scaling**: Ability frequencies change with difficulty
- **Automatic Detection**: Bosses detect world difficulty on spawn

### 📡 NEW FEATURE: COORDINATE BROADCASTING
- **Testament Coordinates**: Broadcast altar locations when testaments are completed
- **Convergence Coordinates**: Announce Convergence Nexus location when activated
- **World Information**: Include world names for multi-world servers
- **Configurable Messages**: Customize broadcast format with placeholders
- **Toggle System**: Enable/disable specific broadcast types
- **Enhanced Community**: Server-wide awareness of major achievements

### 🔧 ENHANCED CONFIGURATION
- **Bounty System Settings**: Configure amounts, duration, and messages
- **Boss Scaling Settings**: Fine-tune scaling multipliers for each difficulty
- **Broadcast Settings**: Customize coordinate announcement messages
- **Comprehensive Options**: Over 20 new configuration options added
- **Backward Compatibility**: All existing configurations remain valid

### 🎮 IMPROVED GAMEPLAY
- **High-Stakes PvP**: Testament completion creates immediate PvP targets
- **Consistent Challenge**: Boss difficulty always matches server setting
- **Community Engagement**: Coordinate broadcasts drive exploration and interaction
- **Risk vs Reward**: Greater divine power comes with greater target status
- **Dynamic Events**: Every major achievement becomes a server-wide event

### 🛠️ TECHNICAL IMPROVEMENTS
- **BountyManager**: New manager class for bounty system operations
- **Enhanced ConfigManager**: Extended with 15+ new configuration methods
- **Scaling Architecture**: Robust boss scaling system with helper methods
- **Event Integration**: Bounty and broadcast systems integrate with existing events
- **Performance Optimized**: Efficient bounty tracking and cleanup systems
- **API Extensions**: New API methods for bounty system access

### 📋 NEW COMMANDS
- **`/fragment bounty list`**: View all active bounties with details
- **`/fragment bounty clear`**: Clear all active bounties (admin only)
- **Enhanced Help**: Updated command help with new bounty options

### 🎨 VISUAL ENHANCEMENTS
- **Bounty Claim Effects**: Epic particles and sounds when bounties are claimed
- **Coordinate Formatting**: Beautiful broadcast messages with world information
- **Boss Scaling Feedback**: Visual indicators of difficulty scaling
- **Enhanced Death Messages**: Special messages for bounty claim kills

### 🔄 COMPATIBILITY & MIGRATION
- **Seamless Upgrade**: Existing saves and configurations work without changes
- **New Defaults**: Sensible default values for all new features
- **Optional Features**: All new systems can be disabled if desired
- **API Backward Compatibility**: Existing API integrations continue to work

## [1.9.0] - Divine Convergence Update

### 🌟 THE ULTIMATE ACHIEVEMENT: DIVINE CONVERGENCE
- **Convergence Nexus**: Legendary shrine that spawns when a player completes all 12 testaments
- **Crown of Divine Convergence**: Ultimate helmet with +20 hearts, complete damage immunity, and all testament powers
- **Scepter of Omnipotence**: Ultimate weapon with 1000 damage, reality manipulation, and all weapon abilities
- **Divine Codex of All Knowledge**: Complete lore book with all god knowledge and divine techniques
- **Master of All Divinity**: Ultimate title with creative flight and infinite resources
- **Epic Server Events**: Server-wide announcements and celebrations for convergence achievement
- **Convergence Event System**: Custom events for plugin integration and expansion
- **Memorial System**: Nexus leaves a beacon memorial after activation

### ✨ Enhanced Progression System
- **Extended Ascension**: New "Convergence" level beyond Godlike for ultimate achievement
- **Automatic Nexus Spawning**: System detects 12-testament completion and spawns convergence shrine
- **Ultimate Effects**: Complete damage immunity, creative flight, and reality manipulation powers
- **Achievement Tracking**: Persistent tracking of convergence status across server restarts
- **Epic Visual Effects**: Massive particle explosions and sound sequences for convergence activation

### 🔧 Technical Improvements
- **ConvergenceManager**: New manager class handling ultimate achievement system
- **ConvergenceEvent**: Custom event fired when divine convergence is achieved
- **ConvergenceItems**: New item class for ultimate convergence rewards
- **Enhanced API**: Extended TestamentAPI with convergence checking methods
- **Performance Optimized**: Efficient convergence checking and nexus management
- **Heart Return System**: Automatic return of Heart of Fallen God after totem consumption
- **Boss System Integration**: Complete boss battle system with god-specific encounters
- **Attribute Compatibility**: Updated for Paper 1.21.5 attribute constants

## [1.8.0] - Professional Enhancement Update

### 🎯 Major New Features
- **Public API System**: Added `TestamentAPI` for external plugin integration
- **Custom Events**: `TestamentCompletedEvent` and `FragmentFoundEvent` for developers
- **Godlex System**: Interactive divine knowledge compendium with book and text modes
- **Altar Management**: Complete admin toolkit for altar creation and management
- **Enhanced Configuration**: Expanded config with effects, conflicts, and cross-platform settings
- **Divine Conflicts**: Heart of Fallen God cannot coexist with Mace of Divine Forging
- **Heart Return System**: Heart returns 45 seconds after totem pop
- **Armor Trim System**: Fallen armor features Silence trim with rotating colors

### ✨ Polish & Immersion
- **Sound & Particle Effects**: Enhanced feedback for all major actions
- **Cross-Platform Support**: Bedrock/Geyser friendly UI improvements
- **Conflict Penalties**: Configurable effects when opposing gods clash
- **Altar Protection**: Grief protection system for generated altars
- **Enhanced Feedback**: Improved titles, action bars, and visual effects
- **God-Specific Effects**: Unique particles and sounds for each god's altar completion
- **Visual Polish**: Enhanced weapon abilities with better feedback

### 🔧 Admin Tools
- **Debug Commands**: Comprehensive debugging and testing tools
- **Altar Commands**: Create, regenerate, and manage altars manually
- **Enhanced Fragment Commands**: Better testing and management options
- **Configuration Expansion**: More granular control over all systems
- **Conflict Management**: Tools to manage divine conflicts and incompatibilities

### 🎮 Player Experience
- **Godlex Book**: Beautiful in-game guide with progress tracking
- **Better Progression Feedback**: Clear status updates and visual cues
- **Improved Commands**: More intuitive and helpful command structure
- **Enhanced Tooltips**: Better item descriptions and lore
- **Strategic Choices**: Meaningful decisions between conflicting divine powers
- **Immersive Effects**: Rich particle and sound systems for all interactions

### 🏗️ Technical Improvements
- **API Integration**: Clean public API for other plugins
- **Event System**: Proper event firing for all major actions
- **Performance Optimization**: Better caching and async operations
- **Code Organization**: Improved structure and maintainability
- **Conflict Prevention**: Robust system to prevent incompatible divine powers
- **Enhanced Configuration**: Comprehensive config system with full documentation

### 🔄 Compatibility
- **Geyser/Floodgate**: Enhanced Bedrock Edition support
- **Plugin Integration**: API allows seamless integration with other plugins
- **Version Compatibility**: Maintained compatibility with Paper 1.21.5+
- **Cross-Platform UI**: Better support for touch and console interfaces

---

## [1.7.0] - Complete God System

### 🏛️ The Twelve Gods
- **6 Core Gods**: Fallen, Banishment, Abyssal, Sylvan, Tempest, Veil
- **6 Expansion Gods**: Forge, Void, Time, Blood, Crystal, Shadow
- **Advanced Weapon Abilities**: Active and passive abilities for all weapons
- **Divine Ascension**: 5-tier progression system (Mortal → Godlike)

### ⚔️ Weapon Systems
- **Active Abilities**: Right-click abilities with cooldowns
- **Passive Effects**: Automatic weapon enhancements
- **Testament Conflicts**: Strategic opposing god pairs
- **Enhanced Combat**: Sophisticated weapon mechanics

### 🎯 Player Systems
- **Title System**: Dynamic player titles based on behavior
- **Toxicity Detection**: Automatic toxic behavior monitoring
- **Heart vs Veil**: Strategic PvP balance mechanics
- **Ascension Effects**: Progressive power increases

---

## [1.6.0] - Foundation Release

### 🎮 Core Systems
- **Fragment Collection**: 7 fragments per god from chests and mobs
- **Altar Generation**: Natural worldgen altar spawning
- **Testament Completion**: Right-click altar interaction system
- **Player Data**: Persistent progress tracking

### 🏗️ Technical Foundation
- **Plugin Architecture**: Modular manager-based design
- **Configuration System**: Comprehensive YAML configuration
- **Command System**: Player and admin command interfaces
- **Event Listeners**: Complete event handling system

---

## Future Roadmap

### 🌟 Planned Features
- **✅ Divine Convergence**: Ultimate achievement system - **COMPLETED!**
- **Guild Integration**: Team-based testament progression
- **Seasonal Events**: Time-limited divine challenges
- **Custom Enchantments**: Testament-specific enchantments
- **World Events**: Server-wide divine interventions
- **Post-Convergence Content**: New challenges for convergence achievers

### 🔧 Technical Goals
- **Database Integration**: Optional database backend
- **Web Dashboard**: Online progress tracking
- **Metrics System**: Detailed analytics and statistics
- **Plugin Ecosystem**: Expansion pack support