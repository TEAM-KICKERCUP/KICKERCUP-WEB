//----------------------------------------------------------------------------------
//     Created By: Johannes Schweer
// Contributor(s): Jonas Jahns
//    Description: Solo Match GameMode
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.gamemode;

import java.util.*;
import javax.activation.UnsupportedDataTypeException;
import org.kickercup.api.model.*;
import org.kickercup.api.services.impl.tournament.result.table.ResultTableEntry;
import org.kickercup.api.services.interfaces.IGameModeService;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component("RankedSoloMatch")
public class RankedSoloMatchService implements IGameModeService{

    public Tournament generateNewMatches(final Tournament tournament) throws UnsupportedDataTypeException {
        RankedTeamMatchService rankedTeamMatchService = new RankedTeamMatchService();
        return rankedTeamMatchService.generateNewMatches(tournament);
    }

    public boolean isTeamListValid(List<Team> teamList) {
        if(teamList.size() == 2){
            return true;
        } else {
            return false;
        }
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

    public List<Team> generateTeams(List<Player> playerList, Tournament t){
        List<Team> teamList = new ArrayList<Team>();
        for (Player player : playerList) {
            Team team = new Team(player);
            team.setTournament(t);
            teamList.add(team);
        }
        return teamList;
    }
}