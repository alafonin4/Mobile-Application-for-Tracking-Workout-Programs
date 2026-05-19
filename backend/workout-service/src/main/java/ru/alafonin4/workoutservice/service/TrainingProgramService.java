package ru.alafonin4.workoutservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.alafonin4.workoutservice.model.TrainingDay;
import ru.alafonin4.workoutservice.model.TrainingDayExercise;
import ru.alafonin4.workoutservice.model.TrainingProgram;
import ru.alafonin4.workoutservice.repository.TrainingProgramRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrainingProgramService {

    @Autowired
    private TrainingProgramRepository trainingProgramRepository;

    /**
     * Creates a new program.
     * @param program training program being processed
     * @return result of the operation
     */
    public TrainingProgram createProgram(TrainingProgram program) {
        normalizeProgram(program);
        return trainingProgramRepository.save(program);
    }

    /**
     * Returns the program by id.
     * @param id identifier of the target record
     * @return result of the operation
     */
    @Transactional(readOnly = true)
    public TrainingProgram getProgramById(Long id) {
        TrainingProgram program = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id " + id));
        initializeProgram(program);
        return program;
    }

    /**
     * Returns the programs by user id.
     * @param userId identifier of the user
     * @return prepared list with the requested data
     */
    @Transactional(readOnly = true)
    public List<TrainingProgram> getProgramsByUserId(Long userId) {
        List<TrainingProgram> programs = trainingProgramRepository.findByUserIdOrderByIdDesc(userId);
        programs.forEach(this::initializeProgram);
        return programs;
    }

    /**
     * Updates the program.
     * @param id identifier of the target record
     * @param updatedProgram updated training program state
     * @return result of the operation
     */
    public TrainingProgram updateProgram(Long id, TrainingProgram updatedProgram) {
        return trainingProgramRepository.findById(id).map(existingProgram -> {
            existingProgram.setUserId(updatedProgram.getUserId());
            existingProgram.setName(updatedProgram.getName());
            existingProgram.setDescription(updatedProgram.getDescription());
            existingProgram.getTrainingDays().clear();
            if (updatedProgram.getTrainingDays() != null) {
                existingProgram.getTrainingDays().addAll(updatedProgram.getTrainingDays());
            }
            normalizeProgram(existingProgram);
            return trainingProgramRepository.save(existingProgram);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id " + id));
    }

    /**
     * Deletes the program.
     * @param id identifier of the target record
     */
    public void deleteProgram(Long id) {
        if (!trainingProgramRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id " + id);
        }
        trainingProgramRepository.deleteById(id);
    }

    /**
     * Normalizes the program.
     * @param program training program being processed
     */
    private void normalizeProgram(TrainingProgram program) {
        if (program.getTrainingDays() == null) {
            program.setTrainingDays(new ArrayList<>());
            return;
        }

        for (TrainingDay day : program.getTrainingDays()) {
            day.setTrainingProgram(program);

            if (day.getExercises() == null) {
                day.setExercises(new ArrayList<>());
                continue;
            }

            for (TrainingDayExercise exercise : day.getExercises()) {
                exercise.setTrainingDay(day);
            }
        }
    }

    /**
     * InitializeProgram.
     * @param program training program being processed
     */
    private void initializeProgram(TrainingProgram program) {
        program.getTrainingDays().size();
        for (TrainingDay day : program.getTrainingDays()) {
            day.getExercises().size();
        }
    }
}
