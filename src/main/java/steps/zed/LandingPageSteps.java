package steps.zed;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import pages.zed.LandingPage;
import steps.BaseStep;

public class LandingPageSteps extends BaseStep<LandingPage> {

    public LandingPageSteps(WebDriver webDriver){
        setPageObject(PageFactory.initElements(webDriver, LandingPage.class));
    }

    public void clickLogOutLink(){
        pageObject().clickLogOutButton();
    }

}
