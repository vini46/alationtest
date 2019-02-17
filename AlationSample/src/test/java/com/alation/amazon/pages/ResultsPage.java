package com.alation.amazon.pages;

import com.alation.baseframework.AppPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/*
Results page POM class, contains all the elements that are in results page and actions as methods that can be performed on
that page
*/

public class ResultsPage extends AppPage {

    @FindBy(xpath = "//span[@data-component-type=\"s-search-results\"]/div[@class=\"s-result-list sg-row\"]")
    private WebElement resultsTable;

    @FindBy(xpath = "//li[@class=\"a-last\"]/a")
    private WebElement nextBtn;

    @FindBy(xpath = "//*[@data-index]")
    private List<WebElement> resultsElements;


    public ResultsPage(WebDriver driver){
        super(driver);
    }

    public ProductPage clickNthResult(String n){

        String resultsXpath = "//div[@data-index= + \"" + n + "\"]";

        resultsTable.findElement(By.xpath(resultsXpath)).click();

        waitForPageLoadComplete();
        waitForAJaxCompletion();

        return new ProductPage(getDriver());
    }

    public String getNthResultTitle(String n){

        String resultsXpath = "//div[@data-index=  \"" + n + "\"]//h5";

        return driver.findElement(By.xpath(resultsXpath)).getText().trim();


    }

    public void clickNextBtn(){

        nextBtn.click();

        waitForPageLoadComplete();
        waitForAJaxCompletion();
    }

    public int getTotalNumberOfResultsInFirstPage(){

        return  resultsElements.size();


    }



}
