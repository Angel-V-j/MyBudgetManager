package budget.manager.app.models;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String username;
    private String password;
    private Currency currency;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s", id, username, password, currency);
    }
}