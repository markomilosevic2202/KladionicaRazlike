package org.example.definitions;


import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.example.test_data_bils.Match;
import org.example.test_data_bils.MatchDifferences;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


import static io.restassured.RestAssured.given;

public class StepDef {


    public static WebDriver driver;

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
                "src/test/resources/chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        actions = new Actions(driver);
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


    @Given("go to the address {string}")
    public void go_to_the_address(String address) {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        driver.get(address);
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

        Match[] matchHome = objectMapper.readValue(new File("src/test/resources/json/homeBetting.json"), Match[].class);
        Match[] matchForeign = objectMapper.readValue(new File("src/test/resources/json/foreignBetting.json"), Match[].class);

        System.out.println(matchHome.length);
        System.out.println(matchForeign.length);

        List<MatchDifferences> matchesBingo = new ArrayList<>();
        List<Match> matchDiscard = new ArrayList<>();

        for (int i = 0; i < matchHome.length; i++) {

           // Match matchHomeBetting = matchForeign[i];
            String nameHomeBetting = matchHomeBetting.getName().toLowerCase();
            String timeHomeBetting = matchHomeBetting.getTime();
            String nameHomeTime = nameHomeBetting.substring(0, nameHomeBetting.indexOf(" -"));
            String nameForeign = nameHomeBetting.substring(nameHomeBetting.lastIndexOf("- ") + 1);


            for (int j = 0; j < matchForeign.length; j++) {
                Match matchForeignBetting = matchHome[j];
                String nameForeignBetting = matchForeignBetting.getName().toLowerCase();
                String timeForeignBetting = matchForeignBetting.getTime();


                if (nameForeignBetting.contains(nameHomeBetting.substring(0, 4)) && timeHomeBetting.equals(timeForeignBetting)) {
                    System.out.println("1");
                    System.out.println("Domaca: " + timeHomeBetting + " Strana: "+ timeForeignBetting);
                    System.out.println("Domaca: " + nameHomeBetting);
                    System.out.println("Strana: " + nameForeignBetting);
//                    matchesBingo.add(differences(matchHomeBetting, mathForeignBetting));
                    break;
                } else if (nameForeignBetting.contains(nameHomeBetting.substring((nameHomeBetting.length()) - 4, nameHomeBetting.length())) && timeHomeBetting.equals(timeForeignBetting)) {
                    System.out.println("2");
                    System.out.println("Domaca: " + timeHomeBetting + " Strana: "+ timeForeignBetting);
                    System.out.println("Domaca: " + nameHomeBetting);
                    System.out.println("Strana: " + nameForeignBetting);
//                    matchesBingo.add(differences(matchHomeBetting, mathForeignBetting));
                    break;

                } else if (nameForeignBetting.contains(nameHomeTime.substring((nameHomeTime.length()) - 3, nameHomeTime.length())) && timeHomeBetting.equals(timeForeignBetting)) {
                    System.out.println("3");
                    System.out.println("Domaca: " + timeHomeBetting + " Strana: "+ timeForeignBetting);
                    System.out.println("Domaca: " + nameHomeBetting);
                    System.out.println("Strana: " + nameForeignBetting);
//                    matchesBingo.add(differences(matchHomeBetting, mathForeignBetting));
                    break;

                } else if (nameForeignBetting.contains(nameHomeTime.substring(0, 3)) && timeHomeBetting.equals(timeForeignBetting)) {
                    System.out.println("4");
                    System.out.println("Domaca: " + timeHomeBetting + " Strana: "+ timeForeignBetting);
                    System.out.println("Domaca: " + nameHomeBetting);
                    System.out.println("Strana: " + nameForeignBetting);
//                    matchesBingo.add(differences(matchHomeBetting, mathForeignBetting));
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
        try (FileWriter fileWriter = new FileWriter("src/test/resources/json/ordinaryQuotaDifferences.json")) {
            fileWriter.write(json);
            fileWriter.flush();
            System.out.println("upsao");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Then("sort data en write in excel")
    public void sort_data_en_write_in_excel() throws IOException {

        MatchDifferences[] matchArray = objectMapper.readValue(new File("src/test/resources/json/ordinaryQuotaDifferences.json"), MatchDifferences[].class);
//        for (int i = 0; i < matchArray.length - 1; i++) {
//            for (int j = i + 1; j < matchArray.length; j++) {
//
//                double iValueOne = Double.parseDouble(matchArray[i].getOneDifferences());
//                double iMax = iValueOne;
//                double iValueTwo = Double.parseDouble(matchArray[i].getTwoDifferences());
//                if (iMax < iValueTwo){
//                    iMax = iValueTwo;
//                }
//                double iValueX = Double.parseDouble(matchArray[i].getXDifferences());
//                if (iMax < iValueX){
//                    iMax = iValueX;
//                }
//
//                double jValueOne = Double.parseDouble(matchArray[j].getOneDifferences());
//                double jMax = jValueOne;
//                double jValueTwo = Double.parseDouble(matchArray[j].getTwoDifferences());
//                if (jMax < jValueTwo){
//                    jMax = jValueTwo;
//                }
//                double jValueX = Double.parseDouble(matchArray[j].getXDifferences());
//                if (jMax < jValueX){
//                    jMax = jValueX;
//                }
//                //|| jValueTwo > iValue || jValueX > iValue
//                if (jMax > iMax) {
//                    MatchDifferences temp = matchArray[i];
//                    matchArray[i] = matchArray[j];
//                    matchArray[j] = temp;
//
//                }
//            }
//        }
        writeInExcel(matchArray);
//        sendEmail();
    }

    static MatchDifferences differences(Match matchHomeBetting, Match matchForeignBetting) {

        MatchDifferences matchDifferences = new MatchDifferences();
        matchDifferences.setTime(matchHomeBetting.getTime());
//        matchDifferences.setCodeHome(matchHomeBetting.getCode());
        matchDifferences.setDate(matchHomeBetting.getDate());
        matchDifferences.setName(matchHomeBetting.getName());
        matchDifferences.setOneHome(matchHomeBetting.getOne());
        matchDifferences.setOneForeign(matchForeignBetting.getOne());
        double one = Double.parseDouble(matchHomeBetting.getOne()) - Double.parseDouble(matchForeignBetting.getOne());
        matchDifferences.setOneDifferences(Double.toString(Math.round(one * 100.0) / 100.0));

        matchDifferences.setTwoHome(matchHomeBetting.getTwo());
        matchDifferences.setTwoForeign(matchForeignBetting.getTwo());
        double two = Double.parseDouble(matchHomeBetting.getTwo()) - Double.parseDouble(matchForeignBetting.getTwo());
        matchDifferences.setTwoDifferences(Double.toString(Math.round(two * 100.0) / 100.0));

        matchDifferences.setXHome(matchHomeBetting.getX());
        matchDifferences.setXForeign(matchForeignBetting.getX());
        double x = Double.parseDouble(matchHomeBetting.getX()) - Double.parseDouble(matchForeignBetting.getX());
        matchDifferences.setXDifferences(Double.toString(Math.round(x * 100.0) / 100.0));

        return matchDifferences;

    }

    static void writeInExcel(MatchDifferences[] matchDifferences) {


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
        cell.setCellValue("Sifra Domaca");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Datum");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Vreme");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Naziv");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Kec Domaca");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Kec Strana");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Kec Razlika");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Dvojka Domaca");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Dvojka Strana");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Dvojka Razlika");
        cell = row.createCell(cellnum++);
        cell.setCellValue("X Domaca");
        cell = row.createCell(cellnum++);
        cell.setCellValue("X Strana");
        cell = row.createCell(cellnum++);
        cell.setCellValue("X Razlika");


        for (int i = 0; i < matchDifferences.length; i++) {


            row = sheet.createRow(rownum++);
            MatchDifferences match = matchDifferences[i];
            int cellnum1 = 1;
            Cell cell1 = row.createCell(cellnum1++);
            cell1.setCellValue("match.getCodeHome()");
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getDate());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getTime());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getName());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getOneHome());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getOneForeign());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getOneDifferences());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getTwoHome());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getTwoForeign());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getTwoDifferences());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getXHome());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getXForeign());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getXDifferences());

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


