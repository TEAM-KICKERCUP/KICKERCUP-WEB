//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Tournament Result Table Test
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.tournament.result;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.model.*;
import org.kickercup.api.services.interfaces.TournamentResultBuilder;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class TournamentResultBuilderImplTest {

    Tournament tournament;
    TournamentResultBuilder builder;

    static Player playerOne;
    static Player playerTwo;

    static List<Team> teams;

    static List<Match> matches;

    @BeforeClass
    public static void initialize() {
        playerOne = new Player(null, "John", "Wayne", "john.wayne@localhost");
        playerTwo = new Player(null, "Edward", "Nigma", "edward.nigma@localhost");

        Team teamA = new Team(playerOne);
        teamA.setId(1);
        Team teamB = new Team(playerTwo);
        teamB.setId(2);
        teams = new ArrayList<>();
        teams.add(teamA);
        teams.add(teamB);

        Match matchA = new Match(teamA, teamB);
        matchA.setGoalsGuest(5);
        matchA.setGoalsHome(5);
        matchA.setFinished(true);
        Match matchB = new Match(teamA, teamB);
        matchB.setGoalsGuest(5);
        matchB.setGoalsHome(5);
        matchB.setFinished(false);

        matches = new ArrayList<>();
        matches.add(matchA);
        matches.add(matchB);
    }

    @Before
    public void setup() {
        Match matchA = new Match(teams.get(0), teams.get(1));
        matchA.setGoalsGuest(5);
        matchA.setGoalsHome(5);
        matchA.setFinished(true);
        Match matchB = new Match(teams.get(0), teams.get(1));
        matchB.setGoalsGuest(5);
        matchB.setGoalsHome(5);
        matchB.setFinished(false);

        matches = new ArrayList<>();
        matches.add(matchA);
        matches.add(matchB);

        tournament = new Tournament();
        tournament.setId(1);
        tournament.setAmountGoals(2);
        tournament.setAmountSets(1);
        tournament.setGamemode(Gamemode.RankedTeamMatch);
        tournament.setIsFinished(true);
        tournament.setName("Test Tournament");
        tournament.setIsRanked(true);

        tournament.setMatches(matches);
        tournament.setTeams(teams);
        tournament.setUser(null);

        builder = new TournamentResultBuilderImpl();
    }

    @Test
    public void testBuildTournamentShouldReturnResult() throws ResourceNotFoundException, InstantiationException {
        TournamentResult result = builder.build(tournament);
        assertThat(result, notNullValue());
    }

    @Test
    public void testBuildResultHasHeader() throws ResourceNotFoundException, InstantiationException {
        tournament.setName("Test");
        TournamentResult result = builder.build(tournament);
        assertThat(result.getHeader(), notNullValue());
        assertThat(result.getHeader(), equalTo("Test"));
    }

    @Test
    public void testBuildResultHasDefaultHeader() throws ResourceNotFoundException, InstantiationException {
        tournament.setName(null);
        TournamentResult result = builder.build(tournament);
        assertThat(result.getHeader(), notNullValue());
        assertThat(result.getHeader(), equalTo("Tournament"));
    }

    @Test
    public void testBuildResultHasListOfSubheaders() throws ResourceNotFoundException, InstantiationException {
        TournamentResult result = builder.build(tournament);
        assertThat(result.getSubHeaders(), notNullValue());
        assertThat(result.getSubHeaders(), isA(List.class));
    }

    @Test
    public void testBuildResultHasListOfSubHeadersWithStrings() throws ResourceNotFoundException, InstantiationException {
        TournamentResult result = builder.build(tournament);
        assertThat(result.getSubHeaders().size() > 0, is(true));
        result.getSubHeaders().forEach(subHeader -> assertThat(subHeader, isA(String.class)));
    }

    @Test
    public void testBuildResultHasTable() throws ResourceNotFoundException, InstantiationException {
        TournamentResult result = builder.build(tournament);
        assertThat(result.getTable(), notNullValue());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testBuildResultThrowsExceptionIfGameModeDoesNotExist() throws ResourceNotFoundException, InstantiationException {
        tournament.setGamemode(null);
        TournamentResult result = builder.build(tournament);
        assertThat(result, nullValue());
    }

    @Test(expected = InstantiationException.class)
    public void testBuildResultThrowsExceptionIfTableCannotBeBuild() throws ResourceNotFoundException, InstantiationException {
        tournament.setMatches(new ArrayList<>());
        builder.build(tournament);
    }
}