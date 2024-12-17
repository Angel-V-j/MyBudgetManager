package budget.manager.app.managers.interfaces;

import java.io.Serializable;
import java.util.List;

public interface Writer {
    void write(List<? extends Serializable> list, String fileName);
}
