package org.example.page_factory;

import org.example.test_data_bils.Match;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class Mozzart {

    @FindBy(xpath = "//span[contains(text(),'Fudbal')]")
    WebElement btnFootball;

    @FindBy(xpath = "//*[contains(text(),'Sačuvaj i zatvori')]")
    WebElement btnSave;


    WebDriver driver;

    public Mozzart(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickFootball() {
        btnFootball.click();

    }
    public void clickSave() {
        btnSave.click();

    }

    public void clickTime(String time) {
        Assertions.assertTrue(time.equals("1 sat") || time.equals("Danas") || time.equals("3 dana") || time.equals("Svi dani"),
                "Values can be: '1 sat', 'Danas', '3 dana', 'Svi dani'");
        driver.findElement(By.xpath("//li[contains(text(),'" + time + "')]")).click();

    }

    public void waitForPageToLoad() throws InterruptedException {

        WebElement element = driver.findElement(By.className("vb-dragger-styler"));

        // Kreiraj JavaScriptExecutor
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Skrolovanje do kraja
        js.executeScript("arguments[0].scrollTo(0, arguments[0].scrollHeight);", element);


//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        WebElement scrollBar = (WebElement) js.executeScript("return document.querySelector('body > div > div:nth-child(3) > div > div > div.slimScrollBar');");
//        boolean isScrollBarAtEnd = false;
//        Thread.sleep(10000);
//
////        while (!isScrollBarAtEnd) {
//        for (int i = 0; i < 5; i++) {


//            js.executeScript("var divElement = document.getElementById(\"page-wrapper\");\n" +
//                    "divElement.scrollTop = divElement.scrollHeight;\n" +
//                    "\n" +
//                    "// Funkcija koja će se izvršavati prilikom dodavanja novih podataka\n" +
//                    "function dodajNoviPodatak(podatak) {\n" +
//                    "  // Dodajte novi podatak u div element\n" +
//                    "  var noviElement = document.createElement(\"p\");\n" +
//                    "  noviElement.textContent = podatak;\n" +
//                    "  divElement.appendChild(noviElement);\n" +
//                    "  \n" +
//                    "  // Skrolovanje na dno nakon dodavanja novog podatka\n" +
//                    "  divElement.scrollTop = divElement.scrollHeight;\n" +
//                    "}\n" +
//                    "\n" +
//                    "// Primer dodavanja novih podataka svakih 2 sekunde\n" +
//                    "setInterval(function() {\n" +
//                    "  var noviPodatak = \"Novi podatak \" + Math.random();\n" +
//                    "  dodajNoviPodatak(noviPodatak);\n" +
//                    "}, 2000);");

            Thread.sleep(20000);


        }



    public List<Match> writeMatch() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        List<Match> matches = (List<Match>) js.executeScript(
                "let list = document.querySelectorAll('.match.botFlex');\n" +
                        "                        let result = [];\n" +
                        "                        list.forEach(e=>{\n" +
                        "                               console.log(e.querySelector('.bonus-group'));\n" +
                        "                                                \n" +
                        "                             if(e.querySelector('.sp-mark') === null){\n" +
                        "                                   result.push({\n" +
                        "                                      code: e.querySelector('.moreoddstext').innerText,\n" +
                        "                                      name: e.querySelector('.pairs').outerText.split('\\n')[0] + \" - \" + e.querySelector('.pairs').outerText.split('\\n')[1],\n" +
                        "                                      one: e.querySelectorAll('.partvar.odds')[0].outerText.substring(e.querySelectorAll('.partvar.odds')[0].outerText.indexOf(\"\\n\") + 1),\n" +
                        "                                      x: e.querySelectorAll('.partvar.odds')[1].outerText.substring(e.querySelectorAll('.partvar.odds')[1].outerText.indexOf(\"\\n\") + 1),\n" +
                        "                                      two: e.querySelectorAll('.partvar.odds')[2].outerText.substring(e.querySelectorAll('.partvar.odds')[2].outerText.indexOf(\"\\n\") + 1),\n" +
                        "                                      time: e.querySelector('.time').outerText.slice(e.querySelector('.time').outerText.indexOf(\".\") + 2),\n" +
                        "                                       date: '25.05.' \n" +
                        "                                             \n" +
                        "                                     }); }\n" +
                        "                                              });\n" +
                        "                        return result;");

        System.out.println(matches.size());
        return matches;


    }
}
