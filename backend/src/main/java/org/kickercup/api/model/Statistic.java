//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: Data transfer object for statistic
//----------------------------------------------------------------------------------
package org.kickercup.api.model;

import java.util.List;

public class Statistic {
    private long tournamentCount;
    private long matchCount;
    private long playersCount;
    private long goalsCount;
    private List<Player> players;

    public Statistic() {
    }

    public Statistic(long tournamentCount, long matchCount, long playersCount, long goalsCount, List<Player> players) {
        this.tournamentCount = tournamentCount;
        this.matchCount = matchCount;
        this.playersCount = playersCount;
        this.goalsCount = goalsCount;
        this.players = players;
    }

    public long getTournamentCount() {
        return tournamentCount;
    }

    public void setTournamentCount(long tournamentCount) {
        this.tournamentCount = tournamentCount;
    }

    public long getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(long matchCount) {
        this.matchCount = matchCount;
    }

    public long getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(long playersCount) {
        this.playersCount = playersCount;
    }

    public long getGoalsCount() {
        return goalsCount;
    }

    public void setGoalsCount(long goalsCount) {
        this.goalsCount = goalsCount;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
