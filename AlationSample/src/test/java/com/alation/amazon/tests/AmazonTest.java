package com.alation.amazon.tests;

import com.alation.amazon.pages.HomePage;
import com.alation.amazon.pages.ProductPage;
import com.alation.amazon.pages.ResultsPage;
import com.alation.baseframework.AppTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;


public class AmazonTest extends AppTest {

    protected static Logger logger = LoggerFactory.getLogger(AmazonTest.class.getName());
    HomePage home;
    ResultsPage resultsPage;
    ProductPage productPage;


    /*
    Main test where the POM model based tests gets executed
     */
    @Test
    public void testAmazonScenarion(){

        home = new HomePage(getDriver());

        home.setDropDownValue( System.getProperty("category", "Books"));

        home.enterSearchText(System.getProperty("product","Product Catalog"));

        resultsPage = home.clickSearchBtn();

        logger.info("total number of results: " + resultsPage.getTotalNumberOfResultsInFirstPage());

        logger.info("Title of the first result: " + resultsPage.getNthResultTitle("0"));

        productPage = resultsPage.clickNthResult("1");

        logger.info("Number of reviews for the product: " + productPage.getNumberOfReviews());

        logger.info("Authore Name: " + productPage.getAuthorName());

        logger.info("New Item Price: " + productPage.getNewPrice());

        logger.info("Used Item Price: " + productPage.getUsedPrice());

        productPage.clickReadMore();

        productPage.clickReadLess();

        productPage.clickOtherSellersTab();

        logger.info("Other seller Kindle price: " +  productPage.getOtherSellersKindlePrice());

        logger.info("Other seller paperback price: " + productPage.getOtherSellersPaperbackPrice());


    }

}
