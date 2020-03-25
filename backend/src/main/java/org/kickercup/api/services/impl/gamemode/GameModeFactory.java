//----------------------------------------------------------------------------------
//     Created By: Johannes Schweer
// Contributor(s): None
//    Description: Returns a GameMode (e.g. Single K.O.) implementation
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.gamemode;

import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.model.Gamemode;
import org.kickercup.api.model.Tournament;
import org.kickercup.api.services.interfaces.IGameModeService;

public class GameModeFactory {

    public static IGameModeService getObject(final Tournament tournament) throws ResourceNotFoundException {
        IGameModeService gm;

        final Gamemode gamemode = tournament.getGamemode();
        // use the correct GameMode Imeplementation
        switch (gamemode) {
        case RankedSoloMatch:
            gm = new RankedSoloMatchService();
            break;

        case RankedTeamMatch:
            gm = new RankedTeamMatchService();
            break;

        case SingleKO:
            gm = new SingleKoService();
            break;

        default:
            throw new ResourceNotFoundException("Gamemode is not supported");
        }
        return gm;
    }
}