import utils.User;

import java.io.File;

/**
 * Created by slepkan on 4/18/17
 */
public class BaseConfiguration {

    static final String BROWSER_DRIVERS_PATH = "./selenium/";

    String absoluteTestPath = new File(BROWSER_DRIVERS_PATH).getAbsolutePath();

//    protected static final User ADMIN = new User(testingProperties.getProperty("admin.login"),
//            testingProperties.getProperty("admin.password");


}
