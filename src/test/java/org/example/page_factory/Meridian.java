package org.example.page_factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Meridian {
    @FindBy(xpath = "//a[contains(text(),'Fudbal')]")
    WebElement btnFootball;


    WebDriver driver;

    public Meridian(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickFootball() {
        btnFootball.click();

    }


}
