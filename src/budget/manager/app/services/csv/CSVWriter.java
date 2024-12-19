package budget.manager.app.services.csv;

import budget.manager.app.services.csv.interfaces.Writer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class CSVWriter implements Writer {

    @Override
    public void write(List<? extends Serializable> list, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Serializable item : list) {
                writer.write(item.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
