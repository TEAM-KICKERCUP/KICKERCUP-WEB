//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: Tournament Result Table
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.tournament.result;

import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.model.*;
import org.kickercup.api.services.impl.gamemode.GameModeFactory;
import org.kickercup.api.services.impl.localization.LabelService;
import org.kickercup.api.services.impl.tournament.result.table.ResultTable;
import org.kickercup.api.services.impl.tournament.result.table.ResultTableBuilder;
import org.kickercup.api.services.interfaces.IGameModeService;
import org.kickercup.api.services.interfaces.TournamentResultBuilder;
import java.text.DateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TournamentResultBuilderImpl implements TournamentResultBuilder {

    private static final Logger LOGGER = Logger.getLogger(TournamentResultBuilderImpl.class.getName());

    @Override
    public TournamentResult build(Tournament tournament) throws ResourceNotFoundException, InstantiationException {
        return build(tournament, Locale.ENGLISH);
    }

    @Override
    public TournamentResult build(Tournament tournament, Locale locale) throws ResourceNotFoundException, InstantiationException {
        if (null == tournament || null == tournament.getMatches() || tournament.getMatches().size() == 0) {
            LOGGER.log(Level.SEVERE, "The result for this tournament cannot be build, tournament id = "
                    + (null != tournament ? tournament.getId() : "null" ));
            throw new InstantiationException("Cannot operate on tournament");
        }

        if (!tournament.getIsFinished()) {
            LOGGER.log(Level.WARNING, "tournament is not finished yet, tournament id = " + tournament.getId());
        }

        // Get GameMode Service
        IGameModeService gameModeService;
        try {
            gameModeService = GameModeFactory.getObject(tournament);
        } catch (ResourceNotFoundException | NullPointerException e) {
            LOGGER.log(Level.SEVERE, "A GameModeService is required for building tournament results");
            throw new ResourceNotFoundException("GameMode is not available");
        }

        // Build title
        String title = buildTitle(tournament, locale);

        // Build table
        ResultTable table = buildTable(tournament, gameModeService, locale);

        // Build SubHeaders
        List<String> subHeaders = buildSubHeaders(tournament, locale);

        LOGGER.log(Level.INFO, "TournamentResult for Tournament " + tournament.getId() + "  was built");
        return new TournamentResult(title, subHeaders, table);
    }

    private String buildTitle(Tournament tournament, Locale locale) {
        return null == tournament.getName() ?
                LabelService.getString(locale, "tournament") : tournament.getName();
    }

    private ResultTable buildTable(Tournament tournament,
                                   IGameModeService gameModeService, Locale locale) throws InstantiationException {
        ResultTableBuilder builder = new ResultTableBuilder();
        return builder.buildTable(tournament, gameModeService, locale);
    }

    private List<String> buildSubHeaders(Tournament tournament, Locale locale) {
        List<String> result = new ArrayList<>();
        result.add(getIssueDate(locale));
        return result;
    }

    private String getIssueDate(Locale locale) {
        return DateFormat.getDateInstance(DateFormat.FULL, locale)
                .format(new Date());
    }
}
