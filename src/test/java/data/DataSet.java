package data;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSet {
    public String data1 = "webdriver.chrome.driver";
    public String data2 = "src/main/resources/chromedriver";
    public String data3 = "--remote-allow-origins=*";
    public String data4 = "start-maximized";
    public String data5 = "disable-infobars";
    public String data6 = "--disable-extensions";
    public String data7 = "--disable-gpu";
    public String data8 = "--disable-dev-shm-usage";
    public String data9 = "--no-sandbox";
  //wwww
}