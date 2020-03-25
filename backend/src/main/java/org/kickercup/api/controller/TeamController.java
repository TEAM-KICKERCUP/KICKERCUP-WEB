//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): Johannes Schweer
//    Description: REST Controller for creation and usage of teams (one or two players)
//----------------------------------------------------------------------------------
package org.kickercup.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.kickercup.api.model.*;
import org.kickercup.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.kickercup.api.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1")
public class TeamController {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private org.kickercup.api.repository.PlayerRepository playerRepository;

    @GetMapping("/tournaments/{id}/teams")
    public List<Team> getAllTeams(
            @PathVariable(value = "id") Long tournamentId
    ) throws ResourceNotFoundException {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found for this id :: " + tournamentId));
        return tournament.getTeams();
    }

    @GetMapping("/tournaments/{tournamentId}/teams/{teamId}")
    public ResponseEntity<Team> getTeamByTournamentIdAndTeamId(
            @PathVariable Long tournamentId,
            @PathVariable Long teamId
    ) throws ResourceNotFoundException {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found for this id :: " + tournamentId));
        Team team = tournament
                .getTeams()
                .stream()
                .filter(t -> t.getId() == teamId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Team not found for this id :: " + teamId));

        return ResponseEntity.ok().body(team);
    }

    @PostMapping("/tournaments/{tournamentId}/teams")
    public Team createTeam (
            @Valid @PathVariable Long tournamentId,
            @Valid @RequestBody Team team
    ) throws ResourceNotFoundException {
         Tournament tournament = tournamentRepository.findById(tournamentId)
                 .orElseThrow(() -> new ResourceNotFoundException("Tournament not found for this id :: " + tournamentId));
        team.setTournament(tournament);
        return teamRepository.save(team);
    }

    @PutMapping("/tournaments/{tournamentId}/teams/{teamId}")
    public ResponseEntity<Team> updateTeam (
            @PathVariable Long tournamentId,
            @PathVariable Long teamId,
            @Valid @RequestBody Team teamDetails
    ) throws ResourceNotFoundException {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found for this id :: " + tournamentId));

        Team team = tournament
                .getTeams()
                .stream()
                .filter(t -> t.getId() == teamId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Team not found for this id :: " + teamId));

        team.setPlayerLeft(teamDetails.getPlayerLeft());
        team.setPlayerRight(teamDetails.getPlayerRight());

        final Team updatedTeam = teamRepository.save(team);
        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/tournaments/{tournamentId}/teams/{teamId}")
    public Map<String, Boolean> deleteTeam (
            @PathVariable Long tournamentId,
            @PathVariable Long teamId
    ) throws ResourceNotFoundException {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found for this id :: " + tournamentId));

        Team team = tournament
                .getTeams()
                .stream()
                .filter(t -> t.getId() == teamId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Team not found for this id :: " + teamId));

        teamRepository.delete(team);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @PostMapping("/tournaments/{tournamentId}/teams/{teamId}/players")
    public Team addPlayers (
            @RequestParam Long playerLeftId,
            @RequestParam(required = false) Long playerRightId,
            @Valid @PathVariable Long tournamentId,
            @Valid @PathVariable Long teamId
    ) throws ResourceNotFoundException {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found for this id :: " + tournamentId));
        Player playerLeft = playerRepository.findById(playerLeftId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerLeftId));
        Team team = tournament
                .getTeams()
                .stream()
                .filter(t -> t.getId() == teamId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Team not found for this id :: " + teamId));
        team.setPlayerLeft(playerLeft);
        if (null != playerRightId) {
            Player playerRight = playerRepository.findById(playerRightId)
                    .orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerRightId));
            team.setPlayerRight(playerRight);
        }
        return teamRepository.save(team);
    }
}
