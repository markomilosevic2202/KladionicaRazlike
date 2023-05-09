package org.example.definitions;


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
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.*;
import java.time.Duration;

import static io.restassured.RestAssured.given;

public class StepDef {


    public static WebDriver driver;

    public static String baseUrl = "http://178.220.24.117:9914//";


    DataFile dataFile = new DataFile();

    Actions actions;

    MaxBet maxBet;



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
    public void afterClass()  {


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
    @Then("write all match in document")
    public void write_all_match_in_document() {
          maxBet.writeMatch();
    }

}
