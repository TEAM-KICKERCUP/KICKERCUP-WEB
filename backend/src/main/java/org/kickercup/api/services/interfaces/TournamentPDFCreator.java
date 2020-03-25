//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: PDF Creator interface
//----------------------------------------------------------------------------------
package org.kickercup.api.services.interfaces;

import com.itextpdf.text.DocumentException;
import org.kickercup.api.services.impl.tournament.result.TournamentResult;

import java.io.IOException;
import java.nio.file.Path;

public interface TournamentPDFCreator {
    String create(Path folder, TournamentResult tournamentResult) throws DocumentException, IOException;
}
