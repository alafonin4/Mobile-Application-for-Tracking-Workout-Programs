package ru.alafonin4.workoutservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alafonin4.workoutservice.model.TrainingDay;
import ru.alafonin4.workoutservice.model.TrainingProgram;
import ru.alafonin4.workoutservice.repository.TrainingProgramRepository;

import java.util.List;

@Service
public class TrainingProgramService {

    @Autowired
    private TrainingProgramRepository trainingProgramRepository;

    // Создание новой программы
    public TrainingProgram createProgram(TrainingProgram program) {
        if (program.getTrainingDays() != null) {
            for (TrainingDay day : program.getTrainingDays()) {
                day.setTrainingProgram(program);
            }
        }
        return trainingProgramRepository.save(program);
    }

    public TrainingProgram getProgramById(Long id) {
        return trainingProgramRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training program not found with id " + id));
    }

    public List<TrainingProgram> getProgramsByUserId(Long userId) {
        return trainingProgramRepository.findByUserId(userId);
    }

    public TrainingProgram updateProgram(Long id, TrainingProgram updatedProgram) {
        return trainingProgramRepository.findById(id).map(existingProgram -> {
            existingProgram.setName(updatedProgram.getName());
            existingProgram.setDescription(updatedProgram.getDescription());
            // Очистка и обновление списка дней программы
            existingProgram.getTrainingDays().clear();
            if (updatedProgram.getTrainingDays() != null) {
                for (TrainingDay day : updatedProgram.getTrainingDays()) {
                    day.setTrainingProgram(existingProgram);
                    existingProgram.getTrainingDays().add(day);
                }
            }
            return trainingProgramRepository.save(existingProgram);
        }).orElseThrow(() -> new RuntimeException("Training program not found with id " + id));
    }

    public void deleteProgram(Long id) {
        trainingProgramRepository.deleteById(id);
    }
}
