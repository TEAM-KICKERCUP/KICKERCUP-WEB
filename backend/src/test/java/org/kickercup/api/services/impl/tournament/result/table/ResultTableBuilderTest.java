//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Tournament Result Table Test
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.tournament.result.table;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.model.*;
import org.kickercup.api.services.impl.gamemode.GameModeFactory;
import org.kickercup.api.services.impl.localization.LabelService;
import java.util.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ResultTableBuilderTest {

    Tournament tournament;
    ResultTableBuilder builder;

    static Player playerOne;
    static Player playerTwo;

    static List<Team> teams;
    static List<Match> matches;

    ResultTable table;

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
    }

    @Before
    public void setup() throws ResourceNotFoundException, InstantiationException {
        Match matchA = new Match(teams.get(0), teams.get(1));
        matchA.setGoalsGuest(5);
        matchA.setGoalsHome(10);
        matchA.setFinished(true);
        Match matchB = new Match(teams.get(0), teams.get(1));
        matchB.setGoalsGuest(5);
        matchB.setGoalsHome(10);
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

        builder = new ResultTableBuilder();
        table = builder.buildTable(tournament, GameModeFactory.getObject(tournament), Locale.ENGLISH);
    }

    @Test
    public void testGetColumnHeadersReturnsListOfStrings() {
        String rankLabel = LabelService.getString(Locale.ENGLISH, "tournament.result.rank");
        String playerLabel = LabelService.getString(Locale.ENGLISH, "tournament.result.player");
        List<String> columnHeaders = table.getColumnHeaders();

        assertThat(columnHeaders.size(), is(1 + tournament.getTeams().size()));
        assertThat(columnHeaders.get(0), equalTo(rankLabel));

        for (int i = 1; i < columnHeaders.size(); i++) {
            assertThat(columnHeaders.get(i), equalTo(playerLabel));
        }
    }

    @Test
    public void testBuildTableEntriesHaveCorrectOrder() {
        String nameOfWinningPlayer = builder.getFullNameOfPlayer(teams.get(0).getPlayerLeft());
        String nameOfLosingPlayer = builder.getFullNameOfPlayer(teams.get(1).getPlayerLeft());

        assertThat(table.getEntries().size(), is(2));
        assertThat(table.getEntries().get(0).getPlayers().get(0), equalTo(nameOfWinningPlayer));
        assertThat(table.getEntries().get(1).getPlayers().get(0), equalTo(nameOfLosingPlayer));
    }

    @Test(expected = InstantiationException.class)
    public void testBuildTableWithoutMatchesThrowsException() throws ResourceNotFoundException, InstantiationException {
        tournament.setMatches(new ArrayList<>());
        table = builder.buildTable(tournament, GameModeFactory.getObject(tournament), Locale.ENGLISH);
    }

    @Test
    public void testBuildTableHasCorrectPlacing() throws ResourceNotFoundException, InstantiationException {
        assertThat(table.getEntries().get(0).getPlace(), is(1));
        assertThat(table.getEntries().get(1).getPlace(), is(2));
    }

    @Test
    public void testTableEntriesDrawHasCorrectPlaces() throws ResourceNotFoundException, InstantiationException {
        Match matchA = new Match(teams.get(0), teams.get(1));
        matchA.setGoalsGuest(5);
        matchA.setGoalsHome(5);
        matchA.setFinished(true);
        Match matchB = new Match(teams.get(0), teams.get(1));
        matchB.setGoalsGuest(5);
        matchB.setGoalsHome(5);
        matchB.setFinished(false);

        List<Match> matches = new ArrayList<>();
        matches.add(matchA);
        matches.add(matchB);
        tournament.setMatches(matches);
        ResultTable result = builder.buildTable(tournament, GameModeFactory.getObject(tournament), Locale.ENGLISH);
        result.getEntries().forEach(entry -> {
            Assert.assertThat(entry.getPlace(), is(1));
        });
    }

    @Test
    public void testTeamHomeHasGoalsScoredAndCounterGoals() throws ResourceNotFoundException, InstantiationException {
        int goalsScored = tournament.getMatches().stream().map(Match::getGoalsHome).reduce(0, Integer::sum);
        int counterGoals = tournament.getMatches().stream().map(Match::getGoalsGuest).reduce(0, Integer::sum);

        assertThat(table.getEntries().get(0).getGoalsScored(), is(goalsScored));
        assertThat(table.getEntries().get(0).getCounterGoals(), is(counterGoals));
    }

    @Test
    public void testTeamGuestHasGoalsScoredAndCounterGoals() throws ResourceNotFoundException, InstantiationException {
        int goalsScored = tournament.getMatches().stream().map(Match::getGoalsGuest).reduce(0, Integer::sum);
        int counterGoals = tournament.getMatches().stream().map(Match::getGoalsHome).reduce(0, Integer::sum);

        assertThat(table.getEntries().get(1).getGoalsScored(), is(goalsScored));
        assertThat(table.getEntries().get(1).getCounterGoals(), is(counterGoals));
    }
}
