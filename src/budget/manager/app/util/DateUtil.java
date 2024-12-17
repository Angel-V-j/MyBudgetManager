package budget.manager.app.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate stringToDate(String dateString) throws DateTimeParseException {
        return LocalDate.parse(dateString, DATE_TIME_FORMATTER);
    }

    public static String dateToString(LocalDate date) {
        return date.format(DATE_TIME_FORMATTER);
    }

    public DateTimeFormatter getDateFormat() {
        return DATE_TIME_FORMATTER;
    }
}
