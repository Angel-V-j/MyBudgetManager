package budget.manager.app.services.csv.interfaces;

import java.io.Serializable;
import java.util.ArrayList;

public interface Exporter {
    static void exportData(ArrayList<? extends Serializable> data, String defaultFileName) {}
}
