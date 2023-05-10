package org.example.page_factory;

import com.google.gson.Gson;
import org.example.test_data_bils.Match;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Foreign {

    @FindBy(xpath = "//*[contains(@class, 'biab_table-wrapper')]")
    List<WebElement> listMatchWeb;
    @FindBy(xpath = "//a[contains(text(),'Next')]")
    WebElement btnNext;
    WebDriver driver;

    public Foreign(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void findMatches() throws InterruptedException {
        List<Match> list  = new ArrayList<>();
        for (int i = 0; i < 6; i++) {

            Thread.sleep(2000);
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//          wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("biab_table-wrapper")));
           List <WebElement> time = driver.findElements(By.xpath("//*[contains(@class, 'biab_market-time')]"));
           List<WebElement> name = driver.findElements(By.xpath("//*[contains(@class, 'biab_market-title-team-names js-teams')]"));
            List<WebElement> quotas = driver.findElements(By.xpath("//*[contains(@class, 'js-odds biab_odds')]"));


            for (int j = 0; j < 20; j++) {
                Match match = new Match();
//                match.setDate();
                match.setTime(time.get(i).getText().replaceAll("[a-zA-Z]", ""));
                match.setName(name.get(i).getText());
                match.setOne(quotas.get(i+1).getText());
                match.setTwo(quotas.get(i+2).getText());
                match.setX(quotas.get(i+2).getText());
                list.add(match);

            }
            btnNext.click();
        }
        Gson gson = new Gson();
        String json = gson.toJson(list);
        try (FileWriter fileWriter = new FileWriter("/home/marko/IdeaProjects/KladionicaRazlike/src/test/resources/json/data2.json")) {
            fileWriter.write(json);
            fileWriter.flush();
            System.out.println("upsao");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        }





