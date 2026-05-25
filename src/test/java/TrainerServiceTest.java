import com.gym.dao.TrainerDao;
import com.gym.exceptions.ValidationException;
import com.gym.models.Trainer;
import com.gym.models.User;
import com.gym.services.TrainerService;
import com.gym.utils.PasswordGenerator;
import com.gym.utils.UsernameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    private TrainerDao trainerDao;
    private UsernameGenerator usernameGenerator;
    private PasswordGenerator passwordGenerator;

    private TrainerService service;

    @BeforeEach
    void setUp() {
        trainerDao = mock(TrainerDao.class);
        usernameGenerator = mock(UsernameGenerator.class);
        passwordGenerator = mock(PasswordGenerator.class);

        service = new TrainerService(
                trainerDao,
                usernameGenerator,
                passwordGenerator
        );
    }

    @Test
    void createTrainer() {
        User user = new User();
        user.setFirstName("Michael");
        user.setLastName("Jordan");

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(usernameGenerator.generate("Michael", "Jordan"))
                .thenReturn("Michael.Jordan");

        when(passwordGenerator.generate())
                .thenReturn("password");

        Trainer result = service.create(trainer);

        assertEquals("Michael.Jordan", result.getUser().getUsername());
        assertEquals("password", result.getUser().getPassword());

        verify(trainerDao).save(trainer);
    }

    @Test
    void shouldThrowIfNoFirstName() {
        User user = new User();
        user.setLastName("Jordan");

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        assertThrows(ValidationException.class, () -> service.create(trainer));
    }

    @Test
    void shouldSetActiveTrue() {
        User user = new User();
        user.setActive(false);

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerDao.findByUsername("michael"))
                .thenReturn(trainer);

        service.activate("michael");

        assertTrue(user.isActive());
        verify(trainerDao).save(trainer);
    }

    @Test
    void shouldThrowIfAlreadyActive() {
        User user = new User();
        user.setActive(true);

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerDao.findByUsername("michael"))
                .thenReturn(trainer);

        assertThrows(RuntimeException.class, () -> service.activate("michael"));
    }

    @Test
    void shouldSetInactive() {
        User user = new User();
        user.setActive(true);

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerDao.findByUsername("michael"))
                .thenReturn(trainer);

        service.deactivate("michael");

        assertFalse(user.isActive());
        verify(trainerDao).save(trainer);
    }

    @Test
    void changePassword() {
        User user = new User();
        user.setPassword("old");

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerDao.findByUsername("michael"))
                .thenReturn(trainer);

        service.changePassword("michael", "new");

        assertEquals("new", user.getPassword());
    }
}