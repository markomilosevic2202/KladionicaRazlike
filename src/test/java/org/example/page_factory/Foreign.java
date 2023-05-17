package org.example.page_factory;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import io.cucumber.java.it.Ma;
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
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Foreign {

    @FindBy(xpath = "//*[contains(@class, 'biab_group-markets-table js-inplay-market')]")
    List<WebElement> listMatchWeb;


    @FindBy(xpath = "//a[contains(text(),'Next')]")
    WebElement btnNext;
    WebDriver driver;

    public Foreign(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }


    public List<Match> findMatches(String hours) throws InterruptedException {
//        Gson gson = new Gson();
//        List<Match> list = new ArrayList<>();
////        for (int i = 0; i < 2; i++) {
//        Thread.sleep(3000);
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        String matchesString = (String) js.executeScript(
//                "let list = [];\n" +
//                        "list = document.querySelectorAll('.biab_group-markets-table.js-inplay-market'); \n" +
//                        "let result = [];\n" +
//                        "list.forEach(e=>{\n" +
//                        "    result.push({\n" +
//                        "                   code: null,\n" +
//                        "                   name: e.querySelector('.biab_market-title-team-names.js-teams').outerText.replace(/\\n/g, ' - '), \n" +
//                        "                   one: e.getElementsByTagName('span')[4].outerText,\n" +
//                        "                   two: e.getElementsByTagName('span')[8].outerText,\n" +
//                        "                   x: e.getElementsByTagName('span')[12].outerText,\n" +
//                        "                   time: e.querySelector('.biab_market-inplay-cell.js-market-inplay-cell').innerText,\n" +
//                        "                   date: null\n" +
//                        "\n" +
//                        "    });\n" +
//                        "});\n" +
//                        "return JSON.stringify(result);"
//        );
//        System.out.println(matchesString);
//        List<Match> matches = gson.fromJson(matchesString, ArrayList.class);
//        System.out.println(matches.size());
//        System.out.println(matches.get(1).toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter formatterText = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH);

        LocalDate today = LocalDate.now();
        String todayString = today.format(formatter);
        LocalDate tomorrow = today.plusDays(1);
        String tomorrowStrong = tomorrow.format(formatter);
        LocalDate afterOneDayTomorrow = today.plusDays(2);
        String afterOneDayTomorrowString = afterOneDayTomorrow.format(formatter);
        String afterOneDayTomorrowStringTextMount = afterOneDayTomorrow.format(formatterText);
        LocalDate afterTwoDayTomorrow = today.plusDays(3);
        String afterTwoTomorrowStringTextMount = afterTwoDayTomorrow.format(formatterText);
        LocalDate afterForeDayTomorrow = today.plusDays(5);
        String afterForeTomorrowStringTextMount = afterForeDayTomorrow.format(formatterText);

        String andDate;

        if (hours.equals("3") || hours.equals("5") || hours.equals("12") || hours.equals("24")) {
            andDate = afterOneDayTomorrowStringTextMount;
        } else if (hours.equals("48")) {
            andDate = afterTwoTomorrowStringTextMount;
        } else {
            andDate = afterForeTomorrowStringTextMount;
        }


        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < 20; i++) {

            Thread.sleep(500);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

            for (int j = 0; j < listMatchWeb.size(); j++) {
                try {
                    String datePage = listMatchWeb.get(j).findElement(By.xpath("tbody/tr/td")).getText();
                    Match match = new Match();
                    match.setTime(datePage);
                    match.setName(listMatchWeb.get(j).findElement(By.xpath("tbody/tr/td[2]")).getText().replace("\n", " - "));
                    match.setOne(listMatchWeb.get(j).findElement(By.xpath("tbody/tr/td[5]/div/div/div/span[1]")).getText());
                    match.setX(listMatchWeb.get(j).findElement(By.xpath("tbody/tr/td[8]/div/div/div/span[1]")).getText());
                    match.setTwo(listMatchWeb.get(j).findElement(By.xpath("tbody/tr/td[11]/div/div/div/span[1]")).getText());
                    if (datePage.contains("Tomorrow")) {
                        match.setDate(tomorrowStrong);
                    } else if (datePage.contains("Wednesday") || datePage.contains("Thursday") || datePage.contains("Friday") || datePage.contains("Saturday")
                            || datePage.contains("Sunday") || datePage.contains("Monday") || datePage.contains("Tuesday")) {
                        match.setDate(afterOneDayTomorrowString);
                    } else match.setDate(todayString);
                    matches.add(match);

                } catch (Throwable e) {

                }
            }
            List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + andDate + "')]"));
            if (list.size() > 0) {
                break;
            }
            btnNext.click();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

         return matches;
    }



}






