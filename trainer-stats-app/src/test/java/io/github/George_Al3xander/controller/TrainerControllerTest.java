package io.github.George_Al3xander.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.George_Al3xander.dto.workload.ActionType;
import io.github.George_Al3xander.dto.workload.WorkloadRequest;
import io.github.George_Al3xander.model.TrainerWorkload;
import io.github.George_Al3xander.service.TrainerWorkloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @Mock
    private TrainerWorkloadService trainerWorkloadService;


    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setup() {
        TrainerController controller =
                new TrainerController(trainerWorkloadService);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();

        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setValidator(validator)
                .build();
    }

    @Test
    void givenValidAddTrainingRequest_whenAddTraining_thenReturnOk()
            throws Exception {

        WorkloadRequest request = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                20,
                ActionType.ADD
        );

        mockMvc.perform(
                        post("/trainer/workload")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        verify(trainerWorkloadService).handleTraining(request);
    }

    @Test
    void givenNegativeTrainingDuration_whenAddTraining_thenReturnBadRequest()
            throws Exception {

        WorkloadRequest request = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                -1,
                ActionType.ADD
        );

        mockMvc.perform(
                        post("/trainer/workload")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainerWorkloadService);
    }

    @Test
    void givenBlankUsername_whenAddTraining_thenReturnBadRequest()
            throws Exception {

        WorkloadRequest request = createRequest(
                "",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                20,
                ActionType.ADD
        );

        mockMvc.perform(
                        post("/trainer/workload")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainerWorkloadService);
    }

    @Test
    void givenBlankFirstName_whenAddTraining_thenReturnBadRequest()
            throws Exception {

        WorkloadRequest request = createRequest(
                "john.doe",
                "",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                20,
                ActionType.ADD
        );

        mockMvc.perform(
                        post("/trainer/workload")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainerWorkloadService);
    }

    @Test
    void givenBlankLastName_whenAddTraining_thenReturnBadRequest()
            throws Exception {

        WorkloadRequest request = createRequest(
                "john.doe",
                "John",
                "",
                true,
                LocalDate.of(2026, 8, 10),
                20,
                ActionType.ADD
        );

        mockMvc.perform(
                        post("/trainer/workload")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainerWorkloadService);
    }

    @Test
    void givenNullTrainingDate_whenAddTraining_thenReturnBadRequest()
            throws Exception {

        WorkloadRequest request = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                null,
                20,
                ActionType.ADD
        );

        mockMvc.perform(
                        post("/trainer/workload")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainerWorkloadService);
    }

    @Test
    void givenNullActionType_whenAddTraining_thenReturnBadRequest()
            throws Exception {

        WorkloadRequest request = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                20,
                null
        );

        mockMvc.perform(
                        post("/trainer/workload")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainerWorkloadService);
    }

    @Test
    void givenMalformedJson_whenAddTraining_thenReturnBadRequest()
            throws Exception {

        mockMvc.perform(
                        post("/trainer/workload")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "trainerUsername": "john.doe",
                                            "trainerFirstName":
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainerWorkloadService);
    }

    @Test
    void givenMissingRequestBody_whenAddTraining_thenReturnBadRequest()
            throws Exception {

        mockMvc.perform(
                        post("/trainer/workload")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trainerWorkloadService);
    }

    @Test
    void givenMissingContentType_whenAddTraining_thenReturnUnsupportedMediaType()
            throws Exception {

        WorkloadRequest request = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                20,
                ActionType.ADD
        );

        mockMvc.perform(
                        post("/trainer/workload")
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnsupportedMediaType());

        verifyNoInteractions(trainerWorkloadService);
    }

    @Test
    void givenExistingTrainerUsername_whenGetTrainerWorkload_thenReturnOkAndWorkload()
            throws Exception {

        String username = "john.doe";

        TrainerWorkload workload = createWorkload(
                username,
                "John",
                "Doe"
        );

        when(trainerWorkloadService
                .getWorkloadByTrainerUsername(username))
                .thenReturn(workload);

        mockMvc.perform(
                        get("/trainer/workload/{username}", username)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(workload)
                ));

        verify(trainerWorkloadService)
                .getWorkloadByTrainerUsername(username);
    }

    @Test
    void givenExistingTrainerUsername_whenGetTrainerWorkload_thenCallServiceWithUsername()
            throws Exception {

        String username = "john.doe";

        TrainerWorkload workload = createWorkload(
                username,
                "John",
                "Doe"
        );

        when(trainerWorkloadService
                .getWorkloadByTrainerUsername(username))
                .thenReturn(workload);

        mockMvc.perform(
                        get("/trainer/workload/{username}", username)
                )
                .andExpect(status().isOk());

        verify(trainerWorkloadService)
                .getWorkloadByTrainerUsername(username);
    }

    @Test
    void givenTrainerWithZeroHours_whenGetTrainerWorkload_thenReturnZeroHours()
            throws Exception {

        String username = "john.doe";

        TrainerWorkload workload = createWorkload(
                username,
                "John",
                "Doe"
        );

        when(trainerWorkloadService
                .getWorkloadByTrainerUsername(username))
                .thenReturn(workload);

        mockMvc.perform(
                        get("/trainer/workload/{username}", username)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(workload)
                ));
    }

    @Test
    void givenServiceReturnsNull_whenGetTrainerWorkload_thenReturnOkWithEmptyBody()
            throws Exception {

        String username = "john.doe";

        when(trainerWorkloadService
                .getWorkloadByTrainerUsername(username))
                .thenReturn(null);

        mockMvc.perform(
                        get("/trainer/workload/{username}", username)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(trainerWorkloadService)
                .getWorkloadByTrainerUsername(username);
    }

    @Test
    void givenValidUsername_whenGetTrainerWorkload_thenReturnJsonRepresentation()
            throws Exception {

        String username = "john.doe";

        TrainerWorkload workload = createWorkload(
                username,
                "John",
                "Doe"
        );

        when(trainerWorkloadService
                .getWorkloadByTrainerUsername(username))
                .thenReturn(workload);

        mockMvc.perform(
                        get("/trainer/workload/{username}", username)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(content().json(
                        objectMapper.writeValueAsString(workload)
                ));
    }

    private WorkloadRequest createRequest(
            String username,
            String firstName,
            String lastName,
            boolean active,
            LocalDate date,
            int duration,
            ActionType actionType
    ) {
        WorkloadRequest request = new WorkloadRequest();

        request.setCorrelationId(UUID.randomUUID().toString());
        request.setTrainerUsername(username);
        request.setTrainerFirstName(firstName);
        request.setTrainerLastName(lastName);
        request.setActive(active);
        request.setTrainingDate(date);
        request.setTrainingDuration(duration);
        request.setActionType(actionType);

        return request;
    }

    private TrainerWorkload createWorkload(
            String username,
            String firstName,
            String lastName
    ) {
        TrainerWorkload workload = new TrainerWorkload();

        workload.setTrainerUsername(username);
        workload.setTrainerFirstName(firstName);
        workload.setTrainerLastName(lastName);

        return workload;
    }
}
