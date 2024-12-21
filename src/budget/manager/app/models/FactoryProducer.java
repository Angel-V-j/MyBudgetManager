package budget.manager.app.models;

public class FactoryProducer {
    public static AbstractFactory getFactory(String factoryType) {
        if (factoryType.toLowerCase().contains("users")) {
            return new UserFactory();
        } else if (factoryType.toLowerCase().contains("transactions")) {
            return new TransactionFactory();
        } else if (factoryType.toLowerCase().contains("categories")) {
            return new CategoryFactory();
        }

        return null;
    }
}
