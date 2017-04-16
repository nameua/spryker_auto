import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

/**
 * Created by slepkan on 4/13/17
 */

public class MyFirstTest {

    @Test
    public void startWebDriver(){
        System.setProperty("webdriver.chrome.driver", "//Users/slepkan/IdeaProjects/autotest_spryker/src/driver/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.google.com");
        driver.close();
   }
}
