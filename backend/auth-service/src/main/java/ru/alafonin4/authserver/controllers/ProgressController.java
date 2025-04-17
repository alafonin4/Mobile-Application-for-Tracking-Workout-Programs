package ru.alafonin4.authserver.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alafonin4.authserver.pojo.ProgressByDate;
import ru.alafonin4.authserver.pojo.ProgressRequest;
import ru.alafonin4.authserver.services.JwtService;
import ru.alafonin4.authserver.services.WorkoutProgressService;

import java.util.List;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final WorkoutProgressService workoutProgressService;

    private final JwtService jwtService;

    @CrossOrigin(origins = "http://localhost:3001")
    @PostMapping("/getProgressByPeriod")
    public List<ProgressByDate> getProgressByPeriod(@Valid @RequestBody ProgressRequest request) {
        System.out.println(request.getToken());
        return workoutProgressService
                .getProgressByPeriod(jwtService.extractEmail(request.getToken()),
                        request.getCount());
    }

    @CrossOrigin(origins = "http://localhost:3001", methods = {RequestMethod.OPTIONS})
    @RequestMapping(value = "/getProgressByPeriod", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleGetOptionsRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "http://localhost:3001");
        return ResponseEntity.ok().headers(headers).build();
    }
}
