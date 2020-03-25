//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Tournament Result Table
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.tournament.result.table;

import java.util.List;

public class ResultTableEntry {
    private List<String> players;
    private int place;
    private int goalsScored;
    private int counterGoals;
    private int wins;

    public ResultTableEntry(List<String> players, int place, int goalsScored, int counterGoals, int wins) {
        this.players = players;
        this.place = place;
        this.goalsScored = goalsScored;
        this.counterGoals = counterGoals;
        this.wins = wins;
    }

    public ResultTableEntry() { }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public int getCounterGoals() {
        return counterGoals;
    }

    public void setCounterGoals(int counterGoals) {
        this.counterGoals = counterGoals;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}
