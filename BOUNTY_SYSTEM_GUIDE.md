# Bounty System Guide - Fallen God Testament

## 🎯 Overview

The Bounty System creates high-stakes PvP moments by automatically placing bounties on players who complete significant achievements like testaments or Divine Convergence. This system encourages dynamic PvP interactions and creates server-wide events around major accomplishments.

## ⚔️ How It Works

### **Automatic Bounty Placement**
When a player completes a testament or achieves Divine Convergence, a bounty is automatically placed on them:

- **Testament Completion**: Default 10 diamond bounty
- **Divine Convergence**: Default 50 diamond bounty
- **Server Announcement**: All players are notified of the bounty
- **Duration**: Bounties expire after 60 minutes by default

### **Bounty Claims**
When a bounty target is killed by another player:

- **Automatic Reward**: Killer receives diamonds instantly
- **Server Broadcast**: Bounty claim is announced to all players
- **Epic Effects**: Special particles and sounds celebrate the claim
- **Bounty Removal**: The bounty is immediately removed from the target

## 🔧 Configuration

All bounty settings are configurable in `config.yml`:

```yaml
bounty_system:
  enabled: true
  testament_bounty_amount: 10      # Diamonds for testament completion
  convergence_bounty_amount: 50    # Diamonds for convergence achievement
  bounty_duration_minutes: 60     # How long bounties last
  
  messages:
    bounty_placed: "§c§l⚔ BOUNTY PLACED ⚔ §r§c{PLAYER} has a {AMOUNT} diamond bounty! Reason: {REASON} (Expires in {DURATION} minutes)"
    bounty_claimed: "§6§l💰 BOUNTY CLAIMED! §r§6{KILLER} has slain {TARGET} and claimed {AMOUNT} diamonds! ({REASON})"
```

### **Message Placeholders**
- `{PLAYER}` - Target player name
- `{KILLER}` - Player who claimed the bounty
- `{TARGET}` - Player who had the bounty
- `{AMOUNT}` - Bounty amount in diamonds
- `{REASON}` - Why the bounty was placed
- `{DURATION}` - How long the bounty lasts

## 📋 Admin Commands

### **View Active Bounties**
```
/fragment bounty list
```
Shows all active bounties with:
- Target player name
- Bounty amount
- Reason for bounty
- Time remaining

### **Clear All Bounties**
```
/fragment bounty clear
```
Removes all active bounties immediately.

## 🎮 Gameplay Impact

### **Strategic Considerations**
- **Risk vs Reward**: Completing testaments grants power but makes you a target
- **Timing**: Players may wait for safer moments to complete testaments
- **Group Play**: Teams may protect bounty targets or hunt them together
- **Server Events**: Bounty announcements create immediate server-wide focus

### **PvP Dynamics**
- **Immediate Targets**: Fresh testament completers become high-value targets
- **Convergence Hunters**: Players achieving convergence face massive bounties
- **Economic Impact**: Diamond rewards create additional motivation for PvP
- **Community Engagement**: Server-wide announcements drive interaction

## 🛡️ Balance Considerations

### **Protection Strategies**
Players can protect themselves by:
- Completing testaments in secure locations
- Having allies guard them during vulnerable periods
- Using testament powers defensively
- Timing completions strategically

### **Bounty Limitations**
- **Expiration**: Bounties don't last forever
- **Single Claim**: Each bounty can only be claimed once
- **No Stacking**: Multiple achievements don't stack bounties
- **Fair Play**: System prevents exploitation or griefing

## 🔄 Integration with Other Systems

### **Testament System**
- Bounties placed immediately after altar interaction
- Coordinates broadcast simultaneously with bounty placement
- Testament powers help defend against bounty hunters

### **Convergence System**
- Massive bounties for ultimate achievement
- Convergence powers provide strong defensive capabilities
- Creates epic server-wide events

### **Title System**
- Bounty claims may affect player titles
- Successful bounty hunters gain recognition
- Frequent targets may receive special titles

## 📊 Statistics and Tracking

The system tracks:
- Total bounties placed
- Successful bounty claims
- Most valuable bounties
- Top bounty hunters
- Bounty expiration rates

## 🎯 Best Practices

### **For Server Owners**
- **Balance Amounts**: Set bounty amounts appropriate for your server economy
- **Monitor Activity**: Watch for excessive targeting or griefing
- **Adjust Duration**: Modify bounty duration based on server activity
- **Custom Messages**: Personalize announcements for your community

### **For Players**
- **Strategic Timing**: Choose when to complete testaments carefully
- **Alliance Building**: Form protective alliances with other players
- **Power Usage**: Use testament abilities defensively when bounty is active
- **Risk Assessment**: Weigh the benefits of divine power against bounty risks

## 🔮 Future Enhancements

Planned improvements include:
- **Bounty Scaling**: Bounties that increase with player power level
- **Protection Items**: Special items that provide temporary bounty immunity
- **Bounty Contracts**: Player-placed bounties on specific targets
- **Leaderboards**: Rankings for top bounty hunters and most wanted players
- **Guild Bounties**: Team-based bounty systems for guild warfare

The Bounty System transforms testament completion from a solo achievement into a server-wide event, creating dynamic PvP opportunities and ensuring that divine power comes with divine consequences!