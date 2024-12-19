package budget.manager.app.services.csv.interfaces;

import java.io.Serializable;
import java.util.List;

public interface Reader {
     <T extends Serializable> List<T> read(String filePath, String fileName);
}
