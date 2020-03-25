//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Tournament Result Table
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.tournament.result.table;

import org.kickercup.api.model.*;
import org.kickercup.api.services.impl.localization.LabelService;
import org.kickercup.api.services.interfaces.IGameModeService;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResultTableBuilder {

    private static final Logger LOGGER = Logger.getLogger(ResultTableBuilder.class.getName());

    public ResultTable buildTable(Tournament tournament, IGameModeService gameModeService, Locale locale) throws InstantiationException {
        String rankLabel = LabelService.getString(locale, "tournament.result.rank");
        String playerLabel = LabelService.getString(locale, "tournament.result.player");

        if (null == tournament || null == tournament.getMatches() || tournament.getMatches().size() == 0) {
            LOGGER.log(Level.SEVERE, "The result table for this tournament cannot be build, tournament id = "
                    + (null != tournament ? tournament.getId() : "null" ));
            throw new InstantiationException("Cannot operate on tournament");
        }

        Map<Long, ResultTableEntry> entries = createEntries(tournament);
        List<ResultTableEntry> sortedEntries = gameModeService.sortWinningParticipants(entries);

        List<String> columnHeaders = createColumnHeaders(rankLabel, playerLabel, sortedEntries);

        return createTable(sortedEntries, columnHeaders);
    }

    private ResultTable createTable(List<ResultTableEntry> participants, List<String> columnHeaders) {
        ResultTable table = new ResultTable();
        table.setColumnHeaders(columnHeaders);
        table.setEntries(participants);
        return table;
    }

    private Map<Long, ResultTableEntry> createEntries(Tournament tournament) {
        List<Match> matches = tournament.getMatches();
        Map<Long, ResultTableEntry> entries = new HashMap<>();
        for (Match match : matches) {
            getEntriesFromMatch(entries, match);
        }
        return entries;
    }

    private List<String> createColumnHeaders(String rankLabel, String playerLabel, List<ResultTableEntry> participants) {
        List<String> columnHeaders = new ArrayList<>(Collections.singletonList(rankLabel));
        columnHeaders.addAll(IntStream.range(0, participants.stream()
                .mapToInt(entry -> entry.getPlayers().size())
                .max().orElse(2))
                .mapToObj(idx -> playerLabel)
                .collect(Collectors.toList()));
        return columnHeaders;
    }

    private void getEntriesFromMatch(Map<Long, ResultTableEntry> res, Match match) {
        Team teamHome = match.getTeamHome();
        Team teamGuest = match.getTeamGuest();

        ResultTableEntry homeEntry = res.get(match.getTeamHome().getId());
        ResultTableEntry guestEntry = res.get(match.getTeamGuest().getId());

        Boolean teamHomeWon = checkIfTeamHomeWon(match);

        if (null == guestEntry) {
            guestEntry = new ResultTableEntry();
            guestEntry.setCounterGoals(match.getGoalsHome());
            guestEntry.setGoalsScored(match.getGoalsGuest());
            guestEntry.setPlayers(getPlayersOfTeam(teamGuest));
            guestEntry.setWins(null != teamHomeWon && !teamHomeWon ? 1 : 0);
        } else {
            guestEntry.setGoalsScored(guestEntry.getGoalsScored() + match.getGoalsGuest());
            guestEntry.setCounterGoals(guestEntry.getCounterGoals() + match.getGoalsHome());
            guestEntry.setWins(null != teamHomeWon && !teamHomeWon ? guestEntry.getWins() + 1 : guestEntry.getWins());
        }
        res.put(teamGuest.getId(), guestEntry);

        if (null == homeEntry) {
            homeEntry = new ResultTableEntry();
            homeEntry.setCounterGoals(match.getGoalsGuest());
            homeEntry.setGoalsScored(match.getGoalsHome());
            homeEntry.setPlayers(getPlayersOfTeam(teamHome));
            homeEntry.setWins(null != teamHomeWon && teamHomeWon ? 1 : 0);
        } else {
            homeEntry.setGoalsScored(homeEntry.getGoalsScored() + match.getGoalsHome());
            homeEntry.setCounterGoals(homeEntry.getCounterGoals() + match.getGoalsGuest());
            homeEntry.setWins(null != teamHomeWon && teamHomeWon ? homeEntry.getWins() + 1 : homeEntry.getWins());
        }
        res.put(teamHome.getId(), homeEntry);
    }

    private List<String> getPlayersOfTeam(Team team) throws IllegalArgumentException {
        if (null == team) {
            throw new IllegalArgumentException("team cannot be null");
        }
        return Arrays.asList(getFullNameOfPlayer(team.getPlayerLeft()), " " +
                getFullNameOfPlayer(team.getPlayerRight()));
    }

    private Boolean checkIfTeamHomeWon(Match match) {
        if (match.getGoalsHome() > match.getGoalsGuest()) {
            return true;
        } else if (match.getGoalsHome() < match.getGoalsGuest()) {
            return false;
        }
        return null;
    }

    public String getFullNameOfPlayer(Player player) {
        if (null == player) {
            return "";
        }
        return player.getFirstName() + " " + player.getLastName();
    }
}
