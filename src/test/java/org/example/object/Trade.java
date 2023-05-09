package org.example.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trade {
    private String status;
    private String highCreditLimit;
    private String outstandingBalance;
    private String creditUtilization;
    private String tradeType;
    private String lastActivity;
    private String tradeID;


}
