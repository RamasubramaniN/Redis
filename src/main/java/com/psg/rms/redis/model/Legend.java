package com.psg.rms.redis.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author rn5
 *
 */
public class Legend {
    
    private long id;
    private String playerName;
    private String team;
    private int rank;
    
    public Legend(long id, String playerName, String team, int rank) {
        this.id = id;
        this.playerName = playerName;
        this.team = team;
        this.rank = rank;
    }

    public long getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getTeam() {
        return team;
    }

    public int getRank() {
        return rank;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
