//----------------------------------------------------------------------------------
//     Created By: Christopher Heid
// Contributor(s): Johannes Schweer (addded methods)
//    Description: Player Repository for Jpa
//----------------------------------------------------------------------------------
package org.kickercup.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.kickercup.api.model.Player;
import org.kickercup.api.model.User;;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> getPlayersByUser(User user);
    List<Player> getPlayersByUserOrderByRankScore(User user);
    long countByUser(User user);
}
