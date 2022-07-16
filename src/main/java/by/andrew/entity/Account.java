package by.andrew.entity;

public class Account {
    private String cookie;
    private String login;
    private String password;
    private Long accaunt_id;
    private String name;
    private String phone;
    private String token;

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public Long getAccaunt_id() {
        return accaunt_id;
    }

    public void setAccaunt_id(Long accaunt_id) {
        this.accaunt_id = accaunt_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
