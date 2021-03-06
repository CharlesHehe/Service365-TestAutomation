package com.service365.customerPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServicePage {
    WebDriver webDriver;
    @FindBy(xpath = "//*[@id=\"Filters\"]/fieldset[1]/div/input")
    WebElement keyWordInput;
    @FindBy(xpath = "//*[@id=\"Filters\"]/fieldset[2]/div/div[1]/div/div[1]/input")
    WebElement locationInput;
    @FindBy(xpath = "//*[@id=\"Filters\"]/fieldset[2]/div/div[1]/button/div/div/div")
    WebElement locationDiv;
    @FindBy(xpath = "//*[@id=\"Filters\"]/fieldset[2]/div/div[2]/div/div[1]/input")
    WebElement districtsInput;
    @FindBy(xpath = "//*[@id=\"Filters\"]/fieldset[2]/div/div[2]/button/div")
    WebElement districtsDiv;
    @FindBy(xpath = "//*[@id=\"Filters\"]/fieldset[3]/div/div/div/div[1]/input")
    WebElement categoryInput;
    @FindBy(xpath = "//*[@id=\"Filters\"]/fieldset[3]/div/div/button/div/div/div")
    WebElement categoryDiv;
    @FindBy(xpath = "//*[@id=\"Filters\"]/button")
    WebElement searchButton;

    public ServicePage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void setKeyWord(String s) {
        keyWordInput.sendKeys(s);
    }

    public void setLocation(String s) {
        locationDiv.click();
        locationInput.sendKeys(s);
    }

    public void setDistricts(String s) {
        districtsDiv.click();
        districtsInput.sendKeys(s);
    }

    public void setCategory(String s) {
        categoryDiv.click();
        categoryInput.sendKeys(s);
    }

    public void clickSearch() {
        searchButton.click();
    }

//    逻辑方法
    public List getBookNowButtons() {
        List list;
        List list1 = new ArrayList();
        list = webDriver.findElements(By.className("btn-raised"));

        list.remove(list.size()-1);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            WebElement webElement = (WebElement) iterator.next();

            if (webElement.getText().equals("BOOK NOW")) {
                list1.add(webElement.getAttribute("href"));
            }

        }
        return list1;
    }


    public void editFilterList(String keyWord){
        setKeyWord(keyWord);
        setLocation("AUCKLAND");
        setCategory("ALL CATEGORIES");
        setDistricts("ALL DISTRICTS");
        clickSearch();
    }
}
