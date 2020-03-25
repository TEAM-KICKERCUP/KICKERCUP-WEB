//----------------------------------------------------------------------------------
//     Created By: Johannes Schweer
// Contributor(s): Jonas Jahns
//    Description: Team Match GameMode
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.gamemode;

import java.util.ArrayList;
import java.util.List;
import org.kickercup.api.model.*;
import org.kickercup.api.services.impl.tournament.result.table.ResultTableEntry;
import org.kickercup.api.repository.MatchRepository;
import org.kickercup.api.services.interfaces.IGameModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component("RankedTeamMatch")
public class RankedTeamMatchService implements IGameModeService {

    @Autowired
    MatchRepository matchRepository;

    public Tournament generateNewMatches(final Tournament tournament) {

        List<Match> matchList = tournament.getMatches();

        // No Matches created yet
        if (matchList == null || matchList.isEmpty()) {
            matchList = new ArrayList<Match>();
            final List<Team> blackList = new ArrayList<Team>();
            for (final Team teamHome : tournament.getTeams()) {
                for (final Team teamGuest : tournament.getTeams()) {
                    // Teams can not play against themselves
                    if (teamHome.getPlayerLeft() != teamGuest.getPlayerLeft()
                            && (!blackList.contains(teamHome) || !blackList.contains(teamGuest))) {
                        final Match newMatch = new Match(teamHome, teamGuest);
                        newMatch.setTournament(tournament);
                        matchList.add(newMatch);
                        blackList.add(teamGuest);
                        blackList.add(teamHome);
                    }
                }
            }
            tournament.setMatches(matchList);
        } else {
            // Matches are allready created. RankedTeamMatch has only one Match. Nothing to
            // do.
            boolean isFinished = true;
            for (final Match match : matchList) {
                if (!match.isFinished()) { // A Match is not finished yet.
                    isFinished = false;
                    break;
                }
            }
            tournament.setIsFinished(isFinished);
        }
        return tournament;
    }

    @Override
    public boolean isTeamListValid(final List<Team> teamList) {
        boolean isValid = true;

        if (teamList.size() > 2) {
            // "Kann nicht mehr als zwei Teams haben."
            isValid = false;
        } else if (teamList.size() % 2 != 0) {
            // "Kann nicht eine ungerade Zahl an Teams haben."
            isValid = false;
        }else if (teamList.size() < 2) {
            // "Kann nicht weniger als zwei Teams haben."
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

        Map<Integer, Integer> placing = new HashMap<>();
        for (Long teamId : ids) {
            ResultTableEntry entry = preconfiguredEntries.get(teamId);
            placing.put(entry.getWins(), getPlace(placing, entry.getWins()));
        }

        return ids.stream()
                .map(preconfiguredEntries::get)
                .peek(entry -> entry.setPlace(placing.get(entry.getWins())))
                .collect(Collectors.toList());
    }

    private int getPlace(Map<Integer, Integer> placing, int wins) {
        Integer place = placing.get(wins);
        if (null == place) {
            AtomicInteger higherWins = new AtomicInteger(-1);
            AtomicInteger lowerWins = new AtomicInteger(-1);
            placing.keySet().stream().sorted().forEach(w -> {
                if (w < wins) {
                    lowerWins.set(w);
                }
                if (w > wins && higherWins.get() == -1) {
                    higherWins.set(w);
                }
            });

            // first place (no other values found)
            if (higherWins.get() == -1 && lowerWins.get() == -1) {
                return 1;
            }

            // there are no higher wins than the one given
            if (higherWins.get() == -1) {
                placing.keySet().forEach(w -> {
                    placing.put(w, placing.get(w) + 1);
                });
                return placing.get(higherWins.get() + 1);
            }

            // there are no lower wins than the one given
            if (lowerWins.get() == -1) {
                return placing.get(higherWins.get()) + 1;
            }

            // update lower places
            placing.keySet().forEach(w -> {
                if (w < wins) {
                    placing.put(w, placing.get(w) + 1);
                }
            });

            return placing.get(higherWins.get() + 1);
        } else {
            return place;
        }
    }
}