import com.gym.dao.TraineeDao;
import com.gym.dao.TrainerDao;
import com.gym.exceptions.ValidationException;
import com.gym.models.Trainee;
import com.gym.models.Trainer;
import com.gym.models.User;
import com.gym.services.TraineeService;
import com.gym.utils.PasswordGenerator;
import com.gym.utils.UsernameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    private TraineeDao traineeDao;
    private TrainerDao trainerDao;
    private UsernameGenerator usernameGenerator;
    private PasswordGenerator passwordGenerator;

    private TraineeService service;

    @BeforeEach
    void setUp() {
        traineeDao = mock(TraineeDao.class);
        trainerDao = mock(TrainerDao.class);
        usernameGenerator = mock(UsernameGenerator.class);
        passwordGenerator = mock(PasswordGenerator.class);

        service = new TraineeService(
                traineeDao,
                trainerDao,
                usernameGenerator,
                passwordGenerator
        );
    }

    @Test
    void shouldGenerateUsernameAndPassword() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Smith");

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(usernameGenerator.generate("John", "Smith"))
                .thenReturn("John.Smith");

        when(passwordGenerator.generate())
                .thenReturn("123456789");

        Trainee result = service.create(trainee);

        assertEquals("John.Smith", result.getUser().getUsername());
        assertEquals("123456789", result.getUser().getPassword());
        assertTrue(result.getUser().isActive());

        verify(traineeDao).save(trainee);
    }

    @Test
    void shouldThrowIfNoFirstName() {
        User user = new User();
        user.setLastName("Smith");

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        assertThrows(ValidationException.class, () -> service.create(trainee));
    }

    @Test
    void shouldUpdatePassword() {
        User user = new User();
        user.setPassword("old");

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(traineeDao.findByUsername("john"))
                .thenReturn(trainee);

        service.changePassword("john", "newPass");

        assertEquals("newPass", trainee.getUser().getPassword());
        verify(traineeDao).save(trainee);
    }

    @Test
    void shouldThrowIfAlreadyActive() {
        User user = new User();
        user.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(traineeDao.findByUsername("john"))
                .thenReturn(trainee);

        assertThrows(RuntimeException.class, () -> service.activate("john"));
    }

    @Test
    void shouldSetInactive() {
        User user = new User();
        user.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(traineeDao.findByUsername("john"))
                .thenReturn(trainee);

        service.deactivate("john");
        assertFalse(user.isActive());
    }

    @Test
    void shouldAssignTrainer() {
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();

        when(traineeDao.findByUsername("john"))
                .thenReturn(trainee);

        when(trainerDao.findById(1L))
                .thenReturn(trainer);

        service.assignTrainer("john", 1L);

        assertTrue(trainee.getTrainers().contains(trainer));
        assertTrue(trainer.getTrainees().contains(trainee));

        verify(traineeDao).save(trainee);
        verify(trainerDao).save(trainer);
    }

    @Test
    void shouldUpdateList() {
        Trainee trainee = new Trainee();

        Trainer t1 = new Trainer();
        Trainer t2 = new Trainer();

        when(traineeDao.findByUsername("john"))
                .thenReturn(trainee);

        when(trainerDao.findById(1L))
                .thenReturn(t1);

        when(trainerDao.findById(2L))
                .thenReturn(t2);

        service.updateTrainerList("john", List.of(1L, 2L));

        assertEquals(2, trainee.getTrainers().size());

        verify(traineeDao).save(trainee);
    }
}
