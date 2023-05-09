package org.example.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {

    private String code;
    private String time;
    private String name;
    private String one;
    private String two;
    private String x;



}
