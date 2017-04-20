package pages.zed;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.PageObject;
import utils.WebElementFacade;

/**
 * Created by slepkan on 4/20/17
 */
public class LandingPage extends PageObject {

    @FindBy(xpath = ".//*[@id='page-wrapper']/div[1]/nav/ul/li[3]/a")
    private WebElement logOutLink;

    public LandingPage(WebDriver driver) {
        super(driver);
    }

    public WebElementFacade logOutLink() {
        return element(logOutLink);
    }

    public void clickLogOutButton(){
        logOutLink.click();
    }
}
