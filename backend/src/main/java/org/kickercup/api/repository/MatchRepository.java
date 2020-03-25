//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Match Repository for Jpa
//----------------------------------------------------------------------------------
package org.kickercup.api.repository;

import org.kickercup.api.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}
