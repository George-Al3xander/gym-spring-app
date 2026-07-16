package io.github.George_Al3xander.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerFilter {
    private Boolean active = true;
    
    private Boolean assigned = true;
}
