package org.example.page_factory;

import org.example.test_data_bils.Match;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
        for (int i = 0; i < 3; i++) {

            Thread.sleep(2000);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
          wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("biab_table-wrapper")));
          List<WebElement> listElement =  driver.findElements(By.className("biab_table-wrapper"));
            for (int j = 0; j < 20; j++) {
                WebElement element = listElement.get(j);
                Match match = new Match();

                match.setDate(element.getText());
                System.out.println(match.getDate());
            }



            btnNext.click();
        }

        }

    }

