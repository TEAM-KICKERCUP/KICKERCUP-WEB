//----------------------------------------------------------------------------------
//     Created By: Christopher Heid
// Contributor(s): Jonas Jahns (Refctoring Data Model)
//    Description: REST Controller for playing tournaments
//----------------------------------------------------------------------------------
package org.kickercup.api.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.activation.UnsupportedDataTypeException;
import javax.validation.Valid;
import com.itextpdf.text.DocumentException;
import org.kickercup.api.FileServeConfig;
import org.kickercup.api.model.User;
import org.kickercup.api.repository.UserRepository;
import org.kickercup.api.services.impl.tournament.pdf.PDFResultPathResponse;
import org.kickercup.api.services.impl.tournament.pdf.ResultITextPDFCreator;
import org.kickercup.api.services.impl.tournament.result.TournamentResult;
import org.kickercup.api.services.impl.tournament.result.TournamentResultBuilderImpl;
import org.kickercup.api.services.interfaces.TournamentResultBuilder;
import org.kickercup.api.services.impl.TeamService;
import org.kickercup.api.services.impl.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.exception.RessourceValueViolationException;
import org.kickercup.api.model.Player;
import org.kickercup.api.model.Team;
import org.kickercup.api.model.Tournament;
import org.kickercup.api.repository.TournamentRepository;

@RestController
@RequestMapping("/api/v1")
public class TournamentController {

	//private static final Logger LOGGER = Logger.getLogger(TournamentController.class.getName());

	@Autowired
	private TournamentService tournamentService;

	@Autowired
	private TournamentRepository tournamentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamService teamService;

	@GetMapping("/tournaments")
	public List<Tournament> getAllTournaments(@RequestParam(name = "userId", required = false) Long userId) {
		if (null == userId) {
			return tournamentRepository.findAll();
		} else {
			User user = userRepository.getOne(userId);
			return tournamentRepository.getTournamentsByUser(user);
		}
	}

	@GetMapping("/tournaments/{id}")
	public ResponseEntity<Tournament> getTournamentById(@PathVariable(value = "id") Long tournamentId)
			throws ResourceNotFoundException {
		Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(
				() -> new ResourceNotFoundException("Tournament not found for this id :: " + tournamentId));
		return ResponseEntity.ok().body(tournament);
	}

	@PostMapping("/tournaments")
	public Tournament createTournament(@Valid @RequestParam Long userId, @Valid @RequestBody Tournament tournament)
			throws ResourceNotFoundException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		tournament.setIsFinished(false);
		tournament.setIsStarted(false);
		tournament.setUser(user);
		return tournamentRepository.save(tournament);
	}

	@PutMapping("/tournaments/{id}")
	public ResponseEntity<Tournament> updateTournament(@PathVariable(value = "id") Long TournamentId,
			@Valid @RequestBody Tournament TournamentDetails)
			throws ResourceNotFoundException, UnsupportedDataTypeException, RessourceValueViolationException {
		Tournament tournament = tournamentRepository.findById(TournamentId).orElseThrow(
				() -> new ResourceNotFoundException("Tournament not found for this id :: " + TournamentId));

		tournament.setName(null != TournamentDetails.getName() ? TournamentDetails.getName() : tournament.getName());
		tournament.setGamemode(null != TournamentDetails.getGamemode() ? TournamentDetails.getGamemode(): tournament.getGamemode());
		tournament.setAmountGoals(null != TournamentDetails.getAmountGoals() ? TournamentDetails.getAmountGoals() : tournament.getAmountGoals());
		tournament.setAmountSets(null != TournamentDetails.getAmountSets() ? TournamentDetails.getAmountSets() : tournament.getAmountSets());
		tournament.setIsRanked(null != TournamentDetails.getIsRanked() ? TournamentDetails.getIsRanked() : tournament.getIsRanked());
		tournament.setIsFinished(null != TournamentDetails.getIsFinished() ? TournamentDetails.getIsFinished() : tournament.getIsFinished());
		tournament.setIsStarted(null != TournamentDetails.getIsStarted() ? TournamentDetails.getIsStarted() : tournament.getIsStarted());
		final Tournament updatedTournament = tournamentService.updateTournament(tournament);
		return ResponseEntity.ok(updatedTournament);
	}
     
	@DeleteMapping("/tournaments/{id}")
	public Map<String, Boolean> deleteTournament(@PathVariable(value = "id") Long TournamentId)
			throws ResourceNotFoundException {
		Tournament Tournament = tournamentRepository.findById(TournamentId).orElseThrow(
				() -> new ResourceNotFoundException("Tournament not found for this id :: " + TournamentId));

		tournamentRepository.delete(Tournament);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@GetMapping("/tournaments/{id}/result")
	public ResponseEntity<TournamentResult> getTournamentResult(@PathVariable(value = "id") Long tournamentId,
												@RequestParam(required = false, value="locale") String locale) throws ResourceNotFoundException, InstantiationException {
		Tournament tournament = tournamentRepository.findById(tournamentId)
				.orElseThrow(() -> new ResourceNotFoundException("Tournament not found for this id :: " + tournamentId));

		TournamentResultBuilder builder = new TournamentResultBuilderImpl();
		if (!tournament.getIsFinished()) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }
		return new ResponseEntity<>(builder.build(tournament, getLocaleFromString(locale)), HttpStatus.OK);
	}

	@GetMapping("/tournaments/{id}/result/pdf")
	public ResponseEntity<PDFResultPathResponse> getTournamentResultPDF(@PathVariable(value = "id") Long tournamentId,
														@RequestParam(required = false, value="locale") String locale) throws ResourceNotFoundException, InstantiationException, java.io.IOException, DocumentException {
		Tournament tournament = tournamentRepository.findById(tournamentId)
				.orElseThrow(() -> new ResourceNotFoundException("Tournament not found for this id :: " + tournamentId));

		TournamentResultBuilder builder = new TournamentResultBuilderImpl();
		if (!tournament.getIsFinished()) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }

		TournamentResult result = builder.build(tournament, getLocaleFromString(locale));

		Path folder;
		folder = Paths.get(FileServeConfig.getTempPath());
		ResultITextPDFCreator creator = new ResultITextPDFCreator();
		PDFResultPathResponse response = new PDFResultPathResponse(creator.create(folder, result));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/tournaments/{id}/teamMaking")
	public List<Team> teamMaking(@PathVariable(value = "id") Long tournamentId, 
		@Valid @RequestBody ArrayList<Player> playerListDetails) throws ResourceNotFoundException, RessourceValueViolationException {

			Tournament tournament = tournamentRepository.findById(tournamentId)
			.orElseThrow(() -> new ResourceNotFoundException("Tournament not found for this id :: " + tournamentId));

		return teamService.generateTeams(tournament, playerListDetails);
	}

	private Locale getLocaleFromString(String localeText) {
		Locale locale;
		if (null != localeText && !localeText.isEmpty()) {
			try {
				locale = new Locale(localeText);
			} catch (Exception e) {
				e.printStackTrace();
				locale = Locale.ENGLISH;
			}
		} else {
			locale = Locale.ENGLISH;
		}
		return locale;
	}
}
