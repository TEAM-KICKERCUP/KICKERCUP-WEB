//----------------------------------------------------------------------------------
//     Created By: Jonas Janhs
// Contributor(s): Johannes Schweer (Relationship adjusments, bug fixing)
//    Description: Data transfer object for teams
//----------------------------------------------------------------------------------
package org.kickercup.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
@Table(name = "team")
public class Team implements Participant {
    private Tournament tournament;
    private long id;
    private Player playerLeft;
    private Player playerRight;

    public Team() { }

    public Team(Player playerLeft) {
        this.playerLeft = playerLeft;
    }

    public Team(Player playerLeft, Player playerRight) {
        this.playerLeft = playerLeft;
        this.playerRight = playerRight;
    }

    @OneToOne(fetch = FetchType.EAGER)
    public Player getPlayerLeft() {
        return playerLeft;
    }
    public void setPlayerLeft(Player playerLeft) {
        this.playerLeft = playerLeft;
    }

    @OneToOne(fetch = FetchType.EAGER)
    public Player getPlayerRight() {
        return playerRight;
    }
    public void setPlayerRight(Player playerRight) {
        this.playerRight = playerRight;
    }
    
    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "tournament_id", nullable = false)
    public Tournament getTournament() {
        return tournament;
    }
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
