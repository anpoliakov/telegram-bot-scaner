package by.andrew.entity;

public class Accaunt {
    private String cookie;
    private String login;
    private String password;
    private String name;
    private String phone;
    private String token;
    private String lastLogin;
    private int region;
    private int gender;

    public Accaunt(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public int getRegion() {
        return region;
    }

    public int getGender() {
        return gender;
    }
}
