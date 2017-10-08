package Excepciones;


public class EmptyException extends ValidationException{

    public EmptyException(String fieldName) {
        super("El campo " + fieldName + " no puede estar vacío");
    }
}
