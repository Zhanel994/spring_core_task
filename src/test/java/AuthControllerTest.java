import com.gym.controllers.AuthController;
import com.gym.services.AuthService;
import com.gym.services.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void login_success() throws Exception {
        when(authService.authenticate("john", "123456789")).thenReturn(true);

        mockMvc.perform(get("/api/v1/auth/login")
                        .param("username", "john")
                        .param("password", "123456789"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful!"));
        System.out.println("Login success!");
    }

    @Test
    void login_fail() throws Exception {
        when(authService.authenticate("john", "wrong")).thenReturn(false);

        mockMvc.perform(get("/api/v1/auth/login")
                        .param("username", "john")
                        .param("password", "wrong"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid credentials!"));
        System.out.println("Login fail!");
    }
}
