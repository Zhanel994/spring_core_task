import com.gym.controllers.TrainerController;
import com.gym.models.Trainer;
import com.gym.models.User;
import com.gym.services.TrainerService;
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

public class TrainerControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
    }

    @Test
    void getProfile_success() throws Exception {
        User user = new User();
        user.setUsername("trainer1");
        user.setFirstName("Michael");
        user.setLastName("Jordan");
        user.setActive(true);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization("Basketball");
        trainer.setTrainees(new ArrayList<>());

        when(trainerService.getByUsername("trainer1")).thenReturn(trainer);

        mockMvc.perform(get("/api/v1/trainers/trainer1")).andExpect(status().isOk());

        System.out.println("Get trainer profile success!");
    }
}
