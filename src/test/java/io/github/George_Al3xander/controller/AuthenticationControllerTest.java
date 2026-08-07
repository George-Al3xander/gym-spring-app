package io.github.George_Al3xander.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.George_Al3xander.auth.JwtUtil;
import io.github.George_Al3xander.dto.auth.ChangeLoginRequest;
import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticationController controller;

    private MockMvc mockMvc;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setValidator(validator)
                .build();
    }

    @Test
    void login_ShouldReturn200_AndAuthenticate() throws Exception {
        CredentialsDTO credentials =
                new CredentialsDTO("john", "1234567890");

        when(authenticationService.authenticate(credentials))
                .thenReturn(true);

        when(jwtUtil.generateToken("john"))
                .thenReturn("jwt-token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk());

        verify(authenticationService)
                .authenticate(credentials);

        verify(jwtUtil)
                .generateToken("john");
    }

    @Test
    void login_ShouldReturn401_WhenCredentialsInvalid() throws Exception {
        CredentialsDTO credentials =
                new CredentialsDTO("john", "1234567890");

        when(authenticationService.authenticate(credentials))
                .thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isUnauthorized());

        verify(authenticationService)
                .authenticate(credentials);

        verifyNoInteractions(jwtUtil);
    }

    @Test
    void changeLogin_ShouldReturn200_AndChangePassword() throws Exception {
        ChangeLoginRequest request =
                new ChangeLoginRequest("john", "oldPassword", "abcdefghij");

        mockMvc.perform(put("/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        ArgumentCaptor<ChangeLoginRequest> requestCaptor =
                ArgumentCaptor.forClass(ChangeLoginRequest.class);

        verify(authenticationService)
                .changePassword(
                        requestCaptor.capture()
                );

        assertEquals(
                "john",
                requestCaptor.getValue().getUsername()
        );
        assertEquals(
                "oldPassword",
                requestCaptor.getValue().getOldPassword()
        );
        assertEquals(
                "abcdefghij",
                requestCaptor.getValue().getNewPassword()
        );
    }

    @Test
    void login_ShouldReturn400_WhenPasswordTooShort() throws Exception {
        CredentialsDTO credentials =
                new CredentialsDTO("john", "short");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void changeLogin_ShouldReturn400_WhenPasswordInvalid() throws Exception {
        ChangeLoginRequest request =
                new ChangeLoginRequest("username", "oldPassword", "123");

        mockMvc.perform(put("/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void changeLogin_ShouldReturn400_WhenOldPasswordMissing() throws Exception {
        ChangeLoginRequest request =
                new ChangeLoginRequest(null, null, "abcdefghij");

        mockMvc.perform(put("/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }
}