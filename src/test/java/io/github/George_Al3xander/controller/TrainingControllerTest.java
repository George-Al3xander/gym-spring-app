package io.github.George_Al3xander.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.George_Al3xander.dto.training.AddTrainingRequest;
import io.github.George_Al3xander.facade.GymFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @Mock
    private GymFacade gymFacade;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {

        TrainingController controller =
                new TrainingController(gymFacade);

        LocalValidatorFactoryBean validator =
                new LocalValidatorFactoryBean();

        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setValidator(validator)
                .build();

        objectMapper = new ObjectMapper()
                .findAndRegisterModules();
    }


    @Test
    void addTraining_shouldReturnCreated()
            throws Exception {

        AddTrainingRequest request =
                new AddTrainingRequest(
                        "trainee1",
                        "trainer1",
                        "Cardio",
                        LocalDateTime.now(),
                        3600
                );


        mockMvc.perform(post("/trainings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }


    @Test
    void addTraining_shouldReturnBadRequest_whenTraineeUsernameMissing()
            throws Exception {

        AddTrainingRequest request =
                new AddTrainingRequest(
                        "",
                        "trainer1",
                        "Cardio",
                        LocalDateTime.now(),
                        3600
                );


        mockMvc.perform(post("/trainings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void addTraining_shouldReturnBadRequest_whenTrainerUsernameMissing()
            throws Exception {

        AddTrainingRequest request =
                new AddTrainingRequest(
                        "trainee1",
                        "",
                        "Cardio",
                        LocalDateTime.now(),
                        3600
                );


        mockMvc.perform(post("/trainings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void addTraining_shouldReturnBadRequest_whenTrainingNameMissing()
            throws Exception {

        AddTrainingRequest request =
                new AddTrainingRequest(
                        "trainee1",
                        "trainer1",
                        "",
                        LocalDateTime.now(),
                        3600
                );


        mockMvc.perform(post("/trainings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void addTraining_shouldReturnBadRequest_whenDurationIsNegative()
            throws Exception {

        AddTrainingRequest request =
                new AddTrainingRequest(
                        "trainee1",
                        "trainer1",
                        "Cardio",
                        LocalDateTime.now(),
                        -10
                );


        mockMvc.perform(post("/trainings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void addTraining_shouldReturnBadRequest_whenDateIsMissing()
            throws Exception {

        AddTrainingRequest request =
                new AddTrainingRequest(
                        "trainee1",
                        "trainer1",
                        "Cardio",
                        null,
                        3600
                );


        mockMvc.perform(post("/trainings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}