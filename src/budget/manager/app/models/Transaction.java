package budget.manager.app.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Locale;

import static budget.manager.app.util.DateUtil.dateToString;

public class Transaction implements Serializable {

    private int id;
    private int userId;
    private int categoryId;
    private double amount;
    private Currency currency;
    private LocalDate date;
    private String description;

    public Transaction() {
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getLocalDate() {
        return date;
    }

    public void setLocalDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%d,%d,%d,%.2f,%s,%s", id, userId, categoryId, amount, dateToString(date), description);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}