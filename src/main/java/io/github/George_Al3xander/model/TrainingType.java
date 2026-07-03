package io.github.George_Al3xander.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "training_types")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingType {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "training_type_name", nullable = false)
    private String trainingTypeName;
}
