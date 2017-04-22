package settings;

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
