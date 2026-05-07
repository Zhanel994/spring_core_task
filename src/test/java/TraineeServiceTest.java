import com.gym.config.AppConfig;
import com.gym.models.Trainee;
import com.gym.services.TraineeService;
import com.gym.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeServiceTest {

    private TraineeService service;
    private Storage storage;

    @BeforeEach
    void setUp() {
        var ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        service = ctx.getBean(TraineeService.class);
        storage = ctx.getBean(Storage.class);

        storage.getTrainees().clear();
        storage.getTrainers().clear();
        storage.getTrainings().clear();
    }

    @Test
    void createTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");

        Trainee created = service.create(trainee);

        assertNotNull(created.getId());
        assertTrue(created.getUsername().startsWith("John.Smith"));
        assertEquals(10, created.getPassword().length());
    }

    @Test
    void deleteTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");

        Trainee created = service.create(trainee);

        Long id = created.getId();
        service.delete(id);

        assertNull(service.get(id));
    }

    @Test
    void usernameUniqueness() {
        Trainee trainee1 = new Trainee();
        trainee1.setFirstName("John");
        trainee1.setLastName("Smith");

        Trainee trainee2 = new Trainee();
        trainee2.setFirstName("John");
        trainee2.setLastName("Smith");

        Trainee created1 = service.create(trainee1);
        Trainee created2 = service.create(trainee2);

        assertEquals("John.Smith", created1.getUsername());
        assertEquals("John.Smith1", created2.getUsername());
    }
}
