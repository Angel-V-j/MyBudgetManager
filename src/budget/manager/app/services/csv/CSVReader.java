package budget.manager.app.services.csv;

import budget.manager.app.services.csv.interfaces.Reader;
import budget.manager.app.models.AbstractFactory;
import budget.manager.app.models.FactoryProducer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVReader implements Reader {

    @Override
    public <T extends Serializable> List<T> read(String filePath, String fileName) {
        List<T> list = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            AbstractFactory abstractFactory = FactoryProducer.getFactory(fileName);
            while ((line = bufferedReader.readLine()) != null) {
                T t = (T) abstractFactory.createFromCsv(line);
                if (t != null) {
                    list.add(t);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }

        return list;
    }
}
