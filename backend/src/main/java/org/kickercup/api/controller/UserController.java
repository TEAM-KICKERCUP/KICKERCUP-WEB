//----------------------------------------------------------------------------------
//     Created By: Christopher Heid
// Contributor(s): Lucas Wierer (Method enhancement for Design Component)
//    Description: REST Controller for manging user data
//----------------------------------------------------------------------------------
package org.kickercup.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import org.kickercup.api.model.Design;
import org.kickercup.api.repository.DesignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.model.User;
import org.kickercup.api.repository.UserRepository;
import org.kickercup.api.services.impl.PasswordService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
	@Autowired
	private UserRepository UserRepository;

	@Autowired
	private DesignRepository designRepository;

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return UserRepository.findAll();
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long UserId) throws ResourceNotFoundException {
		User User = UserRepository.findById(UserId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + UserId));
		return ResponseEntity.ok().body(User);
	}

	@PostMapping("/users")
	public User createUser(@Valid @RequestBody User User) {
		User.setPassword(PasswordService.hashPassword(User.getPassword()));
		UserRepository.save(User);
		// Create default Design
		Design design = new Design(User);
		designRepository.save(design);

		return User;
	}

	@PostMapping("/users/auth/")
	public User authUser(@Valid @RequestBody User User) {

		try {
			Optional<User> toLogin = UserRepository.findByUsername(User.getUsername());
			if (PasswordService.checkPassword(User.getPassword(), toLogin.get().getPassword())) {
				toLogin.get().setPassword("");
				return toLogin.get();
			} else
				return new User();

		} catch (Exception e) {
			return new User();
		}

	}

	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long UserId,
			@Valid @RequestBody User UserDetails) throws ResourceNotFoundException {
		User User = UserRepository.findById(UserId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + UserId));

		if (UserDetails.getUsername() != null) {
			User.setUsername(UserDetails.getUsername());
		}
		if (UserDetails.getPassword() != null) {
			User.setPassword(PasswordService.hashPassword(UserDetails.getPassword()));
		}
		if (UserDetails.getFirstName() != null) {
			User.setFirstName(UserDetails.getFirstName());
		}
		if (UserDetails.getLastName() != null) {
			User.setLastName(UserDetails.getLastName());
		}
		if (UserDetails.getEmail() != null) {
			User.setEmail(UserDetails.getEmail());
		}
		final User updatedUser = UserRepository.save(User);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/users/{id}")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long UserId) throws ResourceNotFoundException {
		User User = UserRepository.findById(UserId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + UserId));

		UserRepository.delete(User);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
