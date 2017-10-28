package Modelo.Managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Helpers.DBHelper;
import Modelo.Enums.EnumAnswer;
import Modelo.Question;
import Modelo.RGRLResult;

/**
 * Created by Tomás on 8/10/2017.
 */

public class QuestionManager extends Manager<Question> {
    public QuestionManager(){}

    public QuestionManager(DBHelper helper){
        this.dbHelper = helper;
    }

    @Override
    public void persist(Question item) throws SQLException {
        Dao daoque = dbHelper.getQuestionDao();

        if (!daoque.idExists(item.idDB)) {
            daoque.create(item);
        } else {
            daoque.update(item);
        }
    }

    public List<Question> generarPreguntasParaResult(final RGRLResult rgrlResult) {
        List<Question> questions = new ArrayList<>();

        questions.add(new Question(){
            {
                id = 1;
                description = "¿Dispone del Servicio de Higiene y Seguridad?";
                answer_code = EnumAnswer.NULL.id;
                result = rgrlResult;
                category = "Servicio de Higiente y Seguridad en el trabajo";
            }
        });

        questions.add(new Question(){
            {
                id = 2;
                description = "¿Posee documentación actualizada con registración de todas las acciones tendientes a cumplir la misión fundamental y los objetivos de prevención de riesgos, establecidos en la legislación vigente?";
                answer_code = EnumAnswer.NULL.id;
                result = rgrlResult;
                category = "Servicio de Higiente y Seguridad en el trabajo";
            }
        });

        questions.add(new Question(){
            {
                id = 3;
                description = "¿Dispone del Servicio de Medicina del trabajo?";
                answer_code = EnumAnswer.NULL.id;
                result = rgrlResult;
                category = "Servicio de Medicina del trabajo";
            }
        });

        questions.add(new Question(){
            {
                id = 4;
                description = "¿Posee documentación actualizada con registración de todas las acciones tendientes a cumplir la misión fundamental, ejecutando acciones de educación sanitaria, socorro, vacunación y estudios de ausentismo por morbilidad?";
                answer_code = EnumAnswer.NULL.id;
                result = rgrlResult;
                category = "Servicio de Medicina del trabajo";
            }
        });

        questions.add(new Question(){
            {
                id = 5;
                description = "¿Se realizan los exámenes médicos periódicos?";
                answer_code = EnumAnswer.NULL.id;
                result = rgrlResult;
                category = "Servicio de Medicina del trabajo";
            }
        });

        return questions;
    }
}
