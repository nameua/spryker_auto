import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import settings.Zed;
import utils.enums.Driver;

/**
 * Created by slepkan on 4/13/17
 */

public class ZedLoginVerification extends BaseConfiguration {

    @Test
    public void loginToZed() {
        System.setProperty(Driver.getBroserKey(Driver.CHROME.name()), absoluteTestPath + "/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get(Zed.LOCAL.getURL());
        driver.close();
    }
}
