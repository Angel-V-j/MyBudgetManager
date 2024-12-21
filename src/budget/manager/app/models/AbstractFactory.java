package budget.manager.app.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public abstract class AbstractFactory<T extends Serializable> {
    public abstract User create(int id, String username, String password, String currency);
    public abstract Transaction create(int id, int userId, int categoryId, double amount, String currency, LocalDate date, String info);
    public abstract Category create(int id, int userId, String name, boolean isIncome);
    public abstract T createFromCsv(String csvLine);
    public abstract T createFromRSet(ResultSet resultSet) throws SQLException;
}
