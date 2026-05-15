package ru.alafonin4.workoutservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alafonin4.workoutservice.dto.PersonalizationProfileResponse;
import ru.alafonin4.workoutservice.dto.ProgramAdaptationResponse;
import ru.alafonin4.workoutservice.service.PersonalizationService;

@RestController
@RequestMapping("/api/personalization")
public class PersonalizationController {

    @Autowired
    private PersonalizationService personalizationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<PersonalizationProfileResponse> getProfile(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(personalizationService.buildProfile(userId));
    }

    @GetMapping("/user/{userId}/program/{programId}")
    public ResponseEntity<ProgramAdaptationResponse> getProgramAdaptation(
            @PathVariable("userId") Long userId,
            @PathVariable("programId") Long programId
    ) {
        return ResponseEntity.ok(personalizationService.buildProgramAdaptation(userId, programId));
    }
}
