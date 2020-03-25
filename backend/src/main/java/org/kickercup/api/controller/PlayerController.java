//----------------------------------------------------------------------------------
//     Created By: Christopher Heid
// Contributor(s): Jonas Jahns (Refctoring Data Model)
//    Description: REST Controller for managing players
//----------------------------------------------------------------------------------
package org.kickercup.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.kickercup.api.model.User;
import org.kickercup.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.model.Player;
import org.kickercup.api.repository.PlayerRepository;

@RestController
@RequestMapping("/api/v1")
public class PlayerController {
	@Autowired
	private PlayerRepository PlayerRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/players")
	public ResponseEntity<List<Player>> getAllPlayers(@RequestParam(name = "userId", required = false) Long userId) {
		if (null == userId) {
			return ResponseEntity.ok().body(PlayerRepository.findAll());
		} else {
			User user = userRepository.getOne(userId);
			List<Player> player = PlayerRepository.getPlayersByUser(user); 
			return ResponseEntity.ok().body(player); 
		}
	}

	@GetMapping("/players/{id}")
	public ResponseEntity<Player> getPlayerById(@PathVariable(value = "id") Long PlayerId)
			throws ResourceNotFoundException {
		Player Player = PlayerRepository.findById(PlayerId)
				.orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + PlayerId));
		return ResponseEntity.ok().body(Player);
	}

	@PostMapping("/players")
	public Player createPlayer(@RequestParam Long userId, @Valid @RequestBody Player player)
			throws ResourceNotFoundException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		player.setUser(user);
		return PlayerRepository.save(player);
	}

	@PutMapping("/players/{id}")
	public ResponseEntity<Player> updatePlayer(@PathVariable(value = "id") Long PlayerId,
			@Valid @RequestBody Player PlayerDetails) throws ResourceNotFoundException {
		Player Player = PlayerRepository.findById(PlayerId)
				.orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + PlayerId));

		Player.setEmailId(null != PlayerDetails.getEmailId() ? PlayerDetails.getEmailId() : Player.getEmailId());
		Player.setLastName(null != PlayerDetails.getLastName() ? PlayerDetails.getLastName() : Player.getLastName());
		Player.setFirstName(null != PlayerDetails.getFirstName() ? PlayerDetails.getFirstName() : Player.getFirstName());
		Player.setUser(null != PlayerDetails.getUser() ? PlayerDetails.getUser() : Player.getUser());
		final Player updatedPlayer = PlayerRepository.save(Player);
		return ResponseEntity.ok(updatedPlayer);
	}

	@DeleteMapping("/players/{id}")
	public Map<String, Boolean> deletePlayer(@PathVariable(value = "id") Long PlayerId)
			throws ResourceNotFoundException {
		Player Player = PlayerRepository.findById(PlayerId)
				.orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + PlayerId));

		PlayerRepository.delete(Player);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
