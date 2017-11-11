package Modelo.Managers;

import java.util.ArrayList;
import java.util.List;

import Modelo.HelpQuestion;

public class HelpManager {
     public List<HelpQuestion> obtenerPreguntas(){

         List<HelpQuestion> preguntas = new ArrayList<>();
         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo enviar visitas completadas?"; Answer="Desplazar el menú lateral, por medio del botón en la esquina superior, o bien deslizando la pantalla de izquierda a derecha. Luego, seleccionar la opción \"Sincronizar\". Tenga en cuenta, que esta acción también obtendrá nuevas visitas asignadas.";}});
         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo recibir nuevas visitas?"; Answer="Desplazar el menú lateral, por medio del botón en la esquina superior, o bien deslizando la pantalla de izquierda a derecha. Luego, seleccionar la opción \"Sincronizar\". Tenga en cuenta, que esta acción también enviará las visitas que se encuentren finalizadas a la ART.";}});
         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo acceder a las tareas de una visita?"; Answer="Ubicado en la pantalla principal, podrá ver el listado de visitas asignadas. Al presionar sobre alguna de ellas, lo llevará a una nueva pantalla donde verá los detalles de la misma. En esta nueva pantalla, en la parte superior derecha de la pantalla, tendrá las distintas opciones disponibles para la visita seleccionada.";}});
         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo borrar un empleado del formulario R.A.R.?"; Answer="Ubicado en la pantalla correspondiente al formulario R.A.R., si ya dispone de algún empleado cargado, mantenga presionado sobre el mismo por unos segundos.  Se le consultará si desea borrar el empleado. Si confirma, el empleado será borrado del formulario R.A.R.";}});
         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo editar un empleado del formulario R.A.R.?"; Answer="Ubicado en la pantalla correspondiente al formulario R.A.R., si ya dispone de algún empleado cargado, presione sobre el empleado que desee editar. Esta acción lo llevará a una nueva pantalla donde podrá editar los datos del mismo.";}});
         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo borrar un empleado del formulario de capacitación?"; Answer="Ubicado en la pantalla correspondiente al formulario de capacitación, si ya dispone de algún empleado cargado, mantenga presionado sobre el mismo por unos segundos.  Se le consultará si desea borrar el empleado. Si confirma, el empleado será borrado del formulario R.A.R.";}});
         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo editar un empleado del formulario de capacitación?"; Answer="Ubicado en la pantalla correspondiente al formulario de capacitación, si ya dispone de algún empleado cargado, presione sobre el empleado que desee editar. Esta acción lo llevará a una nueva pantalla donde podrá editar los datos del mismo.";}});
         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo tomar una medición de ruido?"; Answer="Ubicado en la pantalla de constancia de visita, presionar sobre el botón que tiene el dibujo de un micrófono. Esta acción lo llevará a una nueva pantalla, en ella presione el botón \"Medir\", esto iniciará la medición de ruido. Cuando quiera detener la medición, presione el botón \"Parar\". Agregue una descripción para identificar la medición y presione el botón \"Registrar\". La medición aparecerá en el listado disponible en esta misma pantalla.";}});
         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo tomar una foto?"; Answer="Ubicado en la pantalla de constancia de visita, presionar sobre el botón que tiene el dibujo de una cámara. Esta acción abrirá la camara del dispositivo. Una vez tomada la foto, puede confirmarla o descartarla.";}});
         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo borrar una medición de ruido?"; Answer="Ubicado en la pantalla de constancia de visita, presionar sobre el botón que tiene el dibujo de un micrófono. Esta acción lo llevará a una nueva pantalla, en ella, en caso de haber realizado alguna medición, podrá ver el listado de las mismas. Presione sobre la que desee borrar. Se le consultará si desea borrar la medición. Si confirma, la medición será borrada.";}});
         preguntas.add(new HelpQuestion(){{ Question = "¿Cómo borrar una foto?"; Answer="Ubicado en la pantalla de constancia de visita, en caso de ya haber tomado alguna foto, debajo del botón con el dibujo de un micrófono aparecerá una leyenda que dice \"Ver fotos\", presione sobre el mismo. Esto lo llevará a una nueva pantalla, donde podrá ver todas las fotos tomadas en esta visita. Presione sobre la que desee borrar. Se le consultará si desea borrar la foto. Si confirma, la misma será borrada.";}});
         return preguntas;
     }
}
