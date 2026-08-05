import com.gym.dao.TrainingDao;
import com.gym.exceptions.ValidationException;
import com.gym.jms.TrainerWorkloadProducer;
import com.gym.models.*;
import com.gym.services.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    private TrainingDao trainingDao;
    private TrainingService service;
    private TrainerWorkloadProducer trainerWorkloadProducer;

    @BeforeEach
    void setUp() {
        trainingDao = mock(TrainingDao.class);
        trainerWorkloadProducer = mock(TrainerWorkloadProducer.class);

        service = new TrainingService(
                trainingDao,
                trainerWorkloadProducer
        );
    }

    @Test
    void createTraining() {
        Training training = new Training();
        training.setTrainee(new Trainee());
        training.setTrainer(new Trainer());
        training.setTrainingType(new TrainingType());
        training.setTrainingName("Basketball");
        training.setDuration(60);
        training.setTrainingDate(java.time.LocalDate.now());

        Training result = service.create(training);

        assertEquals("Basketball", result.getTrainingName());
        verify(trainingDao).save(training);
    }

    @Test
    void shouldThrowIfDurationInvalid() {
        Training training = new Training();
        training.setTrainee(new Trainee());
        training.setTrainer(new Trainer());
        training.setTrainingType(new TrainingType());
        training.setTrainingName("Basketball");
        training.setDuration(0);

        assertThrows(RuntimeException.class, () -> service.create(training));
    }

    @Test
    void shouldThrowIfNoTrainer() {
        Training training = new Training();
        training.setTrainee(new Trainee());
        training.setTrainingType(new TrainingType());
        training.setTrainingName("Basketball");
        training.setDuration(60);
        training.setTrainingDate(java.time.LocalDate.now());

        assertThrows(RuntimeException.class, () -> service.create(training));
    }

    @Test
    void shouldThrowIfNoName() {
        Training training = new Training();
        training.setTrainee(new Trainee());
        training.setTrainer(new Trainer());
        training.setTrainingType(new TrainingType());
        training.setDuration(60);
        training.setTrainingDate(LocalDate.now());

        assertThrows(ValidationException.class, () -> service.create(training));
    }

    @Test
    void shouldThrowIfNoDate() {
        Training training = new Training();
        training.setTrainee(new Trainee());
        training.setTrainer(new Trainer());
        training.setTrainingType(new TrainingType());
        training.setTrainingName("Basketball");
        training.setDuration(60);

        assertThrows(ValidationException.class, () -> service.create(training));
    }

    @Test
    void getTraineeTrainings() {
        String username = "John";
        LocalDate from = LocalDate.of(2026, 5, 25);
        LocalDate to = LocalDate.of(2026, 6, 25);
        String trainer = "Michael";
        String type = "Basketball";

        when(trainingDao
                .findTraineeTrainings(username, from, to, trainer, type))
                .thenReturn(List.of(new Training()));

        List<Training> result = service.getTraineeTrainings(username, from, to, trainer, type);

        assertEquals(1, result.size());

        verify(trainingDao).findTraineeTrainings(username, from, to, trainer, type);
    }

    @Test
    void getTrainerTrainings() {
        String username = "Michael";
        LocalDate from = LocalDate.of(2026, 5, 25);
        LocalDate to = LocalDate.of(2026, 6, 25);
        String trainee = "John";

        when(trainingDao.findTrainerTrainings(username, from, to, trainee))
                .thenReturn(List.of(new Training()));

        List<Training> result = service.getTrainerTrainings(username, from, to, trainee);

        assertEquals(1, result.size());

        verify(trainingDao).findTrainerTrainings(username, from, to, trainee);
    }
}