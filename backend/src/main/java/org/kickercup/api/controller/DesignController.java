//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): none
//    Description: REST Controller for managing users front-end design adjustments
//----------------------------------------------------------------------------------
package org.kickercup.api.controller;

import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.model.Design;
import org.kickercup.api.repository.DesignRepository;
import org.kickercup.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users/{userId}/designs")
public class DesignController {
    private DesignRepository designRepository;
    private UserRepository userRepository;

    @Autowired
    public DesignController(DesignRepository designRepository, UserRepository userRepository) {
        this.designRepository = designRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public Design ListByUser(@PathVariable long userId) throws ResourceNotFoundException {
        return designRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Did not find a design with id for user with id " + userId));
    }

    @PutMapping("/{id}")
    public Design Update(@PathVariable long userId, @PathVariable long id, @Valid @RequestBody Design design) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return designRepository.findById(id).map(d -> {
            d.setPrimaryColor(design.getPrimaryColor());
            d.setSecondaryColor(design.getSecondaryColor());
            return designRepository.save(d);
        }).orElseThrow(() -> new ResourceNotFoundException("DeisgnId " + id + " not found"));
    }
}
