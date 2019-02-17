package com.alation.baseframework;

import com.alation.constants.WebDriverConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestListenerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/*
A Selenium wrapper class for common interactions that has to be performed on a webpage like type, click etc
*/

public class AppPage extends TestListenerAdapter {

    protected static Logger logger = LoggerFactory.getLogger(AppPage.class.getName());

    protected WebDriver driver;
    JavascriptExecutor javaScriptExecutor;



    public JavascriptExecutor getJavaScriptExecutor()
    {
        if( javaScriptExecutor == null)
            javaScriptExecutor = (JavascriptExecutor) driver;
        return javaScriptExecutor;
    }

    public void waitForPageLoad(int timeout)
    {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeout, TimeUnit.SECONDS).pollingEvery(3, TimeUnit.SECONDS).ignoring(
                NoSuchElementException.class, WebDriverException.class);
        wait.until(new ExpectedCondition<Boolean>()
        {
            public Boolean apply(WebDriver driver)
            {
                String result = (String) getJavaScriptExecutor().executeScript("return document.readyState");
                if(result == null)
                    return false;
                else
                    return result.equals(
                            "complete");
            }
        });
        return;
    }

    public void waitForAJaxCompletion()
    {
        try
        {
            ExpectedCondition<Boolean> isLoadingFalse = new ExpectedCondition<Boolean>()
            {
                public Boolean apply(WebDriver driver)
                {
                    String ajaxCount = (String) ((JavascriptExecutor) driver)
                            .executeScript("return '' + XMLHttpRequest.prototype.ajaxCount");
                    if (ajaxCount != null && ajaxCount.equals("undefined"))
                    {
                        monkeyPatch();
                        return true;
                    }
                    if (ajaxCount != null && Double.parseDouble(ajaxCount) > 0.0d)
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
            };
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(WebDriverConstants.WAIT_ONE_MIN, TimeUnit.SECONDS).pollingEvery(500, TimeUnit.MILLISECONDS)
                    .ignoring(NoSuchElementException.class);
            wait.until(isLoadingFalse);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    /**
     * This method modifies the inbuilt javascripts class XMLHttpRequest to include one more variable ajaxcount.
     * This will be used to keep track of all the ajaxcounts sent out from the browser. The code is minified as
     * the normal version is not getting executed properly in javascript executor.
     */
    public void monkeyPatch()
    {
        String ajaxCount = (String) ((JavascriptExecutor) driver)
                .executeScript("return '' + XMLHttpRequest.prototype.ajaxCount");
        if (ajaxCount != null && ajaxCount.equals("undefined"))
        {
            getJavaScriptExecutor().executeScript(
                    "!function(t){function n(){t.ajaxCount++,console.log(\"Ajax count when triggering ajax send: \"+t.ajaxCount)}function a(){t.ajaxCount>0&&t.ajaxCount--,console.log(\"Ajax count when resolving ajax send: \"+t.ajaxCount)}t.ajaxCount=0;var e=t.send;t.send=function(t){return this.addEventListener(\"readystatechange\",function(){null!=this&&this.readyState==XMLHttpRequest.DONE&&a()},!1),n(),e.apply(this,arguments)};var o=t.abort;return t.abort=function(t){return a(),o.apply(this,arguments)},t}(XMLHttpRequest.prototype);");
        }
    }

    public void waitForPageLoadComplete()
    {
        waitForPageLoad(WebDriverConstants.MAX_TIMEOUT_PAGE_LOAD);
        waitForAJaxCompletion();
        return;
    }

    public AppPage(WebDriver driver)
    {
        this.driver = driver;
        waitForPageLoadComplete();
        PageFactory.initElements(driver, this);
        driver.manage().window().maximize();
    }

    public WebDriver getDriver()
    {
        return this.driver;
    }

    public void get(String url)
    {
        this.driver.get(url);
    }

    public String getCurrentUrl()
    {
        return this.driver.getCurrentUrl();
    }

    public Set<Cookie> getCookies()
    {
        return this.driver.manage().getCookies();
    }

    public HashMap<String,String> getCookiesHash()
    {
        Set<Cookie> cookies = getCookies();
        HashMap<String, String> cookieHash = new HashMap<String, String>();
        for (Cookie c : cookies)
        {
            cookieHash.put(c.getName(), c.getValue());
        }
        return cookieHash;
    }

    public void deleteCookies()
    {
        this.driver.manage().deleteAllCookies();
    }

    public String pageSource()
    {
        return this.driver.getPageSource();
    }

    public boolean isElementPresent(By locator)
    {
        return this.driver.findElements(locator).size() == 0? false : true;
    }
    public boolean isElementPresent(WebElement element)
    {
        try
        {
            element.getAttribute("innerHTML");
        }
        catch(Exception ex)
        {
            return false;
        }
        return true;
    }

    public boolean isElementPresentAndDisplayed(WebElement element)
    {
        try
        {
            return isElementPresent(element) && element.isDisplayed();
        }
        catch(Exception ex)
        {
            return false;
        }
    }

    public boolean isElementPresentAndDisplayed(By xpath)
    {
        try
        {
            return isElementPresentAndDisplayed(this.driver.findElement(xpath));
        }
        catch(Exception ex)
        {
            return false;
        }

    }

    public Boolean isElementPresentInContainer(WebElement container, final By locator)
    {
        Boolean isElementPresent = false;
        if (container != null && container.findElements(locator).size() > 0)
            isElementPresent = true;
        return isElementPresent;
    }

    public void waitForVisible(WebElement element)
    {
        WebDriverWait wait =
                new WebDriverWait(driver, WebDriverConstants.WAIT_FOR_VISIBILITY_TIMEOUT_IN_SEC);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForVisible(WebElement element, Integer timeout)
    {
        WebDriverWait wait =
                new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForVisible(By locator)
    {
        WebDriverWait wait =
                new WebDriverWait(driver,WebDriverConstants.WAIT_FOR_VISIBILITY_TIMEOUT_IN_SEC);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForElementToBeEnabled(WebElement e)
    {
        final WebElement web = e;
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(WebDriverConstants.WAIT_ONE_MIN, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
        wait.until(new ExpectedCondition<Boolean>()
        {
            public Boolean apply(WebDriver driver)
            {
                return web.isEnabled();
            }
        });
        return;
    }

    public void waitForElementToBeEnabled(By locator)
    {
        final By loc = locator;
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(WebDriverConstants.WAIT_ONE_MIN, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
        wait.until(new ExpectedCondition<Boolean>()
        {
            public Boolean apply(WebDriver driver)
            {
                return driver.findElement(locator).isEnabled();
            }
        });
        return;
    }


    public void waitForElementToContainText(WebElement e, String text)
    {
        waitForElementToBeEnabled(e);
        if(isElementPresentAndDisplayed(e))
        {
            final String innerText = text;
            final WebElement element = e;
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(WebDriverConstants.WAIT_ONE_MIN, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
            wait.until(new ExpectedCondition<Boolean>()
            {
                public Boolean apply(WebDriver driver)
                {
                    return element.getText().contains(innerText);
                }
            });
        }
        return;
    }

    public void waitForElementToContainText(By locator, String text)
    {
        waitForElementToBeEnabled(locator);
        if(isElementPresentAndDisplayed(locator))
        {
            final String innerText = text;
            final By loc = locator;
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(WebDriverConstants.WAIT_ONE_MIN, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
            wait.until(new ExpectedCondition<Boolean>()
            {
                public Boolean apply(WebDriver driver)
                {
                    return driver.findElement(loc).getText().contains(innerText);
                }
            });
        }
        return;
    }

    public void clearAndType(WebElement element, String text)
    {
        element.clear();
        element.sendKeys(text);
    }


    public void selectDropdown(WebElement element, String by, String value)
    {
        Select select = new Select(element);
        switch (by.toUpperCase())
        {
        case "INDEX":
            select.selectByIndex(Integer.parseInt(value));
            break;
        case "VALUE":
            select.selectByValue(value);
            break;
        case "TEXT":
            select.selectByVisibleText(value);
            break;
        }
    }

    public void selectDropDownContainingText(WebElement element, String value)
    {
        Select select = new Select(element);
        List<String> allOptions = getAllSelectOptions(element);
        for(String s: allOptions)
        {
            if(s.contains(value))
            {
                select.selectByVisibleText(s);
                break;
            }
        }
    }

    public List<String> getAllSelectOptions(WebElement drpdown)
    {
        Select s = new Select(drpdown);
        List<WebElement> list = s.getOptions();
        List<String> listNames = new ArrayList<String>(list.size());
        for (WebElement w : list)
            listNames.add(w.getText());

        return listNames;
    }

    public void refresh()
    {
        this.driver.navigate().refresh();
        monkeyPatch();
    }


}
