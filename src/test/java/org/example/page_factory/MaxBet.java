package org.example.page_factory;

import com.google.gson.Gson;
import org.example.test_data_bils.Match;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MaxBet {

    @FindBy(xpath = "//div[contains(text(),'Fudbal')]")
    List<WebElement> listFootball;

    @FindBy(xpath = "/html/body/div[1]/div[5]/div/div[2]/div[1]/div[2]/div[4]/div/div[1]/div[2]/div[1]")
    WebElement btnSelectAll;

    @FindBy(xpath = "//*[contains(text(),'Max Bonus Tip Fudbal ')]")
    WebElement btnMaxBonus;

    @FindBy(xpath = "//*[contains(@class, 'home-game bck-col-1 ng-scope')]")
    List<WebElement> listMatchDiv;

    @FindBy(xpath = "//*[contains(@class, 'ui-slider-handle ui-state-default ui-corner-all')]")
    WebElement slider;


    WebDriver driver;

    public MaxBet(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void clickFootball() {
        listFootball.get(0).click();

    }

    public void clickSelectAll() {
        btnSelectAll.click();
    }

    public void clickMaxBonus() {
        btnMaxBonus.click();

    }

    public void clickSlider(String hours) {

        slider.click();
        if (hours.equals("3")) {
            for (int i = 0; i < 5; i++) {
                slider.sendKeys(Keys.ARROW_LEFT);
            }

        } else if (hours.equals("5")) {
            for (int i = 0; i < 4; i++) {
                slider.sendKeys(Keys.ARROW_LEFT);
            }
        } else if (hours.equals("12")) {
            for (int i = 0; i < 3; i++) {
                slider.sendKeys(Keys.ARROW_LEFT);
            }
        } else if (hours.equals("24")) {
            for (int i = 0; i < 2; i++) {
                slider.sendKeys(Keys.ARROW_LEFT);
            }
        } else if (hours.equals("48")) {
            slider.sendKeys(Keys.ARROW_LEFT);
        } else if (hours.equals("all")) {

        } else {
            Assertions.fail("  :: The specified parameter was not found. The options offered are 3,5,12,24,48,all :: ");
        }


    }

    public void waitForPageToLoad() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement scrollBar = (WebElement) js.executeScript("return document.querySelector('body > div > div:nth-child(3) > div > div > div.slimScrollBar');");
        boolean isScrollBarAtEnd = false;
        // skrolovanje na kraj skrol bara
        while (!isScrollBarAtEnd) {


            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(500);
            isScrollBarAtEnd = (boolean) js.executeScript("return (document.documentElement.scrollHeight - window.innerHeight) === window.scrollY");


        }
    }

    public List<Match> writeMatch() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        List<Match> matches = (List<Match>) js.executeScript(
                "let list = document.querySelectorAll('.home-game.bck-col-1.ng-scope');\n" +
                        "let result = [];\n" +
                        "list.forEach(e=>{\n" +
                        "    result.push({\n" +
                        "        \"code\": e.querySelector('.cc-match-code').innerText,\n" +
                        "        \"name\": e.querySelector('.teams-overflow').textContent.replaceAll('\\n', ''),\n" +
                        "        \"one\": e.getElementsByTagName('odd')[0].outerText,\n" +
                        "        \"x\": e.getElementsByTagName('odd')[1].outerText,\n" +
                        "        \"two\": e.getElementsByTagName('odd')[2].outerText,\n" +
                        "        \"time\": e.querySelector('.cc-match-kickoff').outerText.split(' ')[1],\n" +
                        "        \"date\": e.querySelector('.cc-match-kickoff').outerText.split(' ')[0]\n" +
                        "\n" +
                        "    });\n" +
                        "});\n" +
                        "return result;");
        System.out.println(matches.size());
        return matches;

//        List<Match> listObject = new ArrayList<>();
//        System.out.println(listMatchDiv.size());
//        for (int i = 0; i < listMatchDiv.size(); i++) {
//            WebElement element = listMatchDiv.get(i);
//            Match match = new Match();
//            match.setCode(element.findElement(By.xpath("match/span/div/div[1]")).getText());
//            String fullDate = element.findElement(By.xpath("match/span/div/div[2]")).getText();
////            match.setTime(fullDate.substring(7,12));
//            match.setTime(fullDate.split(" ")[1]);
////            match.setDate(fullDate.substring(0,5));
//            match.setDate(fullDate.split(" ")[0]);
//            match.setName(element.findElement(By.xpath("match/span/div/div[3]")).getText());
//            match.setOne(element.findElement(By.xpath("match/span/div/div[5]/span[1]/odd")).getText());
//            match.setTwo(element.findElement(By.xpath("match/span/div/div[5]/span[1]/odd[2]")).getText());
//            match.setX(element.findElement(By.xpath("match/span/div/div[5]/span[1]/odd[3]")).getText());
//            listObject.add(match);
    }

    public List<Match> writeBonusMatch() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        List<Match> matches = (List<Match>) js.executeScript(
                "let list = document.querySelectorAll('.home-game.bck-col-1.ng-scope');\n" +
                        "let result = [];\n" +
                        "list.forEach(e=>{\n" +
                        "    result.push({\n" +
                        "        \"code\": e.querySelector('.cc-match-code').innerText,\n" +
                        "        \"name\": e.querySelector('.teams-overflow').textContent.replaceAll('\\n', ''),\n" +
                        "        \"one\": e.getElementsByTagName('odd')[0].outerText,\n" +
                        "        \"x\": e.getElementsByTagName('odd')[1].outerText,\n" +
                        "        \"two\": e.getElementsByTagName('odd')[2].outerText,\n" +
                        "        \"time\": e.querySelector('.cc-match-kickoff').outerText.split(' ')[1],\n" +
                        "        \"date\": e.querySelector('.cc-match-kickoff').outerText.split(' ')[0]\n" +
                        "\n" +
                        "    });\n" +
                        "});\n" +
                        "return result;");
        System.out.println(matches.size());
        return matches;


    }
}

