package pages.zed;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.PageObject;
import utils.WebElementFacade;


/**
 * Created by slepkan on 4/18/17
 */
public class LoginPage extends PageObject {


    @FindBy (id = "auth_username")
    private WebElement userNameTextField;

    @FindBy (id = "auth_password")
    private WebElement passwordTextField;

    @FindBy (xpath = "//button")
    private WebElement submitButton;

    @FindBy (css = ".form-group.forgot>a")
    private WebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public WebElementFacade userNameTextField() {
        return element(userNameTextField);
    }

    public WebElementFacade passwordTextField() {
        return element(passwordTextField);
    }

    public WebElementFacade submitButton() {
        return element(submitButton);
    }

    public void enterTextInUserNameTextField(String text) {
        userNameTextField().clear();
        userNameTextField().sendKeys(text);
    }

    public void enterTextInPasswordTextField(String text) {
//        passwordTextField().clear();
        passwordTextField().sendKeys(text);
    }

    public void clickSubmitButton() {
        submitButton().click();
    }
}
