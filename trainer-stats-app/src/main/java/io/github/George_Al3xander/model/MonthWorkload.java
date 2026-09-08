package io.github.George_Al3xander.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MonthWorkload {
    @Id
    private String id;

    private int month;

    private int trainingSummaryDuration;
}
