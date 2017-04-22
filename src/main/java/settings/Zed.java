package settings;

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
