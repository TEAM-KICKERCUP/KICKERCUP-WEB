//----------------------------------------------------------------------------------
//     Created By: Christopher Heid
// Contributor(s): None
//    Description: User Repository for Jpa
//----------------------------------------------------------------------------------
package org.kickercup.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.kickercup.api.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);

}
