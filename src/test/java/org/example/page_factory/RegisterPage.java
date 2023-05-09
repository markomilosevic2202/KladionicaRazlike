package org.example.page_factory;

import com.github.kklisura.cdt.protocol.commands.Network;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.exceptions.WebSocketServiceException;
import com.github.kklisura.cdt.services.impl.ChromeDevToolsServiceImpl;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.example.object.Trade;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterPage {
    WebDriver driver;
    @FindBy(id = "email")
   // @CacheLookup
    WebElement inpEmail;

    @FindBy(id = "password")
    WebElement inpPassword;

    @FindBy(id = "repeatPassword")
    WebElement inpRepeatPass;

    @FindBy(xpath = "/html/body/div[1]/div/div/div/div[2]/div/form/button")

    WebElement btnGetStarted;


    @FindBy(xpath = "/html/body/div[1]/div/div/div/div[2]/div/div/a")

    WebElement btnSetUp;

    @FindBy(xpath = "/html/body/div[1]/div/div/div/div[2]/div/form/label/input")

    WebElement chbIUnderstand;

    @FindBy(xpath = "/html/body/div[1]/div/div/div/div[2]/div/form/button")

    WebElement btnShowPassphrase;

    @FindBy(className = "space-x-4")

    WebElement btnGotIt;

    @FindBy(xpath = "/html/body/div[1]/div/div/div/div[2]/div[1]/div/button")

    WebElement btnCheck;

    @FindBy(xpath = "/html/body/div[1]/div/div/div/div[2]/div/div/a")

    WebElement btnLetsGo;

    @FindBy(id = "submit-core")
    // @CacheLookup
    WebElement btnSubmit;

    @FindBy(className = "lg:mt-32")

    WebElement form;

    @FindBy(id = "resend-button")
    WebElement btnResend;

    @FindBy(id = "verificationCode")
    WebElement code;

    @FindBy(id = "one")
    WebElement inpOwner;

    @FindBy(xpath = "/html/body/div[1]/div/div/div/div[2]/div/form/div/div[1]/input")
    WebElement inpVerify;

    @FindBy(xpath = "//*[contains(@class, 'bg-gold-lightest/50')]")
    static List<WebElement> listWord;

    @FindBy(xpath = "//*[contains(@class, 'border-gray-300')]")
    List<WebElement> listWord2;

    @FindBy(xpath = "//*[contains(@class, 'border-gray-300')]")
    List<WebElement> listInput;


    @FindBy(xpath = "//p[contains(text(),'Please, enter the business owner name')]")
  List <WebElement> msgPleaseEnterOwnerName;

    @FindBy(xpath = "//p[contains(text(),'Please, enter correct phone number (only + sign, numbers and dashes allowed)')]")
    List <WebElement> msgPhone;

    @FindBy(xpath = "//p[contains(text(),'Enter valid email!')]")
    List <WebElement> msgEmail;

    @FindBy(xpath = "//p[contains(text(),'EIN number must only contain digits in xx-xxxxxxx format')]")
    List <WebElement> msgEin;

    static private List<String> list = new ArrayList<String>();


    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void enterUsername(String username) {
        inpEmail.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        inpEmail.sendKeys(username);
    }

    public void enterPass(String pass) {
        inpPassword.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        inpPassword.sendKeys(pass);
    }
    public void enterRepeatPass(String repeatPass) {
        inpRepeatPass.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        inpRepeatPass.sendKeys(repeatPass);
    }
    public void enterOwnerName(String ownerName) {
        inpOwner.sendKeys(ownerName);
    }

    public void clickGetStarted() {
        btnGetStarted.click();

    }


    public void clickSetUp() {
        btnSetUp.click();
    }

    public void checkBoxIUnderstand() {
        chbIUnderstand.click();
    }

    public void clickShowPassphrase() {
        btnShowPassphrase.click();
    }

    public void clickGotIt() throws InterruptedException {
        Thread.sleep(2000);
        btnGotIt.click();
    }

    public void clickCheck() {
        btnCheck.click();
    }

    public void clickLetsGo() {
        btnLetsGo.click();
    }

    public void clickSubmit() {
        btnSubmit.click();
    }

    public void clickResend() throws InterruptedException {

        btnResend.click();


    }

    public void takeCode() throws InterruptedException {
        Thread.sleep(2000);

        String text = (String) ((JavascriptExecutor) driver).executeScript("return document.querySelector('#verificationCode')?.textContent;", code);

        inpVerify.sendKeys(text);


    }

    public WebElement getInputBusinessName() {
        return driver.findElement(By.id("businessName"));
    }

    public WebElement getButtonNext() {
        return driver.findElement(By.className("next-btn"));
    }

    public WebElement getRadioIndustrial() {
        return driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div/form/div/div/div[2]/label[2]/span[1]"));
    }

    public WebElement getInputStreet() {
        return driver.findElement(By.id("address"));
    }

    public WebElement getInputCity() {
        return driver.findElement(By.id("city"));
    }

    public WebElement getInputState() {
        return driver.findElement(By.id("state"));
    }

    public WebElement getInputZip() {
        return driver.findElement(By.id("zip"));
    }

    public WebElement getInputCountry() {
        return driver.findElement(By.id("country"));
    }

    public WebElement getInputTel() {

        return driver.findElement(By.id("tel"));
    }

    public WebElement getInputWebsite() {
        return driver.findElement(By.id("website"));
    }

    public WebElement getInputEmail() {
        return driver.findElement(By.id("email"));
    }

    public WebElement getInputEin() {
        return driver.findElement(By.id("ein"));
    }

    public void saveWord() throws InterruptedException {

        for (int i = 0; i < listWord.size(); i++) {

            list.add(listWord.get(i).findElement(By.xpath("span[2]")).getText());

        }


    }


    public void inputWord() throws InterruptedException {

        for (int j = 0; j < list.size(); j++) {
            String wordInspect = list.get(j);
            for (int i = 0; i < list.size(); i++) {
                WebElement a = driver.findElement(By.id("cell-" + i));
                WebElement input = a.findElement(By.xpath("span"));
                String a1 = a.getText();
                if (a1.contains(wordInspect)) {
                    input.click();
                    break;
                }
            }

        }



    }
    public void verifyMessageExistOwner() {
        Assertions.assertTrue(msgPleaseEnterOwnerName.size()>0, " :: Message is not exist :: ");
    }
    public void verifyMessageExistPhone() {
        Assertions.assertTrue(msgPhone.size()>0, " :: Message is not exist :: ");
    }
    public void verifyMessageExistEmail() {
        Assertions.assertTrue(msgEmail.size()>0, " :: Message is not exist :: ");
    }
    public void verifyMessageExistEin() {
        Assertions.assertTrue(msgEin.size()>0, " :: Message is not exist :: ");
    }
    public void verifyMessageExist(String message) {
        Assertions.assertTrue(driver.findElements(By.xpath("//*[contains(text(),'" + message + "')]")).size()>0, " :: Message is not exist '" + message + "' ::");
    }



}