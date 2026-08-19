package io.github.George_Al3xander.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "training_year")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class YearWorkload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_workload_id", nullable = false)
    private TrainerWorkload trainerWorkload;

    @OneToMany(
            mappedBy = "yearWorkload",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MonthWorkload> months = new ArrayList<>();
}
