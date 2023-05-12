package org.example.definitions;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.page_factory.*;
import org.example.test_data_bils.DataFile;
import org.example.test_data_bils.Match;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class StepDef {


    public static WebDriver driver;

    public static String baseUrl = "http://178.220.24.117:9914//";


    DataFile dataFile = new DataFile();

    Actions actions;

    MaxBet maxBet;
    Foreign foreign;



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
        List<Match> objects = mapper.readValue(new File("/home/marko/IdeaProjects/CRMFront/src/test/resources/json/data.json"), new TypeReference<List<Match>>(){});

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
        ObjectMapper objectMapper = new ObjectMapper();
        Match[] matchHome = objectMapper.readValue(new File("src/test/resources/json/data2.json"), Match[].class);
        // Učitamo JSON fajl kao niz objekata tipa Osoba
        Match[] matchOsobe = objectMapper.readValue(new File("src/test/resources/json/data2.json"), Match[].class);

        // Pronađemo objekat sa odgovarajućim parametrima
        Match trazenaOsoba = null;
        for (Match match : matchOsobe) {
            if (match.getName().contains("Shanghai")) {
                trazenaOsoba = match;
                break;
            }
        }

        // Ispisujemo pronađeni objekat
        System.out.println(trazenaOsoba);

        Match[] matchsHome = objectMapper.readValue(new File("src/test/resources/json/data2.json"), Match[].class);

        for (int i = 0; i < matchsHome.length; i++) {

        }
    }

    }


