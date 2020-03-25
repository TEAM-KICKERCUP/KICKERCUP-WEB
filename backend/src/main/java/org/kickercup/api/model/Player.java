//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): Johannes Schweer, Lucas Wierer
//    Description: Data transfer object for player
//----------------------------------------------------------------------------------
package org.kickercup.api.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "players")
public class Player implements Participant {

    public static final int INITIAL_SCORE = 1500;

    private long id;
    private String firstName;
    private String lastName;
    private String emailId;
    private int rankScore = INITIAL_SCORE;

    private User user;

    public Player() {

    }

    public Player(User user, String firstName, String lastName, String emailId) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.rankScore = 1000;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "firstname", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "lastname", nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "email", nullable = false)
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Column(name = "rankScore", nullable = false)
    public int getrankScore() {
        return rankScore;
    }

    public void setrankScore(int rankScore) {
        this.rankScore = rankScore;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return this.toString();
    }

    public void updateRankScore(double opponentScore, MatchResult matchResult) {
        int weight = 20;
        double SA = matchResult.getActualPoints();
        double expectedValue = calculateExpectedValue(opponentScore);

        this.rankScore = (int) Math.round(this.rankScore + weight * (SA - expectedValue));
    }

	/**
	 * Calculate the likelihood of player winning against an opponent
	 * @param opponent
	 * @return
	 */
	public double calculateExpectedValue(double opponentScore) {
        double pow = (opponentScore - this.rankScore) / 400;
        double divisor = 1 + Math.pow(10, pow);
        double expectedValue = 1 / divisor;

        return expectedValue;
    }
}