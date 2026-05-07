import com.gym.config.AppConfig;
import com.gym.models.Training;
import com.gym.services.TrainingService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrainingServiceTest {
    @Test
    void createTraining() {
        var ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        var service = ctx.getBean(TrainingService.class);

        Training training = new Training();
        training.setTrainingName("BASKETBALL");
        training.setTrainingType("SPORT");

        Training created = service.create(training);

        assertNotNull(created.getId());
        assertEquals("BASKETBALL", created.getTrainingName());
    }
}
