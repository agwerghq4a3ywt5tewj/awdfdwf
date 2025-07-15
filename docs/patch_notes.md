# Patch Notes - Version 1.8.0

## Bug Fixes
- Fixed compile errors for Paper 1.21.5
  - Updated Particle.ITEM_CRACK → Particle.ITEM
  - Updated PotionEffectType.CONFUSION → PotionEffectType.NAUSEA
  
  - Updated Sound.ENTITY_WOLF_HOWL → Sound.ENTITY_WOLF_AMBIENT

## New Features
- Added Chronos, Blood, and Shadow boss classes
  - ChronosBoss: 500 health with Time Dilation debug messages
  - BloodBoss: 600 health with Blood Frenzy trigger detection
  - ShadowBoss: 450 health with Shadow Strike debug messages

- Added weapon ability listeners
  - TimeDilationListener: Handles Chronos Staff right-click interactions
  - BloodFrenzyListener: Handles Crimson Blade combat abilities

- Added basic altar generator
  - ForgeAltarGenerator: Creates 3x3 obsidian altars for testing

## Technical Improvements
- Enhanced boss system with minimal implementations
- Improved weapon interaction handling
- Added utility classes for altar generation