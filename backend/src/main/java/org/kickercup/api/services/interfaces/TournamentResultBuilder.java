//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Tournament Result Builder
//----------------------------------------------------------------------------------
package org.kickercup.api.services.interfaces;

import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.model.Tournament;
import org.kickercup.api.services.impl.tournament.result.TournamentResult;
import java.util.Locale;

public interface TournamentResultBuilder {
    TournamentResult build(Tournament tournament) throws ResourceNotFoundException, InstantiationException;
    TournamentResult build(Tournament tournament, Locale locale) throws ResourceNotFoundException, InstantiationException;
}
