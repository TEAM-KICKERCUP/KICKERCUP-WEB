//----------------------------------------------------------------------------------
//     Created By: Johannes Schweer
// Contributor(s): None
//    Description: GameMode Interface, this must be used for additional GameMode implementations
//----------------------------------------------------------------------------------
package org.kickercup.api.services.interfaces;

import org.kickercup.api.exception.RessourceValueViolationException;
import org.kickercup.api.model.*;
import org.kickercup.api.services.impl.tournament.result.table.ResultTableEntry;
import javax.activation.UnsupportedDataTypeException;
import java.util.List;
import java.util.Map;

public interface IGameModeService {
    public Tournament generateNewMatches (Tournament tournament) throws UnsupportedDataTypeException, RessourceValueViolationException;
    public boolean isTeamListValid(List<Team> teamList);
    public List<ResultTableEntry> sortWinningParticipants(Map<Long, ResultTableEntry> entries);
}