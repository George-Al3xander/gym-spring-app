package io.github.George_Al3xander.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeSummaryResponse {
    private String username;

    private String firstName;

    private String lastName;
}
