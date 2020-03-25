//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: GameMode Interface
//----------------------------------------------------------------------------------
package org.kickercup.api.services.interfaces;

import org.kickercup.api.model.Statistic;
import org.kickercup.api.model.User;

public interface IStatisticService {
    Statistic GetStats(User user);
}
