package utils;

import java.io.InputStream;

public class Resource {

    public static String OS_NAME = System.getProperty("os.name").toLowerCase();

    public static InputStream getResourceAsStream(String path) {
        return Resource.class.getClassLoader().getResourceAsStream(path);
    }

}
