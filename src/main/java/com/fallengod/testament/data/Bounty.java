package com.fallengod.testament.data;

import java.util.UUID;

public class Bounty {
    private final UUID targetId;
    private final double amount;
    private final long placedTime;
    private final String reason;
    private final long duration;
    
    public Bounty(UUID targetId, double amount, String reason, long durationMinutes) {
        this.targetId = targetId;
        this.amount = amount;
        this.reason = reason;
        this.placedTime = System.currentTimeMillis();
        this.duration = durationMinutes * 60 * 1000; // Convert to milliseconds
    }
    
    public UUID getTargetId() {
        return targetId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public long getPlacedTime() {
        return placedTime;
    }
    
    public String getReason() {
        return reason;
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() - placedTime > duration;
    }
    
    public long getTimeRemaining() {
        long remaining = duration - (System.currentTimeMillis() - placedTime);
        return Math.max(0, remaining);
    }
    
    public long getTimeRemainingMinutes() {
        return getTimeRemaining() / (60 * 1000);
    }
}