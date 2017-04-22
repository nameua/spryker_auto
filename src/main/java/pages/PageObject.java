package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.WebElementFacade;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PageObject {

    private static final long waitForTimeoutInSeconds = 7;

    protected WebDriver driver;

    public PageObject(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebElementFacade element(WebElement webElement) {
        return element(webElement, TimeUnit.SECONDS.toMillis(waitForTimeoutInSeconds));
    }

    public WebElementFacade element(WebElement webElement, Long waitForTimeoutInMilliseconds) {
        return WebElementFacade.wrapWebElement(driver, webElement, waitForTimeoutInMilliseconds);
    }

    public List<WebElementFacade> elements(List<WebElement> webElements) {
        return webElements.stream().map(this::element).collect(Collectors.toList());
    }

}
