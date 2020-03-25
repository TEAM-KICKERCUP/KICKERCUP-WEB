//----------------------------------------------------------------------------------
//     Created By: Johannes Schweer
// Contributor(s): Jonas Jahns
//    Description: Single K.O. GameMode
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.gamemode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.kickercup.api.exception.RessourceValueViolationException;
import org.kickercup.api.model.Match;
import org.kickercup.api.model.Team;
import org.kickercup.api.model.Tournament;
import org.kickercup.api.services.impl.tournament.result.table.ResultTableEntry;
import org.kickercup.api.services.interfaces.IGameModeService;

public class SingleKoService implements IGameModeService {


    public Tournament generateNewMatches(final Tournament tournament) throws RessourceValueViolationException {

        List<Match> matchList = tournament.getMatches();

        // No Matches created yet
        if (matchList == null || matchList.isEmpty()) {
            matchList = generateMatchesInternal(tournament, new ArrayList<Team>());
            tournament.setMatches(matchList);
        } else {

            // Look for all the loser Teams who can not play anymore
            final List<Team> loser = new ArrayList<Team>();
            for (final Match match : matchList) {
                if (match.isFinished() == true) {
                    loser.add(getLoser(match));
                } else { // if they are still playing they can get a new match
                    loser.add(match.getTeamGuest());
                    loser.add(match.getTeamHome());
                }
            }
            // The Blacklist Assures that no Team has two Matches in one Round
            List<Match> newMatches = generateMatchesInternal(tournament, loser);

            // add new matches to the existing matches
            matchList.addAll(newMatches);
            tournament.setMatches(matchList);

            for(final Match match : tournament.getMatches()){
                if(!match.isFinished()){
                    tournament.setIsFinished(false);
                    break;
                } else {
                    tournament.setIsFinished(true);
                }
            }

        }
        return tournament;
    }

    private List<Match> generateMatchesInternal(Tournament tournament, final List<Team> blackList) {
        // The Blacklist Assures that no Team has two Matches in one Round.
        // In addition contains the blacklist the Losers as well
        List<Match> matchList = new ArrayList<Match>();

        for (final Team teamHome : tournament.getTeams()) {
            if (!blackList.contains(teamHome)) {
                for (final Team teamGuest : tournament.getTeams()) {
                    if (teamHome.getPlayerLeft() != teamGuest.getPlayerLeft() && !blackList.contains(teamGuest)) {
                        Match match = new Match(teamHome, teamGuest);
                        match.setTournament(tournament);
                        matchList.add(match);
                        blackList.add(teamGuest);
                        blackList.add(teamHome);
                        break;
                    }
                }
            }
        }
        return matchList;
    }

    private Team getLoser(final Match match) throws RessourceValueViolationException {
        if (match.getGoalsGuest() > match.getGoalsHome()) { // Guest = Winner & Home = Loser
            return match.getTeamHome();
        } else if (match.getGoalsGuest() < match.getGoalsHome()) {
            return match.getTeamGuest();
        } else {
            throw new RessourceValueViolationException("Ein Match kann in einem KO-Turnier nicht Unentschieden enden.");
        }
    }

    @Override
    public boolean isTeamListValid(final List<Team> teamList) {
        boolean isValid = true;
        int x = teamList.size();
        switch (x) {
            case 2:
            case 4:
            case 8:
            case 16:
            case 32:
            case 64:
            case 128:
            case 256:
            case 512:
            isValid = true;
            break;
            default: 
            isValid = false;
        }
        return isValid;
    }

    @Override
    public List<ResultTableEntry> sortWinningParticipants(Map<Long, ResultTableEntry> preconfiguredEntries) {
        List<Long> ids = preconfiguredEntries.keySet().stream()
                .sorted((teamA, teamB) ->
                        Integer.compare(preconfiguredEntries.get(teamB).getWins(), preconfiguredEntries.get(teamA).getWins()))
                .collect(Collectors.toList());

        List<Long> blackList = new ArrayList<>();
        int place = 1;
        for (Long id : ids) {
            if (!blackList.contains(id)) {
                ResultTableEntry entry = preconfiguredEntries.get(id);
                int wins = entry.getWins();
                for (Long idx : ids) {
                    if (!blackList.contains(idx)) {
                        ResultTableEntry entry2 = preconfiguredEntries.get(idx);
                        if (entry2.getWins() == wins) {
                            entry2.setPlace(place);
                            blackList.add(idx);
                        }
                    }

                }
                place++;
            }
        }

        return ids.stream().map(preconfiguredEntries::get).collect(Collectors.toList());
    }
}