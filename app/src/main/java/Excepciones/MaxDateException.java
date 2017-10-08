package Excepciones;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MaxDateException extends ValidationException {

    public MaxDateException(String fieldName, Date maxDate) {
        super("La fecha " + fieldName + " no puede ser posterior al " + new SimpleDateFormat("dd/MM/yy").format(maxDate));
    }
}
