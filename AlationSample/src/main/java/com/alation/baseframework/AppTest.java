package com.alation.baseframework;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

/*
Main driver for the tests, this acts a baseclass for all the test classes
*/

@Listeners(AppDriver.class)
public class AppTest extends AppDriver{

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(){
        String url = System.getProperty("url","https://www.amazon.com");
        driver = getDriver();
        driver.get(url);

    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(){

        stopDriver();

    }



}
