//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): Johannes Schweer (Method adjustments)
//    Description: REST Controller for managing single matches (games)
//----------------------------------------------------------------------------------
package org.kickercup.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.kickercup.api.model.Match;
import org.kickercup.api.model.Team;
import org.kickercup.api.services.impl.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.model.Tournament;
import org.kickercup.api.repository.*;

@RestController
@RequestMapping("/api/v1")
public class MatchController {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchService matchService;

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/tournaments/{id}/matches")
    public List<Match> getAllMatches(@PathVariable(value = "id") final Long tournamentId) {
                final Tournament tournament = tournamentRepository.getOne(tournamentId);
                return tournament.getMatches();
        }

        @GetMapping("/tournaments/{tournamentId}/matches/{matchId}")
        public ResponseEntity<Match> getMatchByTournamentIdAndMatchId(@PathVariable final Long tournamentId,
                        @PathVariable final Long matchId) throws ResourceNotFoundException {
                final Tournament tournament = tournamentRepository.findById(tournamentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Tournament not found for this id :: " + tournamentId));
                final Match match = tournament.getMatches().stream().filter(m -> matchId.equals(m.getId())).findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Match not found for this id :: " + matchId));
                return ResponseEntity.ok().body(match);
        }

        @PostMapping("/tournaments/{tournamentId}/matches")
        public Match createMatch(@Valid @PathVariable final Long tournamentId, @Valid @RequestBody final Match match)
                        throws ResourceNotFoundException {
                final Tournament tournament = tournamentRepository.findById(tournamentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Tournament not found for this id :: " + tournamentId));
                match.setTournament(tournament);
                return matchRepository.save(match);
        }

        @PutMapping("/tournaments/{tournamentId}/matches/{matchId}")
        public ResponseEntity<Match> updateMatch(@PathVariable final Long tournamentId,
                        @PathVariable final Long matchId, @Valid @RequestBody final Match matchDetails)
                        throws ResourceNotFoundException {
                final Tournament tournament = tournamentRepository.findById(tournamentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Tournament not found for this id :: " + tournamentId));
                final Match match = tournament.getMatches().stream().filter(m -> matchId.equals(m.getId())).findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Match not found for this id :: " + matchId));
                match.setTournament(tournament);
                match.setFinished(null != matchDetails.isFinished() ? matchDetails.isFinished() : match.isFinished());
                match.setGoalsGuest(null != matchDetails.getGoalsGuest() ? matchDetails.getGoalsGuest()
                                : match.getGoalsGuest());
                match.setGoalsHome(null != matchDetails.getGoalsHome() ? matchDetails.getGoalsHome()
                                : match.getGoalsHome());

                final Match updatedMatch = matchService.updateMatch(match, tournament);
                return ResponseEntity.ok(updatedMatch);
        }

        @DeleteMapping("/tournaments/{tournamentId}/matches/{matchId}")
        public Map<String, Boolean> deleteMatch(@PathVariable final Long tournamentId, @PathVariable final Long matchId)
                        throws ResourceNotFoundException {
                final Tournament tournament = tournamentRepository.findById(tournamentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Tournament not found for this id :: " + tournamentId));

                final Match match = tournament.getMatches().stream().filter(m -> matchId.equals(m.getId())).findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Match not found for this id :: " + matchId));

                tournament.getMatches().remove(match);
                tournamentRepository.save(tournament);
                final Map<String, Boolean> response = new HashMap<>();
                response.put("deleted", Boolean.TRUE);
                return response;
        }

        @PostMapping("/tournaments/{tournamentId}/matches/{matchId}/teams")
        public Match addTeams(@Valid @PathVariable final Long tournamentId, @Valid @PathVariable final Long matchId,
                        @RequestParam final Long teamHomeId, @RequestParam final Long teamGuestId)
                        throws ResourceNotFoundException {
                final Tournament tournament = tournamentRepository.findById(tournamentId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Tournament not found for this id :: " + tournamentId));
                final Match match = tournament.getMatches().stream().filter(m -> matchId.equals(m.getId())).findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Match not found for this id :: " + matchId));
                final Team teamHome = teamRepository.findById(teamHomeId).orElseThrow(
                                () -> new ResourceNotFoundException("Team not found for this id :: " + teamHomeId));
                final Team teamGuest = teamRepository.findById(teamGuestId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found for this id :: " + teamGuestId));

        match.setTeamHome(teamHome);
        match.setTeamGuest(teamGuest);
        return matchRepository.save(match);
    }
}
