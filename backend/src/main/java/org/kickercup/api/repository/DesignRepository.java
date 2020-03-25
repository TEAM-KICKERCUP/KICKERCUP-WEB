//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: Design Repository for Jpa
//----------------------------------------------------------------------------------
package org.kickercup.api.repository;

import org.kickercup.api.model.Design;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesignRepository extends JpaRepository<Design, Long> {
    Optional<Design> findByUserId(long UserId);
}
