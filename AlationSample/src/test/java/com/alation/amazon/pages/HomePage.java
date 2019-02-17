package com.alation.amazon.pages;

import com.alation.baseframework.AppPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


/*
Home page POM class, contains all the elements that are in results page and actions as methods that can be performed on
that page
*/

public class HomePage extends AppPage {


    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchTextBox;

    @FindBy(id = "searchDropdownBox")
    private WebElement searchDropdown;

    @FindBy(xpath = "//input[@value=\"Go\"]")
    private WebElement searchBtn;

    public HomePage(WebDriver driver){
       super(driver);
    }


    public void setDropDownValue(String value){

        selectDropdown(searchDropdown, "text", value);
    }

    public void enterSearchText(String value){
        clearAndType(searchTextBox, value);
    }

    public ResultsPage clickSearchBtn(){
        searchBtn.click();
        waitForPageLoadComplete();
        waitForAJaxCompletion();

        return new ResultsPage(driver);
    }



}
