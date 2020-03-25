//----------------------------------------------------------------------------------
//     Created By: Johannes Schweer
// Contributor(s): None
//    Description: Service for handling matches during game flow
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl;

import org.kickercup.api.model.Gamemode;
import org.kickercup.api.model.Match;
import org.kickercup.api.model.MatchResult;
import org.kickercup.api.model.Tournament;
import org.kickercup.api.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    @Autowired
    MatchRepository matchRepository;

    public Match updateMatch(Match match, Tournament tournament) {
        int amountGoals = tournament.getAmountGoals();
        if (match.getGoalsGuest() != match.getGoalsHome()
                && (match.getGoalsGuest() == amountGoals || match.getGoalsHome() == amountGoals)) {
            match.setFinished(true);
            if (tournament.getIsRanked()) {
                setRankedScore(match, tournament);
            }
        }

        return matchRepository.save(match);
    }

    private void setRankedScore(Match match, Tournament tournament) {
        double averageEloHome;
        double averageEloGuest;

        if (tournament.getGamemode() != Gamemode.RankedSoloMatch) {
            averageEloHome = (match.getTeamHome().getPlayerLeft().getrankScore()
                    + match.getTeamHome().getPlayerRight().getrankScore()) / 2;
            averageEloGuest = (match.getTeamGuest().getPlayerLeft().getrankScore()
                    + match.getTeamGuest().getPlayerRight().getrankScore()) / 2;
            if (match.getGoalsHome() > match.getGoalsGuest()) {
                match.getTeamHome().getPlayerLeft().updateRankScore(averageEloGuest, MatchResult.WON);
                match.getTeamHome().getPlayerRight().updateRankScore(averageEloGuest, MatchResult.WON);

                match.getTeamGuest().getPlayerLeft().updateRankScore(averageEloHome, MatchResult.LOST);
                match.getTeamGuest().getPlayerRight().updateRankScore(averageEloHome, MatchResult.LOST);
            } else {
                match.getTeamHome().getPlayerLeft().updateRankScore(averageEloGuest, MatchResult.LOST);
                match.getTeamHome().getPlayerRight().updateRankScore(averageEloGuest, MatchResult.LOST);

                match.getTeamGuest().getPlayerLeft().updateRankScore(averageEloHome, MatchResult.WON);
                match.getTeamGuest().getPlayerRight().updateRankScore(averageEloHome, MatchResult.WON);
            }
        } else {
            averageEloHome = match.getTeamHome().getPlayerLeft().getrankScore();
            averageEloGuest = match.getTeamHome().getPlayerLeft().getrankScore();

            if (match.getGoalsHome() > match.getGoalsGuest()) {
                match.getTeamHome().getPlayerLeft().updateRankScore(averageEloGuest, MatchResult.WON);
                match.getTeamGuest().getPlayerLeft().updateRankScore(averageEloHome, MatchResult.LOST);
            } else {
                match.getTeamHome().getPlayerLeft().updateRankScore(averageEloGuest, MatchResult.LOST);
                match.getTeamGuest().getPlayerLeft().updateRankScore(averageEloHome, MatchResult.WON);
            }
        }
    }
}