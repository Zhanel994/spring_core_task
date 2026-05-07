import com.gym.config.AppConfig;
import com.gym.models.Trainer;
import com.gym.services.TrainerService;
import com.gym.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerServiceTest {
    private TrainerService service;
    private Storage storage;

    @BeforeEach
    void setUp() {
        var ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        service = ctx.getBean(TrainerService.class);
        storage = ctx.getBean(Storage.class);

        storage.getTrainees().clear();
        storage.getTrainers().clear();
        storage.getTrainings().clear();
    }

    @Test
    void createTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Michael");
        trainer.setLastName("Jordan");

        Trainer created = service.create(trainer);

        assertNotNull(created.getId());
        assertTrue(created.getUsername().startsWith("Michael.Jordan"));
        assertEquals(10, created.getPassword().length());
    }

    @Test
    void usernameUniqueness() {
        Trainer trainer1 = new Trainer();
        trainer1.setFirstName("Michael");
        trainer1.setLastName("Jordan");

        Trainer trainer2 = new Trainer();
        trainer2.setFirstName("Michael");
        trainer2.setLastName("Jordan");

        Trainer created1 = service.create(trainer1);
        Trainer created2 = service.create(trainer2);

        assertEquals("Michael.Jordan", created1.getUsername());
        assertEquals("Michael.Jordan1", created2.getUsername());
    }
}
