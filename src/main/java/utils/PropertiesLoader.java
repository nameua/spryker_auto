package utils;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private static final Logger logger = Logger.getLogger(PropertiesLoader.class.getName());
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
            logger.error(
                    "FileNotFoundException on method initProperties  \n File in path  = '" + path
                            + "' are not found");
        } catch (IOException e) {
            logger.error("IOException on method initProperties");
        }
        return properties;
    }
}
