package com.alation.baseframework;

import com.alation.customexceptions.MyCoreExceptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestListenerAdapter;

/*
Wrapper for WebDriver class, using the methods in this class we can interact with driver objects
*/


public class AppDriver extends TestListenerAdapter {

    WebDriver driver = null;

    protected static Logger logger = LoggerFactory.getLogger(AppDriver.class.getName());

    public WebDriver setBrowser(){
        String browser = System.getProperty("browserName", "chrome").toLowerCase();

        switch (browser){
        case "chrome":
            logger.info("Setting up chrome driver...");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            break;

        case "firefox":
            logger.info("Setting up firefox driver...");
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            break;

        case "ie":
            logger.info("Setting up ie driver...");
            WebDriverManager.iedriver().setup();
            driver = new InternetExplorerDriver();
            break;

        case "edge":
            logger.info("Setting up edge driver...");
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
            break;
        }

        logger.info("completed setting up the driver...");

        return driver;

    }

    public WebDriver getDriver(){

        logger.info("getting driver object...");
        if (driver != null)
            return driver;

        else
           return setBrowser();
    }

    public void stopDriver(){

        logger.info("stopping driver...");

        if(driver != null)
        {
            driver.close();
            driver.quit();
        }

        logger.info("stopped driver...");
    }

    public String getBrowserHandle() throws MyCoreExceptions {

        try {

            if (driver == null)
                throw new MyCoreExceptions("Unable to get the winhandle as the driver is set as null");

            return driver.getWindowHandle().trim();
        }
        catch (Exception e){
            e.printStackTrace();
            throw new MyCoreExceptions("Exception occured... "+ e.getStackTrace());

        }
    }



}
