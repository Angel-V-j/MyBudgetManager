package budget.manager.app.models;

import java.io.Serializable;

public class Category implements Serializable {

    private int id;
    private int userId;
    private String name;
    private boolean isIncome;

    public Category() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIsIncome(boolean type) {
        this.isIncome = type;
    }

    @Override
    public String toString() {
        return String.format("%d,%d,%s,%b", id, userId, name, isIncome);
    }
}