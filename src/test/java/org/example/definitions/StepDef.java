package org.example.definitions;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.page_factory.*;
import org.example.test_data_bils.DataFile;
import org.example.test_data_bils.Match;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


import static io.restassured.RestAssured.given;

public class StepDef {


    public static WebDriver driver;

    public static String baseUrl = "http://178.220.24.117:9914//";


    DataFile dataFile = new DataFile();

    Actions actions;

    MaxBet maxBet;
    Foreign foreign;
    ObjectMapper objectMapper;


//    WebDriverWait wait = new WebDriverWait(driver, 10);
//WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState == 'complete'"));

    @Before

    public void before() throws FileNotFoundException {
        System.setProperty("webdriver.chrome.driver",
                "src/main/resources/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.manage().window().maximize();
        // driver.get(baseUrl);
        actions = new Actions(driver);
        dataFile = readerData();

        maxBet = new MaxBet(driver);
        foreign = new Foreign(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        objectMapper = new ObjectMapper();
    }

    @After(order = 1)
    public void teardown(Scenario scenario) throws IOException {
        if (scenario.isFailed()) {

            TakesScreenshot ts = (TakesScreenshot) driver;
            final byte[] src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(src, "image/png", "screenshot");

        }

    }


    @After(order = 0)
    public void afterClass() throws InterruptedException {

        Thread.sleep(3000);
        driver.quit();
    }


    public DataFile readerData() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("/home/marko/IdeaProjects/CRMFront/src/test/resources/json/data.json"));
        Gson gson1 = new GsonBuilder().create();
        DataFile dataObject = gson1.fromJson(br, DataFile.class);
        return dataObject;
    }

    @Given("go to the address {string}")
    public void go_to_the_address(String address) {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        driver.get(address);
    }

    @Given("reade data data1")
    public void reade_data_data1() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Match> objects = mapper.readValue(new File("/home/marko/IdeaProjects/CRMFront/src/test/resources/json/data.json"), new TypeReference<List<Match>>() {
        });

        System.out.println(objects.size());
    }

    @When("choose which period you are watching {string}")
    public void choose_which_period_you_are_watching(String hours) throws InterruptedException {
        maxBet.clickSlider(hours);
        Thread.sleep(1000);
    }

    @When("click on the page max-bet button football")
    public void click_on_the_page_maxbet_button_football() {
        maxBet.clickFootball();
    }

    @When("click on the page max-bet button select all")
    public void click_on_the_page_maxbet_select_all() {
        maxBet.clickSelectAll();
    }

    @When("click on the page max-bet button max bonus")
    public void click_on_the_page_maxbet_bonus() throws InterruptedException {
        Thread.sleep(2000);
        maxBet.clickMaxBonus();
        Thread.sleep(2000);

    }

    @When("wait for the whole page to load")
    public void wait_for_the_whole_page_to_load() throws InterruptedException {
        maxBet.waitForPageToLoad();
    }

    @When("take all the matches according to the given criteria")
    public void take_all_the_matches_according_to_the_given_criteria() throws InterruptedException {
        foreign.findMatches();
    }

    @Then("write all match in document")
    public void write_all_match_in_document() {
        maxBet.writeMatch();
    }

    @Then("compare odds")
    public void compare_odds() throws IOException {

        Match[] matchHome = objectMapper.readValue(new File("src/test/resources/json/data.json"), Match[].class);
        Match[] matchForeign = objectMapper.readValue(new File("src/test/resources/json/data2.json"), Match[].class);

        System.out.println(matchHome.length);
        System.out.println(matchForeign.length);

        List<Match> matchesBingo = new ArrayList<>();
        List<Match> matchDiscard = new ArrayList<>();

        for (int i = 0; i < matchForeign.length; i++) {
            System.out.println("Br utakmica " + matchForeign.length);
            Match mathForeignBetting = matchForeign[i];
            String name = mathForeignBetting.getName().toLowerCase();
            String time = mathForeignBetting.getTime();
            String nameHome = name.substring(0, name.indexOf(" -"));
            String nameForeign = name.substring(name.lastIndexOf("- ") + 1);
            System.out.println(nameForeign);


            for (int j = 0; j < matchHome.length; j++) {
                Match matchHomeBetting = matchHome[j];

                String nameBetting = matchHomeBetting.getName().toLowerCase();
                if (nameBetting.contains(name.substring(0, 4)) && time.contains(matchHomeBetting.getTime())) {
                    System.out.println("1");
                    System.out.println(matchHomeBetting.getTime());
                    System.out.println(time);
                    System.out.println(name);
                    System.out.println(matchHomeBetting.getName());
                    matchesBingo.add(differences(matchHomeBetting, mathForeignBetting));
                    break;
                } else if (nameBetting.contains(name.substring((name.length()) - 4, name.length())) && time.contains(matchHomeBetting.getTime())) {
                    System.out.println("2");
                    System.out.println(name);
                    System.out.println(matchHomeBetting.getName());
                    matchesBingo.add(differences(matchHomeBetting, mathForeignBetting));
                    break;
                } else if (nameBetting.contains(nameHome.substring((nameHome.length()) - 3, nameHome.length())) && time.contains(matchHomeBetting.getTime())) {
                    System.out.println("3");
                    System.out.println(name);
                    System.out.println(matchHomeBetting.getName());
                    matchesBingo.add(differences(matchHomeBetting, mathForeignBetting));
                    break;

                } else if (nameBetting.contains(nameHome.substring(0, 3)) && time.contains(matchHomeBetting.getTime())) {
                    System.out.println("4");
                    System.out.println(name);
                    System.out.println(matchHomeBetting.getName());
                    matchesBingo.add(differences(matchHomeBetting, mathForeignBetting));
                    break;
                } else {
                    matchDiscard.add(matchHomeBetting);
                }
            }
        }
        // Ispisujemo pronađeni objekat
        System.out.println(matchesBingo.size());
        System.out.println(matchDiscard.size());

        Gson gson = new Gson();
        String json = gson.toJson(matchesBingo);
        try (FileWriter fileWriter = new FileWriter("/home/marko/IdeaProjects/KladionicaRazlike/src/test/resources/json/data3.json")) {
            fileWriter.write(json);
            fileWriter.flush();
            System.out.println("upsao");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Then("sort data en write in excel")
    public void sort_data_en_write_in_excel() throws IOException {

        Match[] matchArray = objectMapper.readValue(new File("src/test/resources/json/data3.json"), Match[].class);
        for (int i = 0; i < matchArray.length - 1; i++) {
            for (int j = i + 1; j < matchArray.length; j++) {

                double iValueOne = Double.parseDouble(matchArray[i].getOne());
                double iMax = iValueOne;
                double iValueTwo = Double.parseDouble(matchArray[i].getTwo());
                if (iMax < iValueTwo){
                    iMax = iValueTwo;
                }
                double iValueX = Double.parseDouble(matchArray[i].getX());
                if (iMax < iValueX){
                    iMax = iValueX;
                }

                double jValueOne = Double.parseDouble(matchArray[j].getOne());
                double jMax = jValueOne;
                double jValueTwo = Double.parseDouble(matchArray[j].getTwo());
                if (jMax < jValueTwo){
                    jMax = jValueTwo;
                }
                double jValueX = Double.parseDouble(matchArray[j].getX());
                if (jMax < jValueX){
                    jMax = jValueX;
                }
                //|| jValueTwo > iValue || jValueX > iValue
                if (jMax > iMax) {
                    Match temp = matchArray[i];
                    matchArray[i] = matchArray[j];
                    matchArray[j] = temp;

                }
            }
        }
        writeInExcel(matchArray);
//        sendEmail();
    }

    static Match differences(Match matchHomeBetting, Match matchForeignBetting) {

        Match match = new Match();
        double one = Double.parseDouble(matchHomeBetting.getOne()) - Double.parseDouble(matchForeignBetting.getOne());
        match.setOne(Double.toString(Math.round(one * 100.0) / 100.0));
        double two = Double.parseDouble(matchHomeBetting.getTwo()) - Double.parseDouble(matchForeignBetting.getTwo());
        match.setTwo(Double.toString(Math.round(two * 100.0) / 100.0));
        double x = Double.parseDouble(matchHomeBetting.getX()) - Double.parseDouble(matchForeignBetting.getX());
        match.setX(Double.toString(Math.round(x * 100.0) / 100.0));
        match.setName(matchHomeBetting.getName());
        match.setDate(matchHomeBetting.getDate());
        match.setTime(matchHomeBetting.getTime());


        return match;

    }

    static void writeInExcel(Match[] matchDifferences) {


        XSSFWorkbook workbook = new XSSFWorkbook();
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH mm ss");
        DecimalFormat df = new DecimalFormat("#.##"); // format kvota
        XSSFSheet sheet = workbook.createSheet(time.format(formatter));//formira shit
        sheet.setColumnWidth(3, 10000); // širi kolonu
        int rownum = 0;
        Row row = sheet.createRow(rownum++);
        int cellnum = 1;
        Cell cell = row.createCell(cellnum++);
        cell.setCellValue("Datum");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Vreme");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Naziv");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Kec");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Razlika Kec");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Dvojka");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Razlika Dvojka");
        cell = row.createCell(cellnum++);
        cell.setCellValue("X");


        for (int i = 0; i < matchDifferences.length; i++) {


            row = sheet.createRow(rownum++);
            Match match = matchDifferences[i];
            int cellnum1 = 1;
            Cell cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getDate());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getTime());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getName());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getOne());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getTwo());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getTwo());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getTwo());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getX());

        }
        try {
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd-MM-yyyy");//format datuma
            FileOutputStream out = new FileOutputStream(new File(date.format(formatterData) + " - " + time.format(formatter) + ".xlsx"));
            workbook.write(out);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




//    static void sendEmail() {
//
//        String host = "smtp.gmail.com";
//        String korisnickoIme = "markomilosevic@lumenspei.com";
//        String lozinka = "Donjev.018";
//
//        //Postavke primatelja
//        String primatelj = "marko.milosevic2202@gmail.com";
//
//        //Postavke privitka
//        String putanjaDoPrivitka = "/home/marko/IdeaProjects/KladionicaRazlike/12-05-2023 - 15 57 05.xlsx";
//
//        //Postavke poruke
//        String naslov = "Primjer e-maila sa privitkom";
//        String tekstPoruke = "Ovo je primjer e-maila sa privitkom.";
//
//        //Postavke sesije
//        Properties properties = System.getProperties();
//        properties.setProperty("mail.smtp.host", host);
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.port", "465");
//        properties.put("mail.smtp.socketFactory.port", "465");
//        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//
//        //Autentifikacija korisnika
//        Authenticator authenticator = new Authenticator() {
//            public PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(korisnickoIme, "Donjev.018");
//            }
//        };
//
//        //Slanje e-maila
//        try{
//            //Sesija
//            Session session = Session.getDefaultInstance(properties);
//
//            //Nova poruka
//            MimeMessage message = new MimeMessage(session);
//
//            //Postavke pošiljatelja
//            message.setFrom(new InternetAddress(korisnickoIme));
//
//            //Postavke primatelja
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(primatelj));
//
//            //Postavke naslova i sadržaja
//            message.setSubject(naslov);
//            message.setText(tekstPoruke);
//
//            //Stvaranje privitka
//            BodyPart attachment = new MimeBodyPart();
//            DataSource source = new FileDataSource(putanjaDoPrivitka);
//            attachment.setDataHandler(new DataHandler(source));
//            attachment.setFileName(putanjaDoPrivitka);
//
//            //Spajanje privitka sa porukom
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(attachment);
//            message.setContent(multipart);
//
//            //Slanje poruke
//            Transport.send(message);
//            System.out.println("Poruka poslana!");
//        }catch (MessagingException mex) {
//            mex.printStackTrace();
//        }
//    }

}


