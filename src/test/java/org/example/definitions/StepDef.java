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


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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

    private Actions actions;

    private MaxBet maxBet;

    private Foreign foreign;

    private Meridian meridian;

    private Mozzart mozzart;

    private ObjectMapper objectMapper;

    private String hours;

    static String nameFileGlobal;

    static int numberMatchesHome;
    static int numberMatchesForeign;
    static int  numberMatchesBingo;
    static int numberMatchesNotFound;


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
        actions = new Actions(driver);
        maxBet = new MaxBet(driver);
        foreign = new Foreign(driver);
        meridian = new Meridian(driver);
        mozzart = new Mozzart(driver);
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
       // new WebDriverWait(driver, Duration.ofSeconds(30)).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        driver.get(address);
        driver.navigate().refresh();
    }


    @When("choose which period you are watching {string}")
    public void choose_which_period_you_are_watching(String hoursPage) throws InterruptedException {
        hours = hoursPage;
        maxBet.clickSlider(hoursPage);
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
        List<Match> matches = foreign.findMatches("48");
        writeJsonFileMatchList(matches, "foreignBetting");
    }
    @When("click on the page meridian button football")
    public void click_on_the_page_meridian_button_football() throws InterruptedException {
        Thread.sleep(2000);
        meridian.clickFootball();
    }
    @When("click on the page meridian button {string}")
    public void click_on_the_page_meridian_button(String time) {
        meridian.clickTime(time);
    }
    @When("wait for the whole page to load meridian")
    public void wait_for_the_whole_page_to_load_meridian() throws InterruptedException {
       meridian.waitForPageToLoad();
    }
    @When("click on the page mozzart button {string}")
    public void click_on_the_page_mozzart_button(String time) {
       mozzart.clickTime(time);
    }
    @When("click on the page mozzart button football")
    public void click_on_the_page_mozzart_button_football() {
        mozzart.clickFootball();
    }
    @When("wait for the whole page to load mozzart")
    public void wait_for_the_whole_page_to_load_mozzart() throws InterruptedException {
       mozzart.waitForPageToLoad();
    }




    @Then("write all match in document")
    public void write_all_match_in_document() {
        List<Match> matches = maxBet.writeMatch();
        writeJsonFileMatchList(matches, "homeBetting");
    }

    @Then("write bonus match in document")
    public void write_bonus_match_in_document() {
        List<Match> matches = maxBet.writeBonusMatch();
        writeJsonFileMatchList(matches, "homeBonusBetting");

    }
    @Then("write bonus match in document meridian")
    public void write_bonus_match_in_document_meridian() {
        List<Match> matches = meridian.writeMatch();
        writeJsonFileMatchList(matches, "meridianBetting");

    }

    @Then("compare odds")
    public void compare_odds() throws IOException {

        Match[] matchesHomeBetting = objectMapper.readValue(new File("src/test/resources/json/homeBetting.json"), Match[].class);
        Match[] matchesForeignBetting = objectMapper.readValue(new File("src/test/resources/json/foreignBetting.json"), Match[].class);
        compare(matchesHomeBetting, matchesForeignBetting, "ordinaryQuotaDifferences");


    }

    @Then("sort data en write in excel")
    public void sort_data_en_write_in_excel() throws IOException {
        MatchDifferences[] matchArray = objectMapper.readValue(new File("src/test/resources/json/ordinaryQuotaDifferences.json"), MatchDifferences[].class);
        writeInExcel(sort(matchArray), "Ordinary");
    }
    @Then("sort data en write in excel bonus odds")
    public void sort_data_en_write_in_excel_bonus_odds() throws IOException {
        MatchDifferences[] matchArray = objectMapper.readValue(new File("src/test/resources/json/bonusQuotaDifferences.json"), MatchDifferences[].class);
        writeInExcel(sort(matchArray), "Bonus");
    }
    @Then("sort data en write in excel Meridian odds")
    public void sort_data_en_write_in_excel_Meridian_odds() throws IOException {
        MatchDifferences[] matchArray = objectMapper.readValue(new File("src/test/resources/json/plusMeridianBetting.json"), MatchDifferences[].class);
        writeInExcel(sortEarnings(matchArray), "Meridian");
    }
    @Then("sort data en write in excel bonus odds plus")
    public void sort_data_en_write_in_excel_bonus_odds_plus() throws IOException {
        MatchDifferences[] matchArray = objectMapper.readValue(new File("src/test/resources/json/plusBonusQuotaDifferences.json"), MatchDifferences[].class);
        writeInExcel(sortEarnings(matchArray), "Bonus");
    }
    @Then("sort data en write in excel ordinary odds plus")
    public void sort_data_en_write_in_excel_ordinary_odds_plus() throws IOException {
        MatchDifferences[] matchArray = objectMapper.readValue(new File("src/test/resources/json/ordinaryQuotaDifferencesClear.json"), MatchDifferences[].class);
        writeInExcel(sortEarnings(matchArray), "Ordinary");
    }
    @Then("compare bonus odds")
    public void compare_bonus_odds() throws IOException{
        Match[] matchesHomeBettingBonus = objectMapper.readValue(new File("src/test/resources/json/homeBonusBetting.json"), Match[].class);
        Match[] matchesForeignBetting = objectMapper.readValue(new File("src/test/resources/json/foreignBetting.json"), Match[].class);
        compare(matchesHomeBettingBonus, matchesForeignBetting, "bonusQuotaDifferences");

    }
    @Then("compare meridian odds")
    public void compare_meridian_odds() throws IOException{
        Match[] matchesHomeBettingBonus = objectMapper.readValue(new File("src/test/resources/json/meridianBetting.json"), Match[].class);
        Match[] matchesForeignBetting = objectMapper.readValue(new File("src/test/resources/json/foreignBetting.json"), Match[].class);
       // compareMeridian(matchesHomeBettingBonus, matchesForeignBetting, "meridianQuotaDifferences");

    }
    @Then("find all the opposite odds")
    public void find_all_the_opposite_odds() throws IOException, InterruptedException {
        MatchDifferences[] matchArray = objectMapper.readValue(new File("src/test/resources/json/bonusQuotaDifferences.json"), MatchDifferences[].class);

        writeJsonFileMatchDifferencesList(Arrays.asList(foreign.addOppositeOdds(matchArray)), "plusBonusQuotaDifferences");

    }
    @Then("find all the opposite odds Meridian")
    public void find_all_the_opposite_odds_Meridian() throws IOException, InterruptedException {
        MatchDifferences[] matchArray = objectMapper.readValue(new File("src/test/resources/json/meridianQuotaDifferencesClear.json"), MatchDifferences[].class);

        writeJsonFileMatchDifferencesList(Arrays.asList(foreign.addOppositeOdds(matchArray)), "plusMeridianBetting");

    }
    @Then("send email")
    public void send_email() {
        sendEmail();
    }

    @Then("clear list")
    public void clear_list() throws IOException {
        MatchDifferences[] list = objectMapper.readValue(new File("src/test/resources/json/ordinaryQuotaDifferences.json"), MatchDifferences[].class);
        System.out.println( "Pocetna: " + list.length);

        writeJsonFileMatchDifferencesList( clearList(list),"ordinaryQuotaDifferencesClear");


    }
    @Then("clear list Meridian")
    public void clear_list_Meridian() throws IOException {
        MatchDifferences[] list = objectMapper.readValue(new File("src/test/resources/json/meridianQuotaDifferences.json"), MatchDifferences[].class);
        System.out.println( "Pocetna: " + list.length);

        writeJsonFileMatchDifferencesList( clearList(list),"meridianQuotaDifferencesClear");


    }
    @Then("find all the opposite odds for ordinary match")
    public void find_all_the_opposite_odds_for_ordinary_match() throws IOException {
        MatchDifferences[] matchArray = objectMapper.readValue(new File("src/test/resources/json/ordinaryQuotaDifferencesClear.json"), MatchDifferences[].class);

        writeJsonFileMatchDifferencesList(Arrays.asList(foreign.addOppositeOdds(matchArray)), "ordinaryQuotaDifferencesClear");
    }
    @Then("find all the opposite odds for Meridian match")
    public void find_all_the_opposite_odds_for_Meridian_ordinary_match() throws IOException {
        MatchDifferences[] matchArray = objectMapper.readValue(new File("src/test/resources/json/meridianQuotaDifferencesClear.json"), MatchDifferences[].class);

        writeJsonFileMatchDifferencesList(Arrays.asList(foreign.addOppositeOdds(matchArray)), "meridianQuotaDifferencesClear");
    }
    @Then("write bonus match in document mozzart")
    public void write_bonus_match_in_document_mozzart() {
        List<Match> matches = mozzart.writeMatch();
        writeJsonFileMatchList(matches, "mozzartBetting");
    }

    static MatchDifferences differences(Match matchHomeBetting, Match matchForeignBetting, String comparison) {

        MatchDifferences matchDifferences = new MatchDifferences();
        matchDifferences.setNameForeign(matchForeignBetting.getName());
        matchDifferences.setComparison(comparison);
        matchDifferences.setTime(matchHomeBetting.getTime());
        matchDifferences.setCodeHome(matchHomeBetting.getCode());
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

    static void writeInExcel(MatchDifferences[] matchDifferences, String nameFile) {


        XSSFWorkbook workbook = new XSSFWorkbook();
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH mm ss");
        DecimalFormat df = new DecimalFormat("#.##"); // format kvota
        XSSFSheet sheet = workbook.createSheet(time.format(formatter));//formira shit
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(5, 8000);
        sheet.setColumnWidth(15, 8000);// širi kolonu// širi kolonu
        int rownum = 0;
        Row row = sheet.createRow(rownum++);
        int cellnum = 1;

        Cell cell = row.createCell(2);
        cell.setCellValue("Br. Domacih");
        cell = row.createCell(3);
        cell.setCellValue(numberMatchesHome);
        row = sheet.createRow(rownum++);
        cell = row.createCell(2);
        cell.setCellValue("Br. Stranih");
        cell = row.createCell(3);
        cell.setCellValue(numberMatchesForeign);
        row = sheet.createRow(rownum++);
        cell = row.createCell(2);
        cell.setCellValue("Br. Nadjenih");
        cell = row.createCell(3);
        cell.setCellValue(numberMatchesBingo);
        row = sheet.createRow(rownum++);
        cell = row.createCell(2);
        cell.setCellValue("Br. Nenadjenih");
        cell = row.createCell(3);
        cell.setCellValue(numberMatchesNotFound);


        cellnum = 1;
        row = sheet.createRow(rownum++);
        row = sheet.createRow(rownum++);
        row = sheet.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Sifra Domaca");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Uhvacen");
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
        cell = row.createCell(cellnum++);
        cell.setCellValue("Ime Strna");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Kontra kvota");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Kvota Domaca");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Ulog");
        cell = row.createCell(cellnum++);
        cell.setCellValue("Zarada");


        for (int i = 0; i < matchDifferences.length; i++) {


            row = sheet.createRow(rownum++);
            MatchDifferences match = matchDifferences[i];
            int cellnum1 = 1;
            Cell cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getCodeHome());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getComparison());
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
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getNameForeign());

            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getCounterQuota());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getHigherOdds());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getBet());
            cell1 = row.createCell(cellnum1++);
            cell1.setCellValue(match.getEarnings());
            cell1 = row.createCell(cellnum1++);



        }
        try {
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd-MM-yyyy");//format datuma
            FileOutputStream out = new FileOutputStream(new File(nameFile + date.format(formatterData) + " - " + time.format(formatter) + ".xlsx"));
            nameFileGlobal = nameFile + date.format(formatterData) + " - " + time.format(formatter) + ".xlsx";
            workbook.write(out);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


        static void sendEmail() {


                String host = "mail.lumenspei.com";
                int port = 587;
                String username = "marko.milosevic@lumenspei.com";
                String password = "Donjev.018";


                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);

                // Kreiranje sesije
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                try {

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("pedjoni018@yahoo.com"));
                    message.setSubject("Testna poruka");
                    message.setText("Ovo je testna poruka poslana putem JavaMail API1.");

                    Multipart multipart = new MimeMultipart();

                    MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                    String filePath = nameFileGlobal;
                    attachmentBodyPart.attachFile(filePath);
                    multipart.addBodyPart(attachmentBodyPart);
                    message.setContent(multipart);

                    Transport.send(message);

                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }



    public void compare(Match[] matchesHomeBetting, Match[] matchsForeignBetting, String nameFile) throws IOException {


        List<MatchDifferences> matchesBingo = new ArrayList<>();
        List<Match> matchesDiscard = new ArrayList<>();


        for (int i = 0; i < matchesHomeBetting.length; i++) {

            Match matchHomeBetting = matchesHomeBetting[i];
            String nameHomeBetting = matchHomeBetting.getName();
            String dateHomeBetting = matchHomeBetting.getDate();
            String timeHomeBetting = matchHomeBetting.getTime();
            String nameHomeHomeBetting = nameHomeBetting.substring(0, nameHomeBetting.indexOf(" -"));
            String nameGuestHomeBetting = nameHomeBetting.substring(nameHomeBetting.lastIndexOf("- ") + 1);
            Boolean bingo = false;


            for (int j = 0; j < matchsForeignBetting.length; j++) {
                Match matchForeignBetting = matchsForeignBetting[j];
                String nameForeignBetting = matchForeignBetting.getName();
                String nameHomeForeignBetting = nameForeignBetting.substring(0, nameForeignBetting.indexOf(" -"));
                String nameGuestForeignBetting = nameForeignBetting.substring(nameForeignBetting.lastIndexOf("- ") + 1);
                String timeForeignBetting = matchForeignBetting.getTime();
                String dateForeignBetting = matchForeignBetting.getDate().substring(0, 5);

                if (nameForeignBetting.contains(nameHomeHomeBetting) && nameForeignBetting.contains(nameGuestHomeBetting) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
//
                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home guest"));
                    bingo = true;
                    break;
                } else if (nameHomeForeignBetting.contains(nameHomeHomeBetting) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
//
                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home"));
                    bingo = true;
                    break;
                } else if (nameGuestForeignBetting.contains(nameGuestHomeBetting) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {

                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest"));
                    bingo = true;
                    break;
                }
                try {


                    if (nameHomeForeignBetting.contains(nameHomeHomeBetting.substring(0, 6)) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home6"));
                        bingo = true;
                        break;
                    } else if (nameGuestForeignBetting.contains(nameGuestHomeBetting.substring(0, 6)) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest6"));
                        bingo = true;
                        break;
                    }
                } catch (Throwable t) {

                }


                try {


                    if (nameHomeForeignBetting.contains(nameHomeHomeBetting.substring(0, 5)) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home5"));
                        bingo = true;
                        break;
                    } else if (nameGuestForeignBetting.contains(nameGuestHomeBetting.substring(0, 5)) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest5"));
                        bingo = true;
                        break;
                    }
                } catch (Throwable t) {

                }

                try {


                    if (nameHomeForeignBetting.contains(nameHomeHomeBetting.substring(0, 4)) && nameGuestForeignBetting.contains(nameGuestHomeBetting.substring(0,2)) && timeForeignBetting.contains(timeHomeBetting)) {
                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home4go"));
                        bingo = true;
                        break;
                    } else if (nameGuestForeignBetting.contains(nameGuestHomeBetting.substring(0, 4)) && nameHomeForeignBetting.contains(nameHomeHomeBetting.substring(0,2)) && timeForeignBetting.contains(timeHomeBetting)) {
                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest4go"));
                        bingo = true;
                        break;
                    }
                } catch (Throwable t) {

                }
                try {


                    if (nameHomeForeignBetting.contains(nameHomeHomeBetting.substring(0, 4)) && timeForeignBetting.contains(timeHomeBetting)) {
                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home4"));
                        bingo = true;
                        break;
                    } else if (nameGuestForeignBetting.contains(nameGuestHomeBetting.substring(0, 4)) && timeForeignBetting.contains(timeHomeBetting)) {
                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest4"));
                        bingo = true;
                        break;
                    }
                } catch (Throwable t) {

                }
            }


            if (bingo.equals(false)) {
                matchesDiscard.add(matchHomeBetting);
            }

        }
        numberMatchesHome = matchesHomeBetting.length;
        numberMatchesForeign = matchsForeignBetting.length;
        numberMatchesBingo = matchesBingo.size();
        numberMatchesNotFound = matchesDiscard.size();

        writeJsonFileMatchDifferencesList(matchesBingo, nameFile);
    }

    public MatchDifferences[] sort(MatchDifferences[] matchArray) throws IOException {


        for (int i = 0; i < matchArray.length - 1; i++) {
            for (int j = i + 1; j < matchArray.length; j++) {

                double iValueOne = Double.parseDouble(matchArray[i].getOneDifferences());
                double iMax = iValueOne;
                double iValueTwo = Double.parseDouble(matchArray[i].getTwoDifferences());
                if (iMax < iValueTwo) {
                    iMax = iValueTwo;
                }
//                double iValueX = Double.parseDouble(matchArray[i].getXDifferences());
//                if (iMax < iValueX){
//                    iMax = iValueX;
//                }

                double jValueOne = Double.parseDouble(matchArray[j].getOneDifferences());
                double jMax = jValueOne;
                double jValueTwo = Double.parseDouble(matchArray[j].getTwoDifferences());
                if (jMax < jValueTwo) {
                    jMax = jValueTwo;
                }
//                double jValueX = Double.parseDouble(matchArray[j].getXDifferences());
//                if (jMax < jValueX){
//                    jMax = jValueX;
//                }
                //|| jValueTwo > iValue || jValueX > iValue
                if (jMax > iMax) {
                    MatchDifferences temp = matchArray[i];
                    matchArray[i] = matchArray[j];
                    matchArray[j] = temp;

                }
            }
        }
        return matchArray;
    }
    public MatchDifferences[] sortEarnings(MatchDifferences[] matchArray) throws IOException {


        for (int i = 0; i < matchArray.length - 1; i++) {

            for (int j = 0; j < matchArray.length - i - 1; j++) {
                if ( Double.parseDouble(matchArray[j].getEarnings()) <  Double.parseDouble(matchArray[j + 1].getEarnings())) {

                    MatchDifferences temp = matchArray[j];
                    matchArray[j] = matchArray[j + 1];
                    matchArray[j + 1] = temp;
                }
            }
        }
        return matchArray;
    }

    public void writeJsonFileMatchList(List<Match> matches, String nameFile) {
        Gson gson = new Gson();
        String json = gson.toJson(matches);
        try (FileWriter fileWriter = new FileWriter("src/test/resources/json/" + nameFile + ".json")) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeJsonFileMatchDifferencesList(List<MatchDifferences> matches, String nameFile) {
        Gson gson = new Gson();
        String json = gson.toJson(matches);
        try (FileWriter fileWriter = new FileWriter("src/test/resources/json/" + nameFile + ".json")) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<MatchDifferences> clearList(MatchDifferences[] list){
        int a = 0;
        List<MatchDifferences> listaMatcheva = new ArrayList<>(Arrays.asList(list));
        for (int i = 0; i < listaMatcheva.size(); i++) {

       MatchDifferences matchDifferences = listaMatcheva.get(i);
            Double quotaOne = Double.parseDouble(matchDifferences.getOneDifferences());
            Double quotaTwo = Double.parseDouble(matchDifferences.getTwoDifferences());
            Double quotaMax = quotaOne;
            if (quotaTwo > quotaOne){
                quotaMax = quotaTwo;

            }
            if (quotaOne > 0.5 && quotaTwo > 0.5){
                listaMatcheva.remove(i);
                i =-1;

               }
            else if(quotaMax > 1){
                listaMatcheva.remove(i);
                i =-1;
            }
            else if(quotaMax < 0.02){
                listaMatcheva.remove(i);
                i =-1;
            }
       }
        System.out.println(listaMatcheva.size());

        return listaMatcheva;
    }
//    public void compareMeridian(Match[] matchesHomeBetting, Match[] matchsForeignBetting, String nameFile) throws IOException {
//
//        System.out.println(matchesHomeBetting.length);
//        System.out.println(matchsForeignBetting.length);
//        List<MatchDifferences> matchesBingo = new ArrayList<>();
//        List<Match> matchesDiscard = new ArrayList<>();
//
//
//        for (int i = 0; i < matchesHomeBetting.length; i++) {
//
//            Match matchHomeBetting = matchesHomeBetting[i];
//            String nameHomeBetting = matchHomeBetting.getName();
//            String dateHomeBetting = matchHomeBetting.getDate();
//            String timeHomeBetting = matchHomeBetting.getTime();
//            String nameHomeHomeBetting1 = nameHomeBetting.substring(0, nameHomeBetting.indexOf(" -"));
//            String[] rijeci = nameHomeHomeBetting1.split(" ");
//            String nameHomeHomeBetting = "";
//            for (String rijec : rijeci) {
//                if (rijec.length() > nameHomeHomeBetting.length()) {
//                    nameHomeHomeBetting = rijec;
//                }
//            }
//            String nameGuestHomeBetting1 = nameHomeBetting.substring(nameHomeBetting.lastIndexOf("- ") + 1);
//            String[] gostHome = nameGuestHomeBetting1.split(" ");
//            String nameGuestHomeBetting = gostHome[0];
//            for (String gostHome1 : gostHome) {
//                if (gostHome1.length() > nameGuestHomeBetting.length()) {
//                    nameGuestHomeBetting = gostHome1;
//                }
//            }
//            Boolean bingo = false;
//
//
//            for (int j = 0; j < matchsForeignBetting.length; j++) {
//                Match matchForeignBetting = matchsForeignBetting[j];
//                String nameForeignBetting = matchForeignBetting.getName();
//                String nameHomeForeignBetting1 = nameForeignBetting.substring(0, nameForeignBetting.indexOf(" -"));
//                String[] rijeci3 = nameHomeForeignBetting1.split(" ");
//                String nameHomeForeignBetting = "";
//                for (String rijec3 : rijeci3) {
//                    if (rijec3.length() > nameHomeForeignBetting.length()) {
//                        nameHomeForeignBetting = rijec3;
//                    }
//                }
//                String nameGuestForeignBetting1 = nameForeignBetting.substring(nameForeignBetting.lastIndexOf("- ") + 1);
//
//                String[] rijeci4 = nameGuestForeignBetting1.split(" ");
//                String nameGuestForeignBetting = "";
//                for (String rijec4 : rijeci4) {
//                    if (rijec4.length() > nameGuestForeignBetting.length()) {
//                        nameGuestForeignBetting = rijec4;
//                    }
//                }
//                String timeForeignBetting = matchForeignBetting.getTime();
//                String dateForeignBetting = matchForeignBetting.getDate().substring(0, 5);
//
//
//
//                if (nameForeignBetting.contains(nameHomeHomeBetting) && nameForeignBetting.contains(nameGuestHomeBetting) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
////
//                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home guest"));
//                    bingo = true;
//                    break;
//                } else if (nameHomeForeignBetting.contains(nameHomeHomeBetting) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
////
//                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home"));
//                    bingo = true;
//                    break;
//                } else if (nameGuestForeignBetting.contains(nameGuestHomeBetting) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
//
//                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest"));
//                    bingo = true;
//                    break;
//                }
//                try {
//
//
//                    if (nameHomeForeignBetting.contains(nameHomeHomeBetting.substring(0, 6)) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
//                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home6"));
//                        bingo = true;
//                        break;
//                    } else if (nameGuestForeignBetting.contains(nameGuestHomeBetting.substring(0, 6)) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
//                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest6"));
//                        bingo = true;
//                        break;
//                    }
//                } catch (Throwable t) {
//
//                }
//
//
//                try {
//
//
//                    if (nameHomeForeignBetting.contains(nameHomeHomeBetting.substring(0, 5)) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
//                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home5"));
//                        bingo = true;
//                        break;
//                    } else if (nameGuestForeignBetting.contains(nameGuestHomeBetting.substring(0, 5)) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
//                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest5"));
//                        bingo = true;
//                        break;
//                    }
//                } catch (Throwable t) {
//
//                }
//
//                try {
//
//
//                    if (nameHomeForeignBetting.contains(nameHomeHomeBetting.substring(0, 4)) && nameGuestForeignBetting.contains(nameGuestHomeBetting.substring(0,2)) && timeForeignBetting.contains(timeHomeBetting)) {
//                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home4go"));
//                        bingo = true;
//                        break;
//                    } else if (nameGuestForeignBetting.contains(nameGuestHomeBetting.substring(0, 4)) && nameHomeForeignBetting.contains(nameHomeHomeBetting.substring(0,2)) && timeForeignBetting.contains(timeHomeBetting)) {
//                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest4go"));
//                        bingo = true;
//                        break;
//                    }
//                } catch (Throwable t) {
//
//                }
//                try {
//
//
//                    if (nameHomeForeignBetting.contains(nameHomeHomeBetting.substring(0, 4)) && timeForeignBetting.contains(timeHomeBetting)) {
//                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home4"));
//                        bingo = true;
//                        break;
//                    } else if (nameGuestForeignBetting.contains(nameGuestHomeBetting.substring(0, 4)) && timeForeignBetting.contains(timeHomeBetting)) {
//                        matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest4"));
//                        bingo = true;
//                        break;
//                    }
//                } catch (Throwable t) {
//
//                }
//
//
//
//
//
//            if (nameHomeHomeBetting.contains(nameForeignBetting) && nameGuestHomeBetting.contains(nameForeignBetting) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
////
//                matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home guest"));
//                bingo = true;
//                break;
//            } else if (nameHomeHomeBetting.contains(nameHomeForeignBetting) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
////
//                matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home"));
//                bingo = true;
//                break;
//            } else if (nameGuestHomeBetting.contains(nameGuestForeignBetting) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
//
//                matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest"));
//                bingo = true;
//                break;
//            }
//            try {
//
//
//                if (nameHomeHomeBetting.contains(nameHomeForeignBetting.substring(0, 6)) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
//                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home6"));
//                    bingo = true;
//
//                } else if (nameGuestHomeBetting.contains(nameGuestForeignBetting.substring(0, 6)) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
//                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest6"));
//                    bingo = true;
//                    break;
//                }
//            } catch (Throwable t) {
//
//            }
//
//
//            try {
//
//
//                if (nameHomeHomeBetting.contains(nameHomeForeignBetting.substring(0, 5)) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
//                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home5"));
//                    bingo = true;
//
//                } else if (nameGuestHomeBetting.contains(nameGuestForeignBetting.substring(0, 5)) && timeForeignBetting.contains(timeHomeBetting) && dateHomeBetting.equals(dateForeignBetting)) {
//                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest5"));
//                    bingo = true;
//                    break;
//                }
//            } catch (Throwable t) {
//
//            }
//
//            try {
//
//
//                if (nameHomeHomeBetting.contains(nameHomeForeignBetting.substring(0, 4)) && nameGuestForeignBetting.contains(nameGuestHomeBetting.substring(0,2)) && timeForeignBetting.contains(timeHomeBetting)) {
//                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home4go"));
//                    bingo = true;
//                    break;
//                } else if (nameGuestHomeBetting.contains(nameGuestForeignBetting.substring(0, 4)) && nameHomeForeignBetting.contains(nameHomeHomeBetting.substring(0,2)) && timeForeignBetting.contains(timeHomeBetting)) {
//                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest4go"));
//                    bingo = true;
//                    break;
//                }
//            } catch (Throwable t) {
//
//            }
//            try {
//
//
//                if (nameHomeHomeBetting.contains(nameHomeForeignBetting.substring(0, 4)) && timeForeignBetting.contains(timeHomeBetting)) {
//                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "home4"));
//                    bingo = true;
//                    break;
//                } else if (nameGuestHomeBetting.contains(nameGuestForeignBetting.substring(0, 4)) && timeForeignBetting.contains(timeHomeBetting)) {
//                    matchesBingo.add(differences(matchHomeBetting, matchForeignBetting, "guest4"));
//                    bingo = true;
//                    break;
//                }
//            } catch (Throwable t) {
//
//            }
//        }
//
//
//            if (bingo.equals(false)) {
//                matchesDiscard.add(matchHomeBetting);
//            }
//
//        }
//        numberMatchesHome = matchesHomeBetting.length;
//        numberMatchesForeign = matchsForeignBetting.length;
//        numberMatchesBingo = matchesBingo.size();
//        numberMatchesNotFound = matchesDiscard.size();
//
//        writeJsonFileMatchDifferencesList(matchesBingo, nameFile);
//    }


}


