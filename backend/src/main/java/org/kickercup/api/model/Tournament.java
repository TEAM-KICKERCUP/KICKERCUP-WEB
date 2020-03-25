//----------------------------------------------------------------------------------
//     Created By: Christopher Heid
// Contributor(s): Jonas Jahns (Refactoring Data Model), Johannes Schweer (Bug fixing)
//    Description: Data transfer object for the tournament itself
//----------------------------------------------------------------------------------
package org.kickercup.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tournament")
public class Tournament {

	private long id;
	private String name;
	private Integer amountSets;
	private Integer amountGoals;
	private Boolean isRanked;
	private Boolean isFinished;
	private Boolean isStarted;

	private User user;
	private List<Match> matches;
	private List<Team> teams;

	private Gamemode gamemode;
	
	public Tournament() {
		
	}
	
	public Tournament(User user, String name, Gamemode gamemode, int amountSets, int amountGoals, boolean isRanked) {
		this.user = user;
		this.name = name;
		this.gamemode = gamemode;
		this.amountSets = amountSets;
		this.amountGoals = amountGoals;
		this.isRanked = isRanked;
		this.isStarted = false;
		this.isFinished = false;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "gamemode", nullable = false)
	@Enumerated(EnumType.STRING)
	public Gamemode getGamemode() {
		return gamemode;
	}
	public void setGamemode(Gamemode gamemode) {
		this.gamemode = gamemode;
	}
	@Column(name = "amountSets", nullable = false)
	public Integer getAmountSets() {
		return amountSets;
	}
	public void setAmountSets(int amountSets) {
		this.amountSets = amountSets;
	}
	@Column(name = "amountGoals", nullable = false)
	public Integer getAmountGoals() {
		return amountGoals;
	}
	public void setAmountGoals(int amountGoals) {
		this.amountGoals = amountGoals;
	}
	@Column(name = "isRanked", nullable = false)
	public Boolean getIsRanked() {
		return isRanked;
	}
	public void setIsRanked(boolean isRanked) {
		this.isRanked = isRanked;
	}
	@Column(name = "isStarted", nullable = false)
	public Boolean getIsStarted() {
		return isStarted;
	}
	public void setIsStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	@Column(name = "isFinished", nullable = false)
	public Boolean getIsFinished() {
		return isFinished;
	}
	public void setIsFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }

	@OneToMany(
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER,
			targetEntity = Match.class,
			orphanRemoval = true
	)
	@JoinColumn(name = "tournament_id")
	@JsonIgnore
	@Fetch(value = FetchMode.SUBSELECT)
	public List<Match> getMatches() { return matches; }
	public void setMatches(List<Match> matches) { 
		if(this.matches == null){
			this.matches = matches;
		} else if (this.matches != matches) { 
			this.matches.clear();
			this.matches.retainAll(matches);
			this.matches.addAll(matches);	
		}
	}

	@OneToMany(
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER,
			targetEntity = Team.class,
			orphanRemoval = true
	)
	@JoinColumn(name = "tournament_id")
	@JsonIgnore
	@Fetch(value = FetchMode.SUBSELECT)
	public List<Team> getTeams() { return teams; }
	public void setTeams(List<Team> teams) { 
		if(this.teams == null){
			this.teams = teams;
		} else if (this.teams != teams) { 
			this.teams.clear();
			this.teams.retainAll(teams);
			this.teams.addAll(teams);	
		}
	}

	@Override
	public String toString() {
		return this.toString();
	}
	
}
