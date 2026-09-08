package io.github.George_Al3xander.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class YearWorkload {
    @Id
    private String id;

    private int year;

    private List<MonthWorkload> months = new ArrayList<>();
}
