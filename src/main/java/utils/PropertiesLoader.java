package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by slepkan on 4/19/17
 */
public class PropertiesLoader {

    private static final String PATH = "user.properties";

    public static Properties getTestingProperties() {
        return initProperties(PATH);
    }

    public static Properties initProperties(String path) {
        Properties properties = null;
        try (InputStream propertiesResource = Resource.getResourceAsStream(path)) {
            properties = new Properties();
            properties.load(propertiesResource);
        } catch (FileNotFoundException e) {
//            logger.error(
//                    "FileNotFoundException on method initProperties  \n File in path  = '" + path
//                            + "' are not found");
        } catch (IOException e) {
//            logger.error("IOException on method initProperties");
        }
        return properties;
    }
}
