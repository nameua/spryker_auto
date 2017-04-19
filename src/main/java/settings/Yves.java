package settings;

/**
 * Created by slepkan on 4/17/17
 */
public enum Yves {
    LOCAL("http://www.de.demoshop.local/");
    String url;


    Yves(String url) {
        this.url =  url;
    }

    public String getURL(){
        return url;
    }

}
