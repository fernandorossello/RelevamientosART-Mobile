package Modelo.Arquitectura;

import android.support.v4.media.MediaBrowserServiceCompat;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.sql.SQLException;

import Excepciones.ValidationException;
import Helpers.DBHelper;
import Helpers.PDFHelper;
import Modelo.Enums.EnumStatus;
import Modelo.Managers.ResultManager;
import Modelo.Result;
import Modelo.Task;
import Modelo.Visit;

public class GestorPDF {

    private DBHelper _dbHelper;
    private PDFHelper _pdfHelper;

    public GestorPDF(DBHelper dbHelper){
        this._dbHelper = dbHelper;
        this._pdfHelper = new PDFHelper();
    }

    public void GenerarPDFParaTarea(Task tarea) throws IOException, DocumentException, Exception, SQLException {
        //TODO:Hacer método privado y evitar que se generen los PDF a demanda
        ResultManager resultManager = new ResultManager(_dbHelper);
        Result result = resultManager.getResult(tarea);
        if(result != null && result.getStatus() == EnumStatus.FINALIZADA) {
            _pdfHelper.crearPDF(result, tarea);
            resultManager.persist(result);
        } else {
            throw new Exception("Tarea sin finalizar");
        }

    }

    public void GenerarPDFParaVisita(Visit visit) throws Exception {

        for (Task tarea:visit.tasks) {
            this.GenerarPDFParaTarea(tarea);
        }

        _pdfHelper.crearPDF(visit);
    }

}
