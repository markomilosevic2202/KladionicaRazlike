package org.example.definitions;


import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.object.Trade;
import org.example.page_factory.*;
import org.example.test_data_bils.DataFile;
import org.junit.jupiter.api.Assertions;
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
import java.util.Random;

import static io.restassured.RestAssured.given;

public class StepDef {


    public static WebDriver driver;

    public static String baseUrl = "http://178.220.24.117:9914//";


    DataFile dataFile = new DataFile();

    Actions actions;



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
        driver.get(baseUrl);
        actions = new Actions(driver);
        dataFile = readerData();


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

        //  Thread.sleep(4000);
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

}
