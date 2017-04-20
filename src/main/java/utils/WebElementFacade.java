package utils;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.*;
import pages.PageObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by slepkan on 4/19/17
 */
public class WebElementFacade implements WrapsElement, WebElement, Locatable{
//    private static final Logger LOGGER = Logger.getLogger(WebElementFacade.class);

    private static final int TIME_OUT_IN_SECONDS = 20;
    private static final int WAIT_FOR_ELEMENT_PAUSE_LENGTH = 250;
    private static final List<String> HTML_ELEMENTS_WITH_VALUE_ATTRIBUTE =
            ImmutableList.of("input", "button", "option");
    protected final WebDriver driver;
    private final long timeoutInMilliseconds;
    private final Sleeper sleeper;
    private final Clock webdriverClock;
    WebElement webElement;
    private ElementLocator locator;
    private String page;

    private WebElementFacade(final WebDriver driver,
                             final ElementLocator locator,
                             final WebElement webElement,
                             final long timeoutInMilliseconds) {
        this.webElement = webElement;
        this.driver = driver;
        this.timeoutInMilliseconds = timeoutInMilliseconds;
        this.locator = locator;
        this.webdriverClock = new SystemClock();
        this.sleeper = Sleeper.SYSTEM_SLEEPER;
        setOwnerPage();
    }

    private void setOwnerPage() {
        page = Arrays.asList(new Throwable().getStackTrace()).stream()
                .filter(traceElement -> !(traceElement.getClassName().equals(this.getClass().getName()) ||
                        traceElement.getClassName().equals(PageObject.class.getName())))
                .map(StackTraceElement::getClassName)
                .findFirst().orElse("UnknownPage");
    }

    public static WebElementFacade wrapWebElement(final WebDriver driver,
                                                  final WebElement element,
                                                  final long timeoutInMilliseconds) {
        return new WebElementFacade(driver, null, element, timeoutInMilliseconds);

    }

    protected WebElement getElement() {
        if (webElement != null) {
            return webElement;
        }
        if (locator == null) {
            return null;
        }
        return locator.findElement();
    }

    public WebElementFacade findBy(By selector) {
        WebElement nestedElement = getElement().findElement(selector);
        return wrapWebElement(driver, nestedElement, timeoutInMilliseconds);
    }

    public WebElementFacade find(By bySelector) {
        return findBy(bySelector);
    }

    public WebElementFacade then(By bySelector) {
        return findBy(bySelector);
    }

    public String getAttribute(String name) {
        return getElement().getAttribute(name);
    }

    public long getTimeoutInMilliseconds() {
        return timeoutInMilliseconds;
    }

    public WebElementFacade withTimeoutOf(int timeout, TimeUnit unit) {
        return wrapWebElement(driver, getElement(),
                TimeUnit.MILLISECONDS.convert(timeout, unit));
    }

    public boolean isVisible() {
        try {
            WebElement element = getElement();
            return element.isDisplayed() && !element.getAttribute("class").contains("display: none");
        } catch (ElementNotVisibleException | NoSuchElementException | StaleElementReferenceException | NullPointerException se) {
            return false;
        }
    }

    public WebElementFacade and() {
        return this;
    }

    public WebElementFacade then() {
        return this;
    }

    public boolean isCurrentlyVisible() {
        try {
            driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
            return isVisible();
        } finally {
            driver.manage().timeouts()
                    .implicitlyWait(Timeout.DEFAULT_IMPLICITLY_WAIT, TimeUnit.SECONDS);
        }
    }


    public boolean isCurrentlyEnabled() {
        try {
            driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
            return getElement().isEnabled();
        } catch (NoSuchElementException | StaleElementReferenceException se) {
            return false;
        } finally {
            driver.manage().timeouts()
                    .implicitlyWait(Timeout.DEFAULT_IMPLICITLY_WAIT, TimeUnit.SECONDS);
        }
    }

    public void shouldBeVisible() {
        if (!isVisible()) {
            String elementName = Arrays.asList(new Throwable().getStackTrace()).stream()
                    .filter(traceElement -> !traceElement.getClassName().equals(this.getClass().getName()))
                    .map(StackTraceElement::getMethodName).findFirst().orElse("Unknown element");
            throw new AssertionError(page + "#" + elementName + " : false");
        }
    }

    public void shouldNotBeVisible() {
        if (isCurrentlyVisible()) {
            throw new AssertionError("Element should not be visible");
        }
    }

    public boolean containsText(final String value) {
        return ((getElement() != null) && (getElement().getText().contains(value)));
    }

    public List<String> getSelectOptions() {
        List<WebElement> results = Collections.emptyList();
        if (getElement() != null) {
            results = getElement().findElements(By.tagName("option"));
        }
        return results.stream().map(element -> element.getAttribute("textContent").trim())
                .collect(Collectors.toList());
    }

    public boolean isEnabled() {
        return (getElement() != null) && (getElement().isEnabled());
    }

    public WebElementFacade type(final String value) {
        waitUntilElementAvailable();
        clear();
        //        Utils.takeScreenshot(driver, webElement.toString());
        getElement().sendKeys(value);
        return this;
    }

    public WebElementFacade typeAndEnter(final String value) {
        waitUntilElementAvailable();
        clear();
        getElement().sendKeys(value, Keys.ENTER);
        return this;
    }

    public WebElementFacade typeAndTab(final String value) {
        waitUntilElementAvailable();
        clear();
        scrollTo(getElement());
        waitUntilElementAvailable();
        getElement().click();
        getElement().sendKeys(value);
        getElement().sendKeys(Keys.TAB);
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {/**/}
        return this;
    }


    public void setWindowFocus() {
        ((JavascriptExecutor) driver).executeScript("window.focus()");
    }


    public WebElementFacade selectByVisibleText(final String label) {
        waitUntilElementAvailable();
        Select select = new KSelect(getElement(), driver);
        select.selectByVisibleText(label);
        return this;
    }

    public WebElementFacade selectByValue(String value) {
        waitUntilElementAvailable();
        Select select = new KSelect(getElement(), driver);
        select.selectByValue(value);
        return this;
    }

    public String getSelectedValue() {
        waitUntilVisible();
        Select select = new KSelect(getElement(), driver);
        return select.getFirstSelectedOption().getAttribute("value");
    }


    public WebElementFacade selectByIndex(int indexValue) {
        waitUntilElementAvailable();
        Select select = new KSelect(getElement(), driver);
        select.selectByIndex(indexValue);
        return this;
    }

    void waitUntilElementAvailable() {
        waitUntilEnabled();
    }

    public boolean isPresent() {
        try {
            return getElement() != null && !getElement().getTagName().equals("");
        } catch (NoSuchElementException e) {
            return e.getMessage().contains("Element is not usable");
        }
    }

    public WebElementFacade waitUntilVisible() {
        waitFor().until(elementIsDisplayed());
        return this;
    }

    public WebElementFacade waitUntilVisible(long timeout) {
        try {
            waitFor(timeout).until(elementIsDisplayed());
        } catch (StaleElementReferenceException ser) {
            waitFor(timeout).until(elementIsDisplayed());
            return this;
        }
        return this;
    }

    public WebElementFacade waitUntilVisible(long timeout, TimeUnit timeUnit) {
        try {
            waitFor(timeout, timeUnit).until(elementIsDisplayed());
        } catch (StaleElementReferenceException ser) {
            waitFor(timeout, timeUnit).until(elementIsDisplayed());
            return this;
        }
        return this;
    }


    public WebElementFacade waitUntilPresent() {
        waitFor().until(elementIsPresent());
        return this;
    }

    ExpectedCondition<Boolean> elementIsDisplayed() {
        return driver1 -> {
            try {
                return (getElement() != null) && (getElement().isDisplayed());
            } catch (NullPointerException e) {
                // Selenium sometimes throws a NPE if the element is not present at all on the page.
                return false;
            }
        };
    }

    private ExpectedCondition<Boolean> elementIsPresent() {
        return driver1 -> isPresent();
    }

    private ExpectedCondition<Boolean> elementIsNotDisplayed() {
        return driver1 -> !isCurrentlyVisible();
    }

    private ExpectedCondition<Boolean> elementIsEnabled() {
//        LOGGER.debug(String.format("Wait till %s element will be enabled on the page %s", this, this.page));
        return driver1 -> {
            WebElement element = getElement();
            return ((element != null)
                    && element.isEnabled());
        };
    }

    private boolean hasValueAttribute(WebElement webElement) {
        String tag = webElement.getTagName().toLowerCase();
        return HTML_ELEMENTS_WITH_VALUE_ATTRIBUTE.contains(tag);

    }

    public WebDriverWait waitFor() {
        return new WebDriverWait(driver, webdriverClock, sleeper, TIME_OUT_IN_SECONDS, WAIT_FOR_ELEMENT_PAUSE_LENGTH);
    }

    public WebDriverWait waitFor(long timeoutInSeconds) {
        return new WebDriverWait(driver, webdriverClock, sleeper, timeoutInSeconds, WAIT_FOR_ELEMENT_PAUSE_LENGTH);
    }

    public Wait<WebDriver> waitFor(long timeout, TimeUnit timeUnit) {
        return new FluentWait<>(driver, webdriverClock, sleeper)
                .withTimeout(timeout, timeUnit)
                .pollingEvery(WAIT_FOR_ELEMENT_PAUSE_LENGTH, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class, NoSuchFrameException.class);
    }


    public WebElementFacade waitUntilNotVisible() {
        waitFor().until(elementIsNotDisplayed());
        return this;
    }


    public String getValue() {
        waitUntilPresent();
        return getElement().getAttribute("value");
    }


    public boolean isSelected() {
        return getElement().isSelected();
    }


    public String getText() {
        return getElement().getText();
    }


    public WebElementFacade waitUntilEnabled() {
        try {
            waitFor().until(elementIsPresent());
            waitFor().until(elementIsEnabled());
            return this;
        } catch (Exception ex) {
            waitFor().until(elementIsPresent());
//            waitForCondition().until(elementIsEnabled());
            return this;
        }
    }

    public String getTextValue() {
        waitUntilPresent();

        if (!isVisible()) {
            return "";
        }

        if (valueAttributeSupportedAndDefinedIn(getElement())) {
            return getValue();
        }

        if (!StringUtils.isEmpty(getElement().getText())) {
            return getElement().getText();
        }
        return "";
    }

    private boolean valueAttributeSupportedAndDefinedIn(final WebElement webElement) {
        return hasValueAttribute(webElement) && StringUtils.isNotEmpty(getValue());
    }

    /**
     * Wait for an element to be visible and enabled, and then click on it.
     */

    public void click() {
        waitUntilElementAvailable();
        if (((HasCapabilities) driver).getCapabilities().getBrowserName().equals("chrome")) {
            chromeClick();
        } else {
            try {
                getElement().click();
            } catch (WebDriverException we) {
                if (we.getMessage().contains("Element is not clickable")) {
                    scrollTo(getElement());
                    getElement().click();
                } else {
                    By label = By.xpath("./..//*[@for='" + getElement().getAttribute("id") + "']");
                    try {
                        scrollTo(getElement().findElement(label));
                    } catch (Throwable e) {/**/}
                    try {
                        getElement().findElement(label).click();
                    } catch (Throwable e) {/**/}
                }
            }
        }
    }

    private void chromeClick() {
        By label = By.xpath("./..//*[@for='" + getElement().getAttribute("id") + "']");
        try {
            getElement().click();
        } catch (WebDriverException we) {
            try {
                driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
                scrollTo(getElement().findElement(label));
            } catch (Throwable e) {/**/} finally {
                driver.manage().timeouts().implicitlyWait(Timeout.DEFAULT_IMPLICITLY_WAIT, TimeUnit.SECONDS);
            }
            try {
                if (we.getMessage().contains("Element is not clickable")) {
                    waitUntilElementAvailable();
                    waitFor().until(elementIsEnabled());
                    ((JavascriptExecutor) driver)
                            .executeScript("window.scrollTo(arguments[0],arguments[1]);",
                                    ((Locatable) getElement()).getCoordinates().inViewPort().getX(),
                                    ((Locatable) getElement()).getCoordinates().inViewPort().getY());
                    getElement().click();
                } else if (we.getMessage().contains("element not visible")) {
                    try {
                        getElement().findElement(label).click();
                    } catch (Throwable e) {/**/}
                }
            } catch (WebDriverException wbe) {
                waitFor().until(elementIsEnabled());
                ((JavascriptExecutor) driver)
                        .executeScript("window.scrollTo(arguments[0],arguments[1]);",
                                ((Locatable) getElement()).getCoordinates().inViewPort().getX(),
                                ((Locatable) getElement()).getCoordinates().inViewPort().getY());
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].click();", getElement());
            }
        }
    }

    public void clickByJS() {
        waitFor().until(elementIsEnabled());
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", getElement());
    }

    public void clickWithOffset() {
        Actions builder = new Actions(driver);
        int xOffset = (getElement().getSize().getHeight() * 5) / 100;
        int yOffset = (getElement().getSize().getHeight() * 3) / 100;
        builder
                .moveToElement(getElement(), xOffset, yOffset)
                .click()
                .perform();
        builder.release();
    }


    public void focus() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", getElement());
    }

    public void scrollTo(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        } catch (Throwable e) {/**/}
    }

    public WebElementFacade scrollTo() {
        scrollTo(getElement());
        return this;
    }

    public void clear() {
        scrollTo();
        try {
            getElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            getElement().clear();
        } catch (StaleElementReferenceException | InvalidElementStateException e) {
            getElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
//            getElement().clear();
        }
    }

    public String toString() {
        try {
            if (getElement() != null) {
                return getElement().toString();
            }
        } catch (NoSuchElementException e) {/**/}
        return "<Undefined web element>";
    }

    public void submit() {
        getElement().submit();
    }


    public void sendKeys(CharSequence... keysToSend) {
        getElement().sendKeys(keysToSend);
    }

    public String getTagName() {
        return getElement().getTagName();
    }

    public List<WebElement> findElements(By by) {
        return getElement().findElements(by);
    }

    public WebElement findElement(By by) {
        return getElement().findElement(by);
    }

    public boolean isDisplayed() {
        try {
            return getElement().isDisplayed();
        } catch (StaleElementReferenceException e) {
            return getElement().isDisplayed();
        }
    }

    public Point getLocation() {
        return getElement().getLocation();
    }

    public Dimension getSize() {
        return getElement().getSize();
    }


    public Rectangle getRect() {
        return getElement().getRect();
    }

    public String getCssValue(String propertyName) {
        return getElement().getCssValue(propertyName);
    }

    public WebElement getWrappedElement() {
        return getElement();
    }


    public Coordinates getCoordinates() {
        return ((Locatable) getElement()).getCoordinates();
    }

    public void waitForInvisibility() {
        try {
            waitFor().until(CustomExpectedConditions.invisibilityOfElement(getElement()));
        } catch (TimeoutException e) {
            if (getElement().isDisplayed()) {
//                LOGGER.warn("Element " + getElement().toString() + " still visible!!");
            }
        }
    }

    public void makeVisible() {
        String javaScript =
                "arguments[0].style.width = '1px'; arguments[0].style.height = '1px'; arguments[0].style.opacity = '1';";
        ((JavascriptExecutor) driver).executeScript(javaScript, getElement());
        waitUntilVisible();
    }

    public String getTextContent() {
        return getElement().getAttribute("textContent");
    }

    public String getHiddenValueByJS() {
        Object result =
                ((JavascriptExecutor) driver).executeScript("return arguments[0].value;", this);
        return (String) result;
    }

    public String getValueByAttribute() {
        return getAttribute("value");
    }

    public WebElementFacade setAttribute(String attributeName, String attributeValue) {
        String script = "arguments[0].setAttribute(arguments[1],arguments[2]);";
        ((JavascriptExecutor) driver).executeScript(script, this.webElement, attributeName, attributeValue);
        return this;
    }

    public WebElementFacade setValueAttribute(String value) {
        setAttribute("value", value);
        return this;
    }

    public void clearStyle() {
        String javaScript = "arguments[0].style = '';";
        ((JavascriptExecutor) driver).executeScript(javaScript, getElement());
    }

    public void nKeys(int n, CharSequence action) {
        this.withTimeoutOf(1, TimeUnit.SECONDS);
        for (int i = 0; i < n; i++) {
            this.sendKeys(action);
        }
    }

    public void click(Integer numberOfClicks) {
        this.withTimeoutOf(1, TimeUnit.SECONDS);
        for (int i = 0; i < numberOfClicks; i++) {
            this.click();
        }
    }


    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return ((RemoteWebDriver) driver).getScreenshotAs(outputType);
    }

    public String getOnlyNumbersFromElement() {
        return getElement().getText().replaceAll("\\D+", "");
    }


    public String getOnlyIntFromElement() {
        String value = null;
        String timeRegex = "\\D*(\\d+)\\.(\\d+).*";
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(getElement().getText());
        if (matcher.matches()) {
            String dollars = matcher.group(1);
            String cents = matcher.group(2);
            if (cents.length() == 1) cents += "0";
            value = (dollars + cents);
        }
        return value;
    }


}
