package Excepciones;


public class LengthException extends ValidationException{

    public LengthException(String fieldName, int cantidad) {
        super("El campo " + fieldName + " debe tener " + cantidad + " caracteres");
    }
}
