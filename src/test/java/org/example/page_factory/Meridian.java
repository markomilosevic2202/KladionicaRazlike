package org.example.page_factory;

import org.example.test_data_bils.Match;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class Meridian {
    @FindBy(xpath = "//div[contains(text(),'Fudbal')]")
    List<WebElement> listFootball;


    WebDriver driver;

    public Meridian(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickFootball() {
        listFootball.get(0).click();

    }
    public void clickTime(String time) {
        Assertions.assertTrue(time.equals("1h") || time.equals("3h") || time.equals("Danas") || time.equals("3 dana") || time.equals("Sve"),
                "Values can be: '1h', '3h', 'Danas', 'Sve'");
        driver.findElement(By.xpath("//div[contains(text(),'" + time + "')]")).click();

    }

    public void waitForPageToLoad() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement scrollBar = (WebElement) js.executeScript("return document.querySelector('body > div > div:nth-child(3) > div > div > div.slimScrollBar');");
        boolean isScrollBarAtEnd = false;
        Thread.sleep(5000);

//        while (!isScrollBarAtEnd) {
        for (int i = 0; i < 5; i++) {


        js.executeScript("var divElement = document.getElementById(\"page-wrapper\");\n" +
                "divElement.scrollTop = divElement.scrollHeight;\n" +
                "\n" +
                "// Funkcija koja će se izvršavati prilikom dodavanja novih podataka\n" +
                "function dodajNoviPodatak(podatak) {\n" +
                "  // Dodajte novi podatak u div element\n" +
                "  var noviElement = document.createElement(\"p\");\n" +
                "  noviElement.textContent = podatak;\n" +
                "  divElement.appendChild(noviElement);\n" +
                "  \n" +
                "  // Skrolovanje na dno nakon dodavanja novog podatka\n" +
                "  divElement.scrollTop = divElement.scrollHeight;\n" +
                "}\n" +
                "\n" +
                "// Primer dodavanja novih podataka svakih 2 sekunde\n" +
                "setInterval(function() {\n" +
                "  var noviPodatak = \"Novi podatak \" + Math.random();\n" +
                "  dodajNoviPodatak(noviPodatak);\n" +
                "}, 2000);");

        Thread.sleep(2000);


        }
       }


    public List<Match> writeMatch() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        List<Match> matches = (List<Match>) js.executeScript(
                "let list = document.querySelectorAll('.event.ng-star-inserted');\n" +
                        "let result = [];\n" +
                        "list.forEach(e=>{\n" +
                        "\n" +
                        "            result.push({\n" +
                        "              code: e.querySelector('.match-id').innerText,\n" +
                        "              name: e.querySelector('.home').innerText + \" - \" + e.querySelector('.away').innerText,\n" +
                        "              one: e.querySelectorAll('.odds.ng-star-inserted')[0].outerText,\n" +
                        "              x: e.querySelectorAll('.odds.ng-star-inserted')[1].outerText,\n" +
                        "              two: e.querySelectorAll('.odds.ng-star-inserted')[2].outerText,\n" +
                        "              time: e.querySelector('.time').innerText,\n" +
                        "              date: e.querySelector('.date').innerText\n" +
                        "                       \n" +
                        "                           }); \n" +
                        "                        });\n" +
                        "return result;");
        System.out.println(matches.size());
        return matches;


}

    }
