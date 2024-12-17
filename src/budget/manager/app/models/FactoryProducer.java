package budget.manager.app.models;

public class FactoryProducer {
    public static AbstractFactory getFactory(String fileName) {
        if (fileName.contains("users")) {
            return new UserFactory();
        } else if (fileName.contains("transactions")) {
            return new TransactionFactory();
        } else if (fileName.contains("categories")) {
            return new CategoryFactory();
        }

        return null;
    }
}
