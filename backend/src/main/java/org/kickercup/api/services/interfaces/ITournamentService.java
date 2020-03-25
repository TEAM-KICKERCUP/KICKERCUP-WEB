//----------------------------------------------------------------------------------
//     Created By: Johannes Schweer
// Contributor(s): None
//    Description: Tournament Interface
//----------------------------------------------------------------------------------
package org.kickercup.api.services.interfaces;

import org.kickercup.api.model.Tournament;
import java.util.List;
import javax.activation.UnsupportedDataTypeException;
import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.exception.RessourceValueViolationException;

public interface ITournamentService{
    public Tournament createTournament(Tournament tournament) throws UnsupportedDataTypeException;
    public List<Tournament> getAllTournaments();
    public Tournament getTournamentsById(long id);
    public Tournament updateTournament(Tournament tournament) throws ResourceNotFoundException, UnsupportedDataTypeException, RessourceValueViolationException;
    public void deleteTournament(Tournament tournament);
}