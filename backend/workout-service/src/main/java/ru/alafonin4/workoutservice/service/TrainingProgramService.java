package ru.alafonin4.workoutservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public TrainingProgram createProgram(TrainingProgram program) {
        normalizeProgram(program);
        return trainingProgramRepository.save(program);
    }

    @Transactional(readOnly = true)
    public TrainingProgram getProgramById(Long id) {
        TrainingProgram program = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training program not found with id " + id));
        initializeProgram(program);
        return program;
    }

    @Transactional(readOnly = true)
    public List<TrainingProgram> getProgramsByUserId(Long userId) {
        List<TrainingProgram> programs = trainingProgramRepository.findByUserIdOrderByIdDesc(userId);
        programs.forEach(this::initializeProgram);
        return programs;
    }

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
        }).orElseThrow(() -> new RuntimeException("Training program not found with id " + id));
    }

    public void deleteProgram(Long id) {
        trainingProgramRepository.deleteById(id);
    }

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

    private void initializeProgram(TrainingProgram program) {
        program.getTrainingDays().size();
        for (TrainingDay day : program.getTrainingDays()) {
            day.getExercises().size();
        }
    }
}
