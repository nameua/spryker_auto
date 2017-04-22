package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.concurrent.TimeUnit;

public class CustomExpectedConditions {

    public static ExpectedCondition<Boolean> attributeIs(final By locator, final String attribute,
                                                         final String value) {
        return new ExpectedCondition<Boolean>() {
            private String currentValue = null;

            @Override
            public Boolean apply(WebDriver driver) {
                currentValue = driver.findElement(locator).getAttribute(attribute);
                if (currentValue == null) {
                    currentValue = driver.findElement(locator).getCssValue(attribute);
                }
                return value.equals(currentValue);
            }

            @Override
            public String toString() {
                return String
                        .format("value to be \"%s\". Current value: \"%s\"", value, currentValue);
            }
        };
    }

    public static ExpectedCondition<Boolean> invisibilityOfElement(final WebElement element) {
        return new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver webDriver) {
                Boolean visible = false;
                try {
                    webDriver.manage().timeouts().implicitlyWait(1L, TimeUnit.SECONDS);
                    visible = element.isDisplayed();
                } catch (Exception e) {/**/} finally {
                    webDriver.manage().timeouts()
                            .implicitlyWait(Timeout.DEFAULT_IMPLICITLY_WAIT, TimeUnit.SECONDS);
                }
                return !visible;
            }

            @Override
            public String toString() {
                return "invisibility of element " + element;
            }
        };
    }


}
