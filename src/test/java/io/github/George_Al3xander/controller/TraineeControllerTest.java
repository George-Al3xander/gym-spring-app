package io.github.George_Al3xander.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.dto.trainee.TraineeProfileResponse;
import io.github.George_Al3xander.dto.trainee.TraineeRegistrationRequest;
import io.github.George_Al3xander.dto.trainee.UpdateTraineeRequest;
import io.github.George_Al3xander.dto.trainer.TrainerSummaryResponse;
import io.github.George_Al3xander.dto.user.ActivateUserRequest;
import io.github.George_Al3xander.facade.GymFacade;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @Mock
    private GymFacade gymFacade;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {

        TraineeController controller =
                new TraineeController(gymFacade);

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
    void createTrainee_ShouldReturnCredentials() throws Exception {

        CredentialsDTO credentials =
                new CredentialsDTO("john", "1234567890");

        when(gymFacade.createTrainee(any()))
                .thenReturn(credentials);

        TraineeRegistrationRequest request =
                new TraineeRegistrationRequest(
                        "John",
                        "Smith",
                        null,
                        "London"
                );

        mockMvc.perform(post("/trainees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username")
                        .value("john"))
                .andExpect(jsonPath("$.password")
                        .value("1234567890"));

        verify(gymFacade)
                .createTrainee(any(TraineeRegistrationRequest.class));
    }


    @Test
    void getTrainee_ShouldReturnProfile() throws Exception {

        TraineeProfileResponse response =
                new TraineeProfileResponse();

        response.setUsername("john");


        when(gymFacade.getTrainee("john"))
                .thenReturn(response);


        mockMvc.perform(get("/trainees/john")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "john"
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username")
                        .value("john"));


        verify(gymFacade)
                .getTrainee("john");
    }


    @Test
    void updateTrainee_ShouldReturnForbidden_WhenDifferentUser()
            throws Exception {


        UpdateTraineeRequest request =
                new UpdateTraineeRequest(
                        "John",
                        "Smith",
                        null,
                        "Address",
                        true
                );


        mockMvc.perform(put("/trainees/john")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "admin"
                        )
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());


        verifyNoInteractions(gymFacade);
    }


    @Test
    void deleteTrainee_ShouldReturnNoContent()
            throws Exception {


        mockMvc.perform(delete("/trainees/john")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "john"
                        ))
                .andExpect(status().isNoContent());


        verify(gymFacade)
                .deleteTrainee("john");
    }


    @Test
    void deleteTrainee_ShouldReturnForbidden()
            throws Exception {


        mockMvc.perform(delete("/trainees/john")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "other"
                        ))
                .andExpect(status().isForbidden());


        verifyNoInteractions(gymFacade);
    }


    @Test
    void getUnassignedTrainers_ShouldReturnList()
            throws Exception {


        when(gymFacade.getTrainersByTraineeUsername(
                eq("john"),
                any()
        ))
                .thenReturn(List.of(
                        new TrainerSummaryResponse(
                                "trainer1",
                                "Mike",
                                "Brown",
                                null
                        )
                ));


        mockMvc.perform(get("/trainees/john/unassigned-trainers")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "john"
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username")
                        .value("trainer1"));
    }


    @Test
    void activateTrainee_ShouldReturnNoContent()
            throws Exception {


        ActivateUserRequest request =
                new ActivateUserRequest(true);


        mockMvc.perform(patch("/trainees/john/activate")
                        .header(
                                AuthHttpHeader.USERNAME,
                                "john"
                        )
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());


        verify(gymFacade)
                .updateActiveStatusByUsername(
                        "john",
                        true
                );
    }
}