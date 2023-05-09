package org.example.test_data_bils;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataFile {



    public String emailUser1NoVerify;
    public String businessName1NoVerify;
    public String passUser1NoVerify;
    public String idUser1NoVerify;

    public String emailUser2NoVerify;
    public String businessName2NoVerify;
    public String passUser2NoVerify;
    public String idUser2NoVerify;

    public String emailUser3NoVerify;
    public String businessName3NoVerify;
    public String passUser3NoVerify;
    public String idUser3NoVerify;

    public String emailAdmin;
    public String passAdmin;

    public String emailUser1;
    public String businessName1;
    public String passUser1;
    public String idUser1;

    public String emailUser2;
    public String businessName2;
    public String passUser2;
    public String idUser2;

    public String emailUser3;
    public String businessName3;
    public String passUser3;
    public String idUser3;

    public String emailUserCreateAdmin;
    public String businessNameCreateAdmin;
    public String passUserCreateAdmin;
    public String idUserCreateAdmin;

    public String idTrade1;
    public String reportingTrade1;
    public String receivingTrade1;
    public String typeTrade1;
    public String dateTrade1;
    public String amountTrade1;
    public String balanceTrade1;






}
