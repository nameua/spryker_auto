import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import settings.Yves;

public class YvesLoginVerification {

    @Test
    public void loginToYves() {
        System.setProperty("webdriver.chrome.driver", "//Users/slepkan/IdeaProjects/spryker_auto/selenium/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get(Yves.LOCAL.getURL());
        driver.close();
    }
}
