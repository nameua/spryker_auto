import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import pages.zed.LandingPage;
import settings.Zed;
import steps.zed.LandingPageSteps;
import steps.zed.LoginPageSteps;
import utils.PropertiesLoader;
import utils.User;
import utils.enums.Driver;

import java.io.File;
import java.util.Properties;

public class ZedLoginVerification {

    private final Logger logger = Logger.getLogger(this.getClass());

    protected WebDriver webDriver;
    private String absoluteTestPath = new File(BROWSER_DRIVERS_PATH).getAbsolutePath();
    static final String BROWSER_DRIVERS_PATH = "./selenium/";
    private static final Properties testingProperties = PropertiesLoader.getTestingProperties();
    protected static final User ADMIN = new User(testingProperties.getProperty("admin.login"),
            testingProperties.getProperty("admin.password"));
    protected LoginPageSteps loginPageSteps;

    @Test
    public void loginToZed() {
            logInToTheZedApplication();
    }

    public void logInToTheZedApplication() {
        webDriver = navigateToZedLandingPage();
        loginPageSteps = new LoginPageSteps(webDriver);
        loginPageSteps.fillOutLoginTextField(ADMIN.getLogin());
        loginPageSteps.fillOutPasswordTextField(ADMIN.getPassword());
        webDriver.close();
    }

    protected WebDriver navigateToZedLandingPage(){
        System.setProperty(Driver.getBroserKey(Driver.CHROME.name()),absoluteTestPath +"/chromedriver");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get(Zed.LOCAL.getURL());
        logger.info("Trying to navigate login page:" + Zed.LOCAL.getURL());
        return webDriver;
    }

}
