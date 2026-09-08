package io.github.George_Al3xander.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "trainer_workloads")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainerWorkload {
    @Id
    private String id;

    private String trainerUsername;

    @TextIndexed
    private String trainerFirstName;

    @TextIndexed
    private String trainerLastName;

    private boolean trainerStatus;

    private List<YearWorkload> years = new ArrayList<>();
}
