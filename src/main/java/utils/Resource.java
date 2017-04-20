package utils;

import java.io.InputStream;


/**
 * Created by slepkan on 4/19/17
 */
public class Resource {

    public static String OS_NAME = System.getProperty("os.name").toLowerCase();

    public static InputStream getResourceAsStream(String path) {
        return Resource.class.getClassLoader().getResourceAsStream(path);
    }

}
