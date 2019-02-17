package com.alation.amazon.pages;

import com.alation.baseframework.AppPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;


/*
Products page POM class, contains all the elements that are in results page and actions as methods that can be performed on
that page
*/
public class ProductPage extends AppPage {

    @FindBy(xpath = "//h1/span[@id=\"productTitle\"]")
    private WebElement titleTxt;

    @FindBy(xpath = "//div[@id=\"bylineInfo\"]/span[contains(@class,\"author\")]")
    private List<WebElement> authorNames;

    @FindBy(id = "acrCustomerReviewLink")
    private WebElement reviewsTxt;

    @FindBy(xpath = "//div[@id=\"usedOfferAccordionRow\"]//span[contains(@class,\"header-price\")]")
    private WebElement usedPriceTxt;

    @FindBy(xpath = "//div[@id=\"newOfferAccordionRow\"]//span[contains(@class,\"header-price\")]")
    private WebElement newPriceTxt;

    @FindBy(xpath = "//ul[@id=\"mediaTabs_tabSet\"]//span[contains(@class,\"mediaTab_title\")  and contains(.,\"Kindle\")]")
    private WebElement kindleTab;

    @FindBy(xpath = "//ul[@id=\"mediaTabs_tabSet\"]//span[contains(@class,\"mediaTab_title\")  and contains(.,\"Other Sellers\")]")
    private WebElement otherSellersTab;

    @FindBy(xpath = "//div[@id=\"tmmSwatches\"]//span[.=\"Kindle\"]/..//span[contains(@class,\"price\")]")
    private WebElement otherSellersKindlePrice;

    @FindBy(xpath = "//div[@id=\"tmmSwatches\"]//span[.=\"Paperback\"]/..//span[@class=\"a-size-base a-color-secondary\"]")
    private WebElement getOtherSellersPaperbackPrice;

    @FindBy(xpath = "//div[@id=\"bookDescription_feature_div\"]//span[.=\"Read more\"]")
    private WebElement readMoreBtn;

    @FindBy(xpath = "//div[@id=\"bookDescription_feature_div\"]//span[.=\"Read less\"]")
    private WebElement readLessBtn;

    public void clickReadMore(){

        readMoreBtn.click();
        waitForAJaxCompletion();
    }

    public void clickReadLess(){

        readLessBtn.click();
        waitForAJaxCompletion();
    }
    public String getOtherSellersKindlePrice(){

        return otherSellersKindlePrice.getText().trim();

    }

    public String getOtherSellersPaperbackPrice(){

        return getOtherSellersPaperbackPrice.getText().trim();
    }

    public void clickOtherSellersTab(){

        otherSellersTab.click();
        waitForPageLoadComplete();
        waitForAJaxCompletion();

    }

    public void clickKindleTab(){

        kindleTab.click();
        waitForPageLoadComplete();
        waitForAJaxCompletion();
    }

    public String getUsedPrice(){

        return usedPriceTxt.getText().trim();
    }

    public String getNewPrice(){

        return newPriceTxt.getText().trim();
    }

    public ProductPage(WebDriver driver){
        super(driver);
    }

    public String getTitleOfTheProduct(){
        return titleTxt.getText().trim();
    }

    public String getAuthorName(){

        final String name ;

       return authorNames.stream().map(e -> e.getText().toString()).reduce(",", String::concat);


    }

    public String getNumberOfReviews(){

        return reviewsTxt.getText().split(" ")[0].trim();
    }





}
