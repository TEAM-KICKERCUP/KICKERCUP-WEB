//----------------------------------------------------------------------------------
//     Created By: Christopher Heid
// Contributor(s): Lucas Wierer (Created queries for statistic)
//    Description: Tournament Repository for Jpa
//----------------------------------------------------------------------------------
package org.kickercup.api.repository;

import org.kickercup.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.kickercup.api.model.Tournament;
import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long>{
    List<Tournament> getTournamentsByUser(User user);
    long countByUser(User user);

    @Query(value = "SELECT count(*) FROM game_match as m" +
            " LEFT JOIN tournament as t" +
            " ON m.tournament_id = t.id WHERE t.user_id = ?1", nativeQuery = true)
    long countMatches(long userId);

    @Query(value = "SELECT SUM(goals_guest) + SUM(goals_home) FROM game_match as m" +
            " LEFT JOIN tournament as t" +
            " ON m.tournament_id = t.id WHERE t.user_id = ?1", nativeQuery = true)
    long countGoals(long userId);
}
