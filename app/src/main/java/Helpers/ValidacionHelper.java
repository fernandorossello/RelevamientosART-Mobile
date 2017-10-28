package Helpers;

import java.util.Date;

import Excepciones.EmptyException;
import Excepciones.LengthException;
import Excepciones.MaxDateException;
import Excepciones.MinDateException;
import Excepciones.ValidationException;

public class ValidacionHelper {

        //Si el campo está vacío arrojará una excepción.
        public static void NullOrEmpty(String texto,String nombreCampo) throws ValidationException{
            if(texto == null || texto.trim().isEmpty()){
                throw new EmptyException(nombreCampo);
            }
        }

        //Si la fecha es posterior a la fecha máxima indicada arrojará una excepción.
        public static void FechaPosterior(Date fecha, Date fechaMaxima, String nombreCampo) throws ValidationException{
            if (fecha.after(fechaMaxima)){
                throw new MaxDateException(nombreCampo,fechaMaxima);
            }
        }

        //Si la fecha es anterior a la fecha mínima indicada arrojará una excepción.
        public static void FechaAnterior(Date fecha, Date fechaMinima, String nombreCampo) throws ValidationException{
            if (fecha.before(fechaMinima)){
                throw new MinDateException(nombreCampo,fechaMinima);
            }
        }

        //Si el campo se encuentra vacío o tiene una cantidad incorrecta de caracteres arrojará una excepción
        public static void CantidadCaracteres(String texto, int cantidad, String nombreCampo)throws ValidationException{
            NullOrEmpty(texto,nombreCampo);

            if(texto.length() != cantidad){
                throw new LengthException(nombreCampo, cantidad);
            }
        }

    public static void Null(Object object ,String nombreCampo) throws ValidationException{
        if(object == null){
            throw new EmptyException(nombreCampo);
        }
    }

}
