//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): Johannes Schweer (Several important adjusments)
//    Description: Data transfer object for games
//----------------------------------------------------------------------------------
package org.kickercup.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
@Table(name = "game_match")
public class Match {
    private Long id;

    private Team teamHome;
    private Team teamGuest;

    private int goalsHome;
    private int goalsGuest;

    private boolean finished;

    private Tournament tournament;

    public Match() { }

    public Match(Team teamHome, Team teamGuest) {
        this.teamHome = teamHome;
        this.teamGuest = teamGuest;
        this.finished = false;
        this.goalsHome = 0;
        this.goalsGuest = 0;
    }

    @OneToOne(fetch = FetchType.EAGER)
    public Team getTeamHome() {
        return teamHome;
    }
    public void setTeamHome(Team teamHome) {
        this.teamHome = teamHome;
    }

    @OneToOne(fetch = FetchType.EAGER)
    public Team getTeamGuest() {
        return teamGuest;
    }
    public void setTeamGuest(Team teamGuest) {
        this.teamGuest = teamGuest;
    }

    @Column(name = "goals_home")
    public Integer getGoalsHome() {
        return goalsHome;
    }
    public void setGoalsHome(int goalsHome) {
        this.goalsHome = goalsHome;
    }

    @Column(name = "goals_guest")
    public Integer getGoalsGuest() {
        return goalsGuest;
    }
    public void setGoalsGuest(int goalsGuest) {
        this.goalsGuest = goalsGuest;
    }

    @Column(name = "match_finished")
    public Boolean isFinished() {
        return finished;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    public Tournament getTournament() {
        return tournament;
    }
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
}
