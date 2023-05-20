package org.example.test_data_bils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)

public class MatchDifferences {

    private String codeHome;
    private String date;
    private String time;
    private String name;
    private String nameForeign;
    private String oneHome;
    private String oneForeign;
    private String oneDifferences;
    private String twoHome;
    private String twoForeign;
    private String twoDifferences;
    private String xHome;
    private String xForeign;
    private String xDifferences;
    private String comparison;
    private String counterQuota;
    private String higherOdds;
    private String bet;
    private String earnings;


}
