//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Team Repository for Jpa
//----------------------------------------------------------------------------------
package org.kickercup.api.repository;

import org.kickercup.api.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
