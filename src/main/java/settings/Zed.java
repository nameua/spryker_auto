package settings;

/**
 * Created by slepkan on 4/17/17
 */
public enum Zed {
    LOCAL("http://zed.de.demoshop.local/auth/login");
    String url;


    Zed(String url) {
        this.url =  url;
    }

    public String getURL(){
        return url;
    }
}
