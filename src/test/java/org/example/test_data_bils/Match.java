package org.example.test_data_bils;

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
    private String date;
    private String name;
    private String one;
    private String time;
    private String two;
    private String x;
//    private String status; //identicno/delimicno/nemapar
}
