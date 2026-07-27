package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dao.TrainingTypeDao;
import io.github.George_Al3xander.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class TrainingTypeControllerTest {

    @Mock
    private TrainingTypeDao trainingTypeDao;

    private MockMvc mockMvc;


    @BeforeEach
    void setup() {

        TrainingTypeController controller =
                new TrainingTypeController(trainingTypeDao);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }


    @Test
    void getTrainingTypes_shouldReturnTrainingTypes()
            throws Exception {

        TrainingType cardio = new TrainingType();
        cardio.setId(1L);
        cardio.setTrainingTypeName("Cardio");


        TrainingType strength = new TrainingType();
        strength.setId(2L);
        strength.setTrainingTypeName("Strength");


        when(trainingTypeDao.findAll())
                .thenReturn(List.of(cardio, strength));


        mockMvc.perform(get("/training-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()")
                        .value(2))

                .andExpect(jsonPath("$[0].id")
                        .value(1))
                .andExpect(jsonPath("$[0].trainingTypeName")
                        .value("Cardio"))

                .andExpect(jsonPath("$[1].id")
                        .value(2))
                .andExpect(jsonPath("$[1].trainingTypeName")
                        .value("Strength"));
    }


    @Test
    void getTrainingTypes_shouldReturnEmptyList()
            throws Exception {

        when(trainingTypeDao.findAll())
                .thenReturn(List.of());


        mockMvc.perform(get("/training-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()")
                        .value(0));
    }
}