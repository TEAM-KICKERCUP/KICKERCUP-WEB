//----------------------------------------------------------------------------------
//     Created By: Johannes Schweer
// Contributor(s): Moritz Lugbauer
//    Description: Service for handling the tournament during game flow
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl;

import java.util.List;
import javax.activation.UnsupportedDataTypeException;
import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.exception.RessourceValueViolationException;
import org.kickercup.api.model.Tournament;
import org.kickercup.api.model.User;
import org.kickercup.api.repository.TournamentRepository;
import org.kickercup.api.repository.UserRepository;
import org.kickercup.api.services.impl.gamemode.GameModeFactory;
import org.kickercup.api.services.interfaces.IGameModeService;
import org.kickercup.api.services.interfaces.ITournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TournamentService implements ITournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private UserRepository userRepository;


    // #region Interface Implementation
    @Override
    public Tournament updateTournament(Tournament tournament)
            throws ResourceNotFoundException, UnsupportedDataTypeException, RessourceValueViolationException {
        // check for new Matches
        if (tournament.getIsStarted() && !tournament.getIsFinished()) {
            final IGameModeService gameModeService = GameModeFactory.getObject(tournament);
            tournament = gameModeService.generateNewMatches(tournament);
           // matchRepository.saveAll(tournament.getMatches());
        }

        // save new Tournament to DB and send back to User
        return this.tournamentRepository.save(tournament);
    }

    @Override
    public Tournament createTournament(final Tournament tournament) throws UnsupportedDataTypeException {
        return tournamentRepository.save(tournament);
    }

    @Override
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    @Override
    public Tournament getTournamentsById(final long id) {
        return tournamentRepository.getOne(id);
    }

    public List<Tournament> getTournamentsByUser(final long userId) {
        User user = userRepository.getOne(userId);
        return tournamentRepository.getTournamentsByUser(user);
    }

    @Override
    public void deleteTournament(Tournament tournament) {
        tournamentRepository.deleteById(tournament.getId());
    }

    // #endregion

}