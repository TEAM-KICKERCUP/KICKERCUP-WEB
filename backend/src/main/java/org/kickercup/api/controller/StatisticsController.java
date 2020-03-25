//----------------------------------------------------------------------------------
//     Created By: Lucas Wierer
// Contributor(s): None
//    Description: REST Controller which provides statistic information to the front-end
//----------------------------------------------------------------------------------
package org.kickercup.api.controller;

import org.kickercup.api.exception.ResourceNotFoundException;
import org.kickercup.api.model.Statistic;
import org.kickercup.api.model.User;
import org.kickercup.api.repository.UserRepository;
import org.kickercup.api.services.interfaces.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {
    IStatisticService statisticService;
    UserRepository userRepository;

    @Autowired
    public StatisticsController(IStatisticService statisticService, UserRepository userRepository) {
        this.statisticService = statisticService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Statistic> getStats(@RequestParam(name = "userId", required = true) Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        Statistic stats = statisticService.GetStats(user);
        return  ResponseEntity.ok().body(stats);
    }
}
