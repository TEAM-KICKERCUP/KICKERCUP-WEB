//----------------------------------------------------------------------------------
//     Created By: Johannes Schweer
// Contributor(s): None
//    Description: Service for handling teams during game creation
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.exception.RessourceValueViolationException;
import org.kickercup.api.model.Gamemode;
import org.kickercup.api.model.Player;
import org.kickercup.api.model.Team;
import org.kickercup.api.model.Tournament;
import org.kickercup.api.repository.TournamentRepository;
import org.kickercup.api.services.impl.gamemode.GameModeFactory;
import org.kickercup.api.services.impl.gamemode.RankedSoloMatchService;
import org.kickercup.api.services.interfaces.IGameModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    TournamentRepository tournamentRepository;

    public List<Team> generateTeams(Tournament tournament, ArrayList<Player> playerList)
            throws ResourceNotFoundException, RessourceValueViolationException {
        List<Team> teamList = new LinkedList<Team>();

        if(tournament.getGamemode() == Gamemode.RankedSoloMatch){
            RankedSoloMatchService rankedSolo = (RankedSoloMatchService) GameModeFactory.getObject(tournament);
            teamList = rankedSolo.generateTeams(playerList, tournament);
        } else if(tournament.getIsRanked() == false) {
            teamList = this.randomTeamMaking(playerList, tournament);
        } else {
            teamList = this.eloBasedTeamMaking(playerList, tournament);
        }

        IGameModeService gm = GameModeFactory.getObject(tournament);
        if(!gm.isTeamListValid(teamList)){
            throw new RessourceValueViolationException("Anzahl der Teams ist für diesen Spielmodus nicht korrekt");
        } else {
            tournament.setTeams(teamList);
            return tournamentRepository.save(tournament).getTeams();
        }
    }

    private List<Team> randomTeamMaking (List<Player> playerList, Tournament tournament){
        List<Team> teamList = new LinkedList<Team>();
        List<Player> blackList = new LinkedList<Player>();
        for (Player player : playerList) {
            for (Player player2 : playerList) {
                if (player != player2 && !blackList.contains(player2) && !blackList.contains(player)) {
                    Team t = new Team(player, player2);
                    t.setTournament(tournament);
                    teamList.add(t);
                    blackList.add(player);
                    blackList.add(player2);
                    break;
                }
            }
        }
        return teamList;
    }

    private List<Team> eloBasedTeamMaking (ArrayList<Player> playerList, Tournament tournament) {
        List<Team> teamList = new LinkedList<Team>();
        List<Player> blackList = new LinkedList<Player>();
        playerList.stream()
            .sorted((a, b) -> { return Integer.compare(a.getrankScore(), b.getrankScore());});
        
        @SuppressWarnings("unchecked")
        List<Player> playerReverse = (ArrayList<Player>) playerList.clone();
        Collections.reverse(playerReverse);

        for (Player goodPlayer : playerList) {
            for (Player badPlayer : playerReverse) {
                if(goodPlayer.getId() != badPlayer.getId() && !blackList.contains(goodPlayer) && !blackList.contains(badPlayer)){
                    Team t = new Team(goodPlayer, badPlayer);
                    t.setTournament(tournament);
                    teamList.add(t);
                    blackList.add(goodPlayer);
                    blackList.add(badPlayer);
                    break;
                }
            } 
        }

        return teamList;
    }
}