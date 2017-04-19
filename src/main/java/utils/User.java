package utils;

/**
 * Created by slepkan on 4/18/17
 */
public class User {

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String login;
    private String password;

    public User (String login, String password) {
        this.login = login;
        this.password = password;
    }
}
