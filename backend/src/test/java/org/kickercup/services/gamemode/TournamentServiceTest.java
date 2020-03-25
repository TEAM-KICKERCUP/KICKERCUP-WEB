//----------------------------------------------------------------------------------
//     Created By: Johannes Schweer
// Contributor(s): None
//    Description: Tournament Game Creation and Flow Test
//----------------------------------------------------------------------------------

package org.kickercup.services.gamemode;

import static org.junit.Assume.assumeTrue;
import java.util.ArrayList;
import java.util.List;
import javax.activation.UnsupportedDataTypeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.exception.RessourceValueViolationException;
import org.kickercup.api.model.Gamemode;
import org.kickercup.api.model.Player;
import org.kickercup.api.model.Team;
import org.kickercup.api.model.Tournament;
import org.kickercup.api.model.User;
import org.kickercup.api.services.impl.gamemode.GameModeFactory;
import org.kickercup.api.services.interfaces.IGameModeService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TournamentServiceTest {

    private Tournament tournament;

    public TournamentServiceTest() {

    }

    @Before
    public void initUseCase() {
    }

    @After
    public void cleanUp() {
    }

    private void mockTournament(final Gamemode gamemode) throws UnsupportedDataTypeException {
        final User user = new User("testuser", "123456", "test", "user", "johannes.schweer@stud.th-rosenheim.de");
        tournament = new Tournament(user, "testRankedTeam", gamemode, 1, 10, true);
        final List<Team> teamList = new ArrayList<Team>();

        if (gamemode == Gamemode.RankedTeamMatch) {
            final Player player1 = new Player(user, "Player", "1", "johannes.schweer@stud.th-rosenheim.de");
            final Player player2 = new Player(user, "Player", "2", "johannes.schweer@stud.th-rosenheim.de");
            final Player player3 = new Player(user, "Player", "3", "johannes.schweer@stud.th-rosenheim.de");
            final Player player4 = new Player(user, "Player", "4", "johannes.schweer@stud.th-rosenheim.de");

            final Team team1 = new Team(player1, player2);
            final Team team2 = new Team(player3, player4);

            teamList.add(team1);
            teamList.add(team2);
        } else if (gamemode == Gamemode.SingleKO) {
            final Player player1 = new Player(user, "Player", "1", "johannes.schweer@stud.th-rosenheim.de");
            final Player player2 = new Player(user, "Player", "2", "johannes.schweer@stud.th-rosenheim.de");
            final Player player3 = new Player(user, "Player", "3", "johannes.schweer@stud.th-rosenheim.de");
            final Player player4 = new Player(user, "Player", "4", "johannes.schweer@stud.th-rosenheim.de");
            final Player player5 = new Player(user, "Player", "5", "johannes.schweer@stud.th-rosenheim.de");
            final Player player6 = new Player(user, "Player", "6", "johannes.schweer@stud.th-rosenheim.de");
            final Player player7 = new Player(user, "Player", "7", "johannes.schweer@stud.th-rosenheim.de");
            final Player player8 = new Player(user, "Player", "8", "johannes.schweer@stud.th-rosenheim.de");

            final Team team1 = new Team(player1, player2);
            final Team team2 = new Team(player3, player4);
            final Team team3 = new Team(player5, player6);
            final Team team4 = new Team(player7, player8);

            teamList.add(team1);
            teamList.add(team2);
            teamList.add(team3);
            teamList.add(team4);

            long i = 0;
            for (Team t : teamList) {
                t.setId(i++);
            }
        }

        tournament.setTeams(teamList);
    }

    @Test
    public void testRankedTeam()
            throws UnsupportedDataTypeException, ResourceNotFoundException, RessourceValueViolationException {
        // Init Tournament
        mockTournament(Gamemode.RankedTeamMatch);

        // start Tournament
        tournament.setIsStarted(true);

        // generate Matches
        IGameModeService iGameModeService = GameModeFactory.getObject(tournament);
        tournament = iGameModeService.generateNewMatches(tournament);

        // Set Mach Results
        final List<org.kickercup.api.model.Match> matchList = tournament.getMatches();
        matchList.get(0).setGoalsGuest(0);
        matchList.get(0).setGoalsHome(10);
        matchList.get(0).setFinished(true);

        // update Match
        tournament.setMatches(matchList);
        tournament = iGameModeService.generateNewMatches(tournament);

        // check if Tournament run through
        assumeTrue(tournament.getIsFinished());
    }

    @Test
    public void testSingleKo()
            throws UnsupportedDataTypeException, RessourceValueViolationException, ResourceNotFoundException {
        // Init Tournament
        mockTournament(Gamemode.SingleKO);

        // start Tournament
        tournament.setIsStarted(true);

        // generate Matches
        IGameModeService iGameModeService = GameModeFactory.getObject(tournament);
        tournament = iGameModeService.generateNewMatches(tournament);

        // Set Mach Results
        List<org.kickercup.api.model.Match> matchList = tournament.getMatches();

        matchList.get(0).setGoalsGuest(0);
        matchList.get(0).setGoalsHome(10);
        matchList.get(0).setFinished(true);

        matchList.get(1).setGoalsGuest(0);
        matchList.get(1).setGoalsHome(10);
        matchList.get(1).setFinished(true);

        tournament.setMatches(matchList);
        tournament = iGameModeService.generateNewMatches(tournament);

        matchList = tournament.getMatches();

        matchList.get(2).setGoalsGuest(0);
        matchList.get(2).setGoalsHome(10);
        matchList.get(2).setFinished(true);

        tournament.setMatches(matchList);
        tournament = iGameModeService.generateNewMatches(tournament);

        // check if Tournament run through
        assumeTrue(tournament.getIsFinished());
    }

    @Test
    public void testSingleKoBigTournament()
            throws UnsupportedDataTypeException, RessourceValueViolationException, ResourceNotFoundException {
        // Init Tournament
        final User user = new User("testuser", "123456", "test", "user", "johannes.schweer@stud.th-rosenheim.de");
        tournament = new Tournament(user, "testRankedTeam", Gamemode.SingleKO, 1, 10, true);
        final List<Team> teamList = new ArrayList<Team>();

        final Player player1 = new Player(user, "Player", "1", "johannes.schweer@stud.th-rosenheim.de");
        final Player player2 = new Player(user, "Player", "2", "johannes.schweer@stud.th-rosenheim.de");
        final Player player3 = new Player(user, "Player", "3", "johannes.schweer@stud.th-rosenheim.de");
        final Player player4 = new Player(user, "Player", "4", "johannes.schweer@stud.th-rosenheim.de");
        final Player player5 = new Player(user, "Player", "5", "johannes.schweer@stud.th-rosenheim.de");
        final Player player6 = new Player(user, "Player", "6", "johannes.schweer@stud.th-rosenheim.de");
        final Player player7 = new Player(user, "Player", "7", "johannes.schweer@stud.th-rosenheim.de");
        final Player player8 = new Player(user, "Player", "8", "johannes.schweer@stud.th-rosenheim.de");
        final Player player9 = new Player(user, "Player", "9", "johannes.schweer@stud.th-rosenheim.de");
        final Player player10 = new Player(user, "Player", "10", "johannes.schweer@stud.th-rosenheim.de");
        final Player player11 = new Player(user, "Player", "11", "johannes.schweer@stud.th-rosenheim.de");
        final Player player12 = new Player(user, "Player", "12", "johannes.schweer@stud.th-rosenheim.de");
        final Player player13 = new Player(user, "Player", "13", "johannes.schweer@stud.th-rosenheim.de");
        final Player player14 = new Player(user, "Player", "14", "johannes.schweer@stud.th-rosenheim.de");
        final Player player15 = new Player(user, "Player", "15", "johannes.schweer@stud.th-rosenheim.de");
        final Player player16 = new Player(user, "Player", "16", "johannes.schweer@stud.th-rosenheim.de");

        final Team team1 = new Team(player1, player2);
        final Team team2 = new Team(player3, player4);
        final Team team3 = new Team(player5, player6);
        final Team team4 = new Team(player7, player8);
        final Team team5 = new Team(player9, player10);
        final Team team6 = new Team(player11, player12);
        final Team team7 = new Team(player13, player14);
        final Team team8 = new Team(player15, player16);

        teamList.add(team1);
        teamList.add(team2);
        teamList.add(team3);
        teamList.add(team4);
        teamList.add(team5);
        teamList.add(team6);
        teamList.add(team7);
        teamList.add(team8);

        long i = 0;
        for (Team t : teamList) {
            t.setId(i++);
        }

        tournament.setTeams(teamList);

        // start Tournament
        tournament.setIsStarted(true);

        // generate Matches
        IGameModeService iGameModeService = GameModeFactory.getObject(tournament);
        tournament = iGameModeService.generateNewMatches(tournament);

        // Set Mach Results
        List<org.kickercup.api.model.Match> matchList = tournament.getMatches();

        // Round 1
        matchList.get(0).setGoalsGuest(0);
        matchList.get(0).setGoalsHome(10);
        matchList.get(0).setFinished(true);

        matchList.get(1).setGoalsGuest(0);
        matchList.get(1).setGoalsHome(10);
        matchList.get(1).setFinished(true);

        matchList.get(2).setGoalsGuest(0);
        matchList.get(2).setGoalsHome(10);
        matchList.get(2).setFinished(true);

        matchList.get(3).setGoalsGuest(0);
        matchList.get(3).setGoalsHome(10);
        matchList.get(3).setFinished(true);

        tournament.setMatches(matchList);
        tournament = iGameModeService.generateNewMatches(tournament);

        matchList = tournament.getMatches();

        // Halbfinale
        matchList.get(4).setGoalsGuest(0);
        matchList.get(4).setGoalsHome(10);
        matchList.get(4).setFinished(true);

        matchList.get(5).setGoalsGuest(0);
        matchList.get(5).setGoalsHome(10);
        matchList.get(5).setFinished(true);

        tournament.setMatches(matchList);
        tournament = iGameModeService.generateNewMatches(tournament);

        matchList = tournament.getMatches();

        // Finale
        matchList.get(6).setGoalsGuest(0);
        matchList.get(6).setGoalsHome(10);
        matchList.get(6).setFinished(true);

        tournament.setMatches(matchList);
        tournament = iGameModeService.generateNewMatches(tournament);

        // check if Tournament run through
        assumeTrue(tournament.getIsFinished());
    }

}