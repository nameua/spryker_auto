package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class KSelect extends Select {

    private static final By arrow = By.className("selectBox-arrow");
    private static final By options = By.xpath("//*[contains(@class,'selectBox-options-top') or"
            + " contains(@class,'selectBox-options-bottom') and contains(@style, 'width')]");
    private WebElement parent;
    private WebDriver driver;
    private WebElement element;
    private WebDriverWait wait;

    public KSelect(WebElement element, WebDriver driver) {
        super(element);
        this.parent = element.findElement(By.xpath("./.."));
        this.driver = driver;
        this.element = element;
        this.wait = new WebDriverWait(driver, 1);
    }
    public void scrollTo() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", parent);
    }

    public void scrollTo(WebElement element) {
        parent = element;
        scrollTo();
    }

    @Override
    public boolean isMultiple() {
        return super.isMultiple();
    }

    @Override
    public List<WebElement> getOptions() {
        return super.getOptions();
    }

    @Override
    public List<WebElement> getAllSelectedOptions() {
        return super.getAllSelectedOptions();
    }

    @Override
    public WebElement getFirstSelectedOption() {
        return super.getFirstSelectedOption();
    }

    @Override
    public void selectByVisibleText(String text) {
        openDropDown();
        WebElement option = driver.findElement(options).findElement(By.linkText(text));
        scrollTo(option);
        option.click();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {/**/}
    }

    public String selectRandomOption() {
        openDropDown();
        List<WebElement> opts = driver.findElement(options).findElements(By.tagName("a"));
        opts.remove(0);
        Collections.shuffle(opts);
        String text = opts.get(0).getText();
        opts.get(0).click();
        return text;
    }

    @Override
    public void selectByIndex(int index) {
        By realOption = By.xpath(".//option[" + (index + 1) + "]");
        openDropDown();
        String text = element.findElement(realOption).getAttribute("textContent");
        driver.findElement(options).findElement(By.linkText(text.trim())).click();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {/**/}
    }

    @Override
    public void selectByValue(String value) {
        By realOption = By.xpath(".//option[@value='" + value + "']");
        try {
            openDropDown();
        } catch (Exception ex) {/**/}
        String text = element.findElement(realOption).getAttribute("textContent");
        if (driver.findElement(options).findElements(By.linkText(text.trim())).size() > 0) {
            text = text.trim();
        }
        if (driver.findElement(options).findElements(By.linkText(text)).size() > 0) {
            scrollTo(driver.findElement(options).findElement(By.linkText(text)));
            driver.findElement(options).findElement(By.linkText(text)).click();
        } else {
            By opt = By.xpath(".//*[@rel='" + value + "']");
            scrollTo(driver.findElement(options).findElement(opt));
            driver.findElement(options).findElement(opt).click();
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {/**/}
    }

    public void selectByValueForm(String value) {
        By realOption = By.xpath("//option[@value='" + value + "']");
        element.click();
        wait.until(ExpectedConditions.elementToBeClickable(element));
        scrollTo(element.findElement(realOption));
        element.findElement(realOption).click();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {/**/}
    }

    private void openDropDown() {
        scrollTo();
        arrowClick();
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(options));
        } catch (TimeoutException e) {
            arrowClick();
            wait.until(ExpectedConditions.presenceOfElementLocated(options));
        }
    }

    private void arrowClick() {
        WebElement arrowElement = parent.findElement(arrow);
        wait.until(ExpectedConditions.elementToBeClickable(arrowElement));
        try {
            arrowElement.click();
        } catch (WebDriverException e) {
            if (!driver.findElement(options).isDisplayed()) {
                throw e;
            }
        }
    }

    public String selectRandomValue() {
        String randomValue = getRandomValue();
        selectByValue(randomValue);
        return randomValue;
    }

    public String selectRandomText() {
        String randomText = getRandomText();
        selectByVisibleText(randomText);
        return randomText;
    }

    public String getRandomText() {
        List<WebElement> options_ = super.getOptions();
        options_.remove(0);
        return options_.get(
                options_.size() <= 1 ? 0 : new Random().nextInt(options_.size()))
                .getAttribute("textContent").trim();
    }

    public String getRandomValue() {
        List<WebElement> options_ = super.getOptions();
        options_.remove(0);
        if (!options_.get(options_.size() - 1).isEnabled())
            options_.remove(options_.size() - 1);
        return options_.get(
                options_.size() <= 1 ? 0 : new Random().nextInt(options_.size()))
                .getAttribute("value").trim();
    }

    public String getFirstSelectedOptionText() {
        return new Select(element).getFirstSelectedOption().getAttribute("textContent");
    }

    public String getFirstSelectedOptionValue() {
        return new Select(element).getFirstSelectedOption().getAttribute("value");
    }

    public boolean isCurrentlyVisible() {
        return parent.isDisplayed();
    }

    public void waitUntilVisible() {
        new WebDriverWait(driver, Timeout.DEFAULT_IMPLICITLY_WAIT)
                .until(ExpectedConditions.visibilityOf(parent));
    }


    public void selectByRel(String rel) {
        openDropDown();
        By relXpath = By.xpath(".//*[@rel='" + rel + "']");
        scrollTo(driver.findElement(options).findElement(relXpath));
        driver.findElement(options).findElement(relXpath).click();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {/**/}
    }



}
