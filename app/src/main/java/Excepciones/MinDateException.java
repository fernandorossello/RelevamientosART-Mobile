package Excepciones;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MinDateException extends ValidationException{

    public MinDateException(String fieldName, Date minDate) {
        super("La fecha " + fieldName + " no puede ser anterior al " + new SimpleDateFormat("dd/MM/yy").format(minDate));
    }
}
