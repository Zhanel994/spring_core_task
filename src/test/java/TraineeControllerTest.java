import com.gym.controllers.TraineeController;
import com.gym.models.Trainee;
import com.gym.models.User;
import com.gym.services.TraineeService;
import com.gym.services.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TraineeControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TraineeController traineeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
    }

    @Test
    void getProfile_success() throws Exception {
        User user = new User();
        user.setUsername("trainee1");
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        //trainee.setDateOfBirth(LocalDate.of(1998, 5, 18)); --> temporarily commented
        trainee.setAddress("London");
        trainee.setTrainers(new ArrayList<>());

        when(traineeService.getByUsername("trainee1")).thenReturn(trainee);

        mockMvc.perform(get("/api/v1/trainees/trainee1")).andExpect(status().isOk());

        System.out.println("Get trainee profile success!");
    }
}
