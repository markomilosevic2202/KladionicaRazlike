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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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


    public void findMatches() throws InterruptedException {
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

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate afterTomorrow = today.plusDays(2);


        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            Thread.sleep(500);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
//        driver.findElement(By.xpath("//*[contains(@class, 'biab_group-markets-table js-inplay-market')]"));
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
                        match.setDate(tomorrow.toString());
                    } else if (datePage.contains("Wednesday") || datePage.contains("Thursday") || datePage.contains("Friday") || datePage.contains("Saturday")
                            || datePage.contains("Sunday") || datePage.contains("Monday") || datePage.contains("Tuesday")) {
                        match.setDate(afterTomorrow.toString());
                    } else match.setDate(today.toString());
                    matches.add(match);

                } catch (Throwable e) {

                }
            }
            btnNext.click();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        Gson gson = new Gson();
        String json = gson.toJson(matches);
        try (FileWriter fileWriter = new FileWriter("src/test/resources/json/foreignBetting.json")) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//         match1.setName(listMatchWeb.get(i).findElement(By.xpath("//*[contains(@class, 'biab_market-title-team-names js-teams')]")).getText());
//            match1.setTime(listMatchWeb.get(i).findElement(By.xpath("//*[contains(@class, 'biab_market-time')]")).getText());
//            match1.setOne(listMatchWeb.get(i).findElements(By.xpath("//span[contains(@class, 'js-odds biab_odds')]")).get(1).getText());
//            match1.setTwo(listMatchWeb.get(i).findElements(By.xpath("//span[contains(@class, 'js-odds biab_odds')]")).get(5).getText());
//            match1.setOne(listMatchWeb.get(i).findElements(By.xpath("//span[contains(@class, 'js-odds biab_odds')]")).get(3).getText());
}






