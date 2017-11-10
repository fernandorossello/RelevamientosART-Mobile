package Modelo.Managers;

import java.util.ArrayList;
import java.util.List;

import Modelo.HelpQuestion;

public class HelpManager {
     public List<HelpQuestion> obtenerPreguntas(){

         List<HelpQuestion> preguntas = new ArrayList<>();

         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo borrar una foto?";}});
         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo borrar una medición de ruido?";}});

         return preguntas;
     }
}
