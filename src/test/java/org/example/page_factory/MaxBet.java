package org.example.page_factory;

import org.example.object.Match;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
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
    List<WebElement> listMatch;

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
        if(hours.equals("3")){
            for (int i = 0; i < 5; i++) {
                slider.sendKeys(Keys.ARROW_LEFT);
            }
        } else if (hours.equals("5")) {
            for (int i = 0; i < 4; i++) {
                slider.sendKeys(Keys.ARROW_LEFT);
            }
        }else if (hours.equals("12")) {
            for (int i = 0; i < 3; i++) {
                slider.sendKeys(Keys.ARROW_LEFT);
            }
        }
        else if (hours.equals("24")) {
            for (int i = 0; i < 2; i++) {
                slider.sendKeys(Keys.ARROW_LEFT);
            }
        }
        else if (hours.equals("24")) {
            slider.sendKeys(Keys.ARROW_LEFT);
        }
        else if (hours.equals("all")) {

        }
        else {
            Assertions.fail("  :: The specified parameter was not found. The options offered are 3,5,12,24,48,all :: ");
        }

        slider.sendKeys(Keys.ARROW_LEFT);
        slider.sendKeys(Keys.ARROW_LEFT);

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
    public void writeMatch()  {
        for (int i = 0; i < listMatch.size(); i++) {
            WebElement element = listMatch.get(i);
            Match match = new Match();
            System.out.println();
            System.out.println(element.findElement(By.xpath("match/span/div/div[1]")).getText());

        }

    }
    }
