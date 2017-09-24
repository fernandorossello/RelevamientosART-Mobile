package Helpers;

import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Modelo.Institution;
import Modelo.Task;
import Modelo.Visit;

public class PDFHelper {

    private final Font fontLabel = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD | Font.UNDERLINE);
    private final Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD | Font.ITALIC);
    private final Font fontTexto = FontFactory.getFont(FontFactory.HELVETICA, 11);
    private final Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD | Font.ITALIC);
    Document document = new Document(PageSize.A4);


    public void crearPDF(Task tarea) throws IOException, DocumentException {

        File archivo = crearArchivo(tarea.getTypeShortName()+ "_"+tarea.visit.institution.name);
        FileOutputStream output = new FileOutputStream(archivo);

        PdfWriter.getInstance(document, output);

        document.open();

        document.add(new Paragraph(tarea.getTypeName(),fontTitulo));

        //generarLogo(document);
        generarCabeceraVisita(tarea.visit, document);

        document.close();
    }

    public void crearPDF(Visit visit) throws IOException, DocumentException {

        File archivo = crearArchivo("Constancia_visita" + "_"+ visit.institution.name);
        FileOutputStream output = new FileOutputStream(archivo);

        PdfWriter.getInstance(document, output);

        document.open();

        document.add(new Paragraph("Constancia de visita",fontTitulo));

        //generarLogo(document);
        generarCabeceraVisita(visit, document);

        document.close();
    }


    private String formatearFecha(Date date){
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        return sdf.format(date);
    }

    private void generarLogo(Document document) throws IOException,DocumentException {
            Image foto = Image.getInstance("logo.png");
            foto.scaleToFit(100, 100);
            foto.setAlignment(Chunk.ALIGN_LEFT);
            document.add(foto);
    }

    private File crearArchivo(String nombre) throws IOException {
            File pdfFolder = new File(Environment.getExternalStorageDirectory(), "ARTpdf");

        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
            Log.i(this.toString(), "Directorio PDF creado");
        }

            File pdfFile = new File(pdfFolder, nombre + ".pdf");
            return pdfFile;
    }


    private void generarCabeceraVisita(Visit visit, Document documento) throws DocumentException{
        Institution institution = visit.institution;

        Paragraph lineaSubtitulo = new Paragraph("Datos de la institución",fontSubtitulo);

        Paragraph linea1 = new Paragraph();
        Phrase item11 = new Phrase();
        linea1.add(new Chunk("Razón social:",fontLabel));
        linea1.add(new Chunk(institution.name,fontTexto));
        linea1.add(new Chunk("Contrato N°:",fontLabel));
        linea1.add(new Chunk(institution.contract,fontTexto));
        linea1.add(new Chunk("Fecha visita:",fontLabel));
        linea1.add(new Chunk(formatearFecha(visit.to_visit_on),fontTexto));

        Paragraph linea2 = new Paragraph();
        linea2.add(new Chunk("Domicilio:",fontLabel));
        linea2.add(new Chunk(institution.address,fontTexto));
        linea2.add(new Chunk("CUIT:",fontLabel));
        linea2.add(new Chunk(institution.cuit,fontTexto));

        Paragraph linea3 = new Paragraph();
        linea3.add(new Chunk("Actividad:",fontLabel));
        linea3.add(new Chunk(institution.activity,fontTexto));
        linea3.add(new Chunk("CIIU:",fontLabel));
        linea3.add(new Chunk(institution.ciiu,fontTexto));

        Paragraph linea4 = new Paragraph();
        linea4.add(new Chunk("N° y Nombre del establecimiento:",fontLabel));
        linea4.add(new Chunk(institution.number,fontTexto));
        linea4.add(new Chunk("N° trab. Estab.:",fontLabel));
        linea4.add(new Chunk(Integer.toString(institution.workers_count),fontTexto));

        Paragraph linea5 = new Paragraph();
        linea5.add(new Chunk("Código postal:",fontLabel));
        linea5.add(new Chunk(institution.postal_code,fontTexto));
        linea5.add(new Chunk("Localidad:",fontLabel));
        linea5.add(new Chunk(institution.city,fontTexto));
        linea5.add(new Chunk("Provincia:",fontLabel));
        linea5.add(new Chunk(institution.province,fontTexto));

        Paragraph linea6 = new Paragraph();
        linea6.add(new Chunk("Teléfono:",fontLabel));
        linea6.add(new Chunk(institution.phone,fontTexto));
        linea6.add(new Chunk("Cod. Establecimiento AFIP:",fontLabel));
        linea6.add(new Chunk(institution.afip_cod,fontTexto));

        Paragraph linea7 = new Paragraph();
        linea7.add(new Chunk("Persona contactada:",fontLabel));
        linea7.add(new Chunk(institution.contact,fontTexto));
        linea7.add(new Chunk("Email:",fontLabel));
        linea7.add(new Chunk(institution.email,fontTexto));

        documento.add(lineaSubtitulo);
        documento.add(linea1);
        documento.add(linea2);
        documento.add(linea3);
        documento.add(linea4);
        documento.add(linea5);
        documento.add(linea6);
        documento.add(linea7);
    }



}
