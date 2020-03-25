//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: Service for user statistics
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.statistic;

import org.kickercup.api.model.Player;
import org.kickercup.api.model.Statistic;
import org.kickercup.api.model.User;
import org.kickercup.api.repository.PlayerRepository;
import org.kickercup.api.repository.TournamentRepository;
import org.kickercup.api.services.interfaces.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StatisticService implements IStatisticService {
    private PlayerRepository playerRepository;
    private TournamentRepository tournamentRepository;


    @Autowired
    public StatisticService(PlayerRepository playerRepository, TournamentRepository tournamentRepository) {
        this.playerRepository = playerRepository;
        this.tournamentRepository = tournamentRepository;

    }

    @Override
    public Statistic GetStats(User user) {
        long playersCount = playerRepository.countByUser(user);
        long tournamentsCount = tournamentRepository.countByUser(user);
        long matchesCount = tournamentRepository.countMatches(user.getId());
        long goalsCount = tournamentRepository.countGoals(user.getId());
        List<Player> players = playerRepository.getPlayersByUserOrderByRankScore(user);

        Statistic statistic = new Statistic(tournamentsCount, matchesCount, playersCount, goalsCount, players);

        return statistic;
    }
}
