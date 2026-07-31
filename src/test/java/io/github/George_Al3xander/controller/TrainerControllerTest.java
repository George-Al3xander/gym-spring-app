package io.github.George_Al3xander.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.dto.trainer.TrainerProfileResponse;
import io.github.George_Al3xander.dto.trainer.TrainerRegistrationRequest;
import io.github.George_Al3xander.dto.trainer.TrainerTrainingResponse;
import io.github.George_Al3xander.dto.trainer.UpdateTrainerRequest;
import io.github.George_Al3xander.dto.user.ActivateUserRequest;
import io.github.George_Al3xander.facade.GymFacade;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.web.AuthHttpHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @Mock
    private GymFacade gymFacade;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {

        TrainerController controller =
                new TrainerController(gymFacade);

        LocalValidatorFactoryBean validator =
                new LocalValidatorFactoryBean();

        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setValidator(validator)
                .build();

        objectMapper = new ObjectMapper();
    }


    @Test
    void createTrainer_shouldReturnCredentials()
            throws Exception {

        CredentialsDTO credentialsDTO = new CredentialsDTO("mike.brown", "1234567890");

        Trainer trainer = new Trainer();

        trainer.setUsername(credentialsDTO.getUsername());
        trainer.setPassword(credentialsDTO.getPassword());


        when(gymFacade.createTrainer(any()))
                .thenReturn(credentialsDTO);


        TrainerRegistrationRequest request =
                new TrainerRegistrationRequest(
                        "Mike",
                        "Brown",
                        1L
                );


        mockMvc.perform(post("/trainers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username")
                        .value("mike.brown"))
                .andExpect(jsonPath("$.password")
                        .value("1234567890"));
    }


    @Test
    void createTrainer_shouldReturnBadRequest_whenFirstNameEmpty()
            throws Exception {

        TrainerRegistrationRequest request =
                new TrainerRegistrationRequest(
                        "",
                        "Brown",
                        1L
                );


        mockMvc.perform(post("/trainers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getTrainer_shouldReturnProfile()
            throws Exception {

        TrainerProfileResponse response =
                new TrainerProfileResponse();

        response.setUsername("mike");
        response.setFirstName("Mike");
        response.setLastName("Brown");
        response.setIsActive(true);


        when(gymFacade.getTrainer("mike"))
                .thenReturn(response);


        mockMvc.perform(get("/trainers/mike")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "mike"
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username")
                        .value("mike"))
                .andExpect(jsonPath("$.firstName")
                        .value("Mike"))
                .andExpect(jsonPath("$.lastName")
                        .value("Brown"))
                .andExpect(jsonPath("$.isActive")
                        .value(true));
    }


    @Test
    void updateTrainer_shouldReturnUpdatedProfile()
            throws Exception {

        TrainerProfileResponse response =
                new TrainerProfileResponse();

        response.setUsername("mike");
        response.setFirstName("Michael");


        when(gymFacade.updateTrainer(
                any(),
                any()
        ))
                .thenReturn(response);


        UpdateTrainerRequest request =
                new UpdateTrainerRequest(
                        "Michael",
                        "Brown",
                        true
                );


        mockMvc.perform(put("/trainers/mike")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "mike"
                        )
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username")
                        .value("mike"))
                .andExpect(jsonPath("$.firstName")
                        .value("Michael"));
    }


    @Test
    void updateTrainer_shouldReturnForbidden_whenDifferentUser()
            throws Exception {

        UpdateTrainerRequest request =
                new UpdateTrainerRequest(
                        "Michael",
                        "Brown",
                        true
                );


        mockMvc.perform(put("/trainers/mike")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "admin"
                        )
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }


    @Test
    void updateTrainer_shouldReturnBadRequest_whenInvalidRequest()
            throws Exception {

        UpdateTrainerRequest request =
                new UpdateTrainerRequest(
                        "",
                        "",
                        null
                );


        mockMvc.perform(put("/trainers/mike")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "mike"
                        )
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getTrainings_shouldReturnTrainingList()
            throws Exception {


        TrainerTrainingResponse training =
                new TrainerTrainingResponse();

        training.setTrainingName("Fitness");
        training.setTraineeName("John");


        when(gymFacade.getTrainerTrainings(
                any(),
                any()
        ))
                .thenReturn(List.of(training));


        mockMvc.perform(get("/trainers/mike/trainings")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "mike"
                        )
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingName")
                        .value("Fitness"))
                .andExpect(jsonPath("$[0].traineeName")
                        .value("John"));
    }


    @Test
    void getTrainings_shouldReturnForbidden_whenDifferentUser()
            throws Exception {


        mockMvc.perform(get("/trainers/mike/trainings")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "admin"
                        )
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isForbidden());
    }


    @Test
    void activateTrainer_shouldReturnNoContent()
            throws Exception {

        ActivateUserRequest request =
                new ActivateUserRequest(true);


        mockMvc.perform(patch("/trainers/mike/activate")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "mike"
                        )
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }


    @Test
    void activateTrainer_shouldReturnForbidden_whenDifferentUser()
            throws Exception {

        ActivateUserRequest request =
                new ActivateUserRequest(true);


        mockMvc.perform(patch("/trainers/mike/activate")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "admin"
                        )
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}