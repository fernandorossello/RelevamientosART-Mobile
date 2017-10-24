package Modelo;

import java.io.Serializable;
import java.util.List;

import Modelo.Enums.EnumStatus;

public class RGRLResult extends Result implements Serializable{

    public List<Question> questions;

    @Override
    public EnumStatus getStatus() {
        for (Question pregunta : questions) {
            //Si tiene alguna pregunta sin responder devuelve pendiente
        }
        return EnumStatus.FINALIZADA;
    }
}
