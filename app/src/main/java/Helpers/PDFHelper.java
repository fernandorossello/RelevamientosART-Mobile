package Helpers;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.telephony.CellIdentityCdma;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;

import Modelo.Attendee;
import Modelo.CAPResult;
import Modelo.Enums.EnumAnswer;
import Modelo.Enums.EnumTareas;
import Modelo.Institution;
import Modelo.Noise;
import Modelo.Question;
import Modelo.RARResult;
import Modelo.RGRLResult;
import Modelo.Result;
import Modelo.Task;
import Modelo.Visit;
import Modelo.WorkingMan;

public class PDFHelper {

    private final Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC);
    private final Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLDITALIC);
    private final Font fontLabel = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
    private final Font fontTexto = FontFactory.getFont(FontFactory.HELVETICA, 11);

    //Se utiliza para generar los PDF de cada tarea
    public void crearPDF(Result result,Task tarea) throws IOException, DocumentException {
        Document document;
        if(tarea.type == EnumTareas.RAR.id) {
            document = new Document(PageSize.A4.rotate());
        } else {
            document = new Document(PageSize.A4);
        }

        File archivo = crearArchivo(tarea.getTypeShortName()+ "_"+tarea.visit.institution.name);
        FileOutputStream output = new FileOutputStream(archivo);

        PdfWriter writer = PdfWriter.getInstance(document, output);

        document.open();

        Paragraph titulo = new Paragraph(tarea.getTypeShortName(),fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        generarCabeceraVisita(tarea.visit, document);

        if(tarea.type == EnumTareas.RAR.id){
            contenidoRAR(document,(RARResult) result);
            writer.setPageEvent(new FooterRAR());
        } else if (tarea.type == EnumTareas.RGRL.id){
            contenidoRGRL(document,(RGRLResult) result);
        } else if (tarea.type == EnumTareas.CAPACITACION.id) {
            contenidoCapacitacion(document,(CAPResult)result);
            writer.setPageEvent(new FooterCapacitacion());
        }

        document.close();
    }

    //Se utiliza para generar el PDF de la constancia de visita
    public void crearPDF(Visit visit) throws IOException, DocumentException {

        File archivo = crearArchivo("Constancia_visita" + "_"+ visit.institution.name);
        FileOutputStream output = new FileOutputStream(archivo);

        Document document = new Document(PageSize.A4);

        PdfWriter writer = PdfWriter.getInstance(document, output);
        writer.setPageEvent(new FooterFirmas());

        document.open();

        Paragraph titulo = new Paragraph("CONSTANCIA DE VISITA",fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        generarCabeceraVisita(visit, document);

        document.add(new Paragraph("Detalle anexo de observaciones",fontSubtitulo));
        document.add(new Paragraph("En la fecha se visita la empresa de referencia para brindar asistencia y asesoramiento técnico sobre legislación vigente en Higiene y Seguridad en el Trabajo, Ley de Riesgos del Trabajo - Ley 24557 -, como así también respecto de agentes de riesgo y la confección del Relevamiento de Agentes de Riesgos según el DEC PEN N°: 658/96 - Dispo. G P y C SRT N° 05/05.",fontTexto));
        document.add(new Paragraph("Se asesora en materia de capacitación y se hace entrega del material",fontTexto));
        document.add(new Paragraph(visit.visit_record.observations,fontTexto));
        document.add(new Paragraph("Actividades realizadas:",fontSubtitulo));


        List tareasRealizadas = new List(List.UNORDERED);
        for (Task tarea:visit.tasks) {
            ListItem item = new ListItem(tarea.getTypeName());
            item.setAlignment(Element.ALIGN_LEFT);
            tareasRealizadas.add(item);
        }
        document.add(tareasRealizadas);

        if(!visit.noises.isEmpty()) {
            document.add(new Paragraph("Mediciones de ruido",fontSubtitulo));
            List medicionesDeRuido = new List(List.UNORDERED);
            for (Noise ruido : visit.noises) {
                ListItem item = new ListItem(formatearDecibeles(ruido.decibels) +"db (" + ruido.description + ")");
                item.setAlignment(Element.ALIGN_LEFT);
                medicionesDeRuido.add(item);
            }
            document.add(medicionesDeRuido);
        }

        document.close();
    }

    private String formatearFecha(Date date){
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        return sdf.format(date);
    }

    private String formatearDecibeles(Double decibeles){
        return new Formatter().format("%03.1f", decibeles).toString();
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

        PdfPTable linea1 = new PdfPTable(3);
        linea1.setWidthPercentage(100);
        linea1.addCell(getPdfPCell(getPhraseItem("Razón social:",institution.name)));
        linea1.addCell(getPdfPCell(getPhraseItem("Contrato N°:",institution.contract)));
        linea1.addCell(getPdfPCell(getPhraseItem("Fecha visita:",formatearFecha(visit.to_visit_on))));


        PdfPTable linea2 = new PdfPTable(2);
        linea2.setWidthPercentage(100);
        linea2.addCell(getPdfPCell(getPhraseItem("Domicilio:",institution.address)));
        linea2.addCell(getPdfPCell(getPhraseItem("C.U.I.T.:",institution.cuit)));

        PdfPTable linea3 = new PdfPTable(2);
        linea3.setWidthPercentage(100);
        linea3.addCell(getPdfPCell(getPhraseItem("Actividad:",institution.activity)));
        linea3.addCell(getPdfPCell(getPhraseItem("CIIU:",institution.ciiu)));


        float[] columnWidths = {5, 3};
        PdfPTable linea4 = new PdfPTable(columnWidths);
        linea4.setWidthPercentage(100);
        linea4.addCell(getPdfPCell(getPhraseItem("N° y Nombre del establecimiento:",institution.number)));
        linea4.addCell(getPdfPCell(getPhraseItem("N° trab. Estab.:",Integer.toString(institution.workers_count))));

        PdfPTable  linea5 = new PdfPTable(3);
        linea5.setWidthPercentage(100);
        linea5.addCell(getPdfPCell(getPhraseItem("Código postal:",institution.postal_code)));
        linea5.addCell(getPdfPCell(getPhraseItem("Localidad:",institution.city)));
        linea5.addCell(getPdfPCell(getPhraseItem("Provincia:",institution.province)));

        PdfPTable linea6 = new PdfPTable(2);
        linea6.setWidthPercentage(100);
        linea6.addCell(getPdfPCell(getPhraseItem("Teléfono:",institution.phone)));
        linea6.addCell(getPdfPCell(getPhraseItem("Cod. Establecimiento AFIP:",institution.afip_cod)));

        PdfPTable linea7 = new PdfPTable(2);
        linea7.setWidthPercentage(100);
        linea7.addCell(getPdfPCell(getPhraseItem("Persona contactada:",institution.contact)));
        linea7.addCell(getPdfPCell(getPhraseItem("Email:",institution.email)));

        documento.add(lineaSubtitulo);
        documento.add(linea1);
        documento.add(linea2);
        documento.add(linea3);
        documento.add(linea4);
        documento.add(linea5);
        documento.add(linea6);
        documento.add(linea7);
    }

    @NonNull
    private PdfPCell getPdfPCell(Phrase item1) {
        PdfPCell celda1 = new PdfPCell(item1);
        celda1.setBorder(PdfPCell.NO_BORDER);
        return celda1;
    }

    private Phrase getPhraseItem(String label, String text){
        Phrase item = new Phrase();
        item.add(new Chunk(label,fontLabel));
        item.add(new Chunk(text,fontTexto));
        return item;
    }

    class FooterFirmas extends PdfPageEventHelper {

        public void onEndPage(PdfWriter writer, Document document) {
            footer(document).writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());

        }

        private PdfPTable footer(Document document) {
            PdfPTable firmas = new PdfPTable(2);
            firmas.setTotalWidth(document.right() - document.left());

            PdfPCell celda1 = new PdfPCell(new Phrase("Firma representante empleador",fontTexto));
            PdfPCell celda2 = new PdfPCell(new Phrase("Firma representante ART",fontTexto));

            celda1.setBorder(PdfPCell.NO_BORDER);
            celda2.setBorder(PdfPCell.NO_BORDER);
            celda1.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda2.setHorizontalAlignment(Element.ALIGN_CENTER);

            firmas.addCell(celda1);
            firmas.addCell(celda2);

            return firmas;
        }
    }

    private void contenidoRAR(Document document, RARResult result) throws DocumentException{

        Paragraph titulo = new Paragraph("Trabajadores expuestos",fontSubtitulo);
        titulo.setSpacingAfter(10f);
        document.add(titulo);

        float[] columnWidths = {2, 2, 2, 2, 2, 2, 2, 5};
        PdfPTable tabla = new PdfPTable(columnWidths);
        tabla.setWidthPercentage(100);
        tabla.addCell("Nombre");
        tabla.addCell("Apellido");
        tabla.addCell("C.U.I.L.");
        tabla.addCell("Fecha de ingreso");
        tabla.addCell("Fecha inicio exposición");
        tabla.addCell("Fecha fin exposición");
        tabla.addCell("Sector");
        tabla.addCell("Códigos de agentes de riesgos");

        for (WorkingMan trabajador: result.working_men) {
            tabla.addCell(trabajador.name);
            tabla.addCell(trabajador.last_name);
            tabla.addCell(trabajador.cuil);
            tabla.addCell(formatearFecha(trabajador.checked_in_on));
            tabla.addCell(formatearFecha(trabajador.exposed_from_at));
            tabla.addCell(trabajador.exposed_until_at != null ? formatearFecha(trabajador.exposed_until_at) : "");
            tabla.addCell(trabajador.sector);
            tabla.addCell(trabajador.obtenerCodigosDeRiesgos());
        }

        document.add(tabla);
    }

    class FooterRAR extends PdfPageEventHelper {

        public void onEndPage(PdfWriter writer, Document document) {
            footer(document).writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());
        }

        private PdfPTable footer(Document document) {
            PdfPTable firmas = new PdfPTable(3);
            firmas.setTotalWidth(document.right() - document.left());

            PdfPCell celda1 = new PdfPCell(new Phrase("Firma representante de la empresa",fontTexto));
            PdfPCell celda2 = new PdfPCell(new Phrase("Firma representante Higiene y seguridad",fontTexto));
            PdfPCell celda3 = new PdfPCell(new Phrase("Firma representante ART",fontTexto));

            celda1.setBorder(PdfPCell.NO_BORDER);
            celda2.setBorder(PdfPCell.NO_BORDER);
            celda3.setBorder(PdfPCell.NO_BORDER);
            celda1.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda2.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda3.setHorizontalAlignment(Element.ALIGN_CENTER);

            firmas.addCell(celda1);
            firmas.addCell(celda2);
            firmas.addCell(celda3);

            return firmas;
        }
    }

    private void contenidoCapacitacion(Document document, CAPResult result) throws DocumentException {

        Paragraph datosGenerales = new Paragraph("Datos generales",fontSubtitulo);
        datosGenerales.setSpacingAfter(10f);
        document.add(datosGenerales);

        Paragraph curso = new Paragraph();
        curso.add(new Chunk("Curso:",fontLabel));
        curso.add(result.course_name);
        document.add(curso);

        Paragraph contenidos = new Paragraph();
        contenidos.add(new Chunk("Contenidos:",fontLabel));
        contenidos.add(result.contents);
        document.add(contenidos);

        Paragraph metodologia = new Paragraph();
        metodologia.add(new Chunk("Metodología:",fontLabel));
        metodologia.add(result.methodology);
        document.add(metodologia);

        Paragraph titulo = new Paragraph("Asistentes",fontSubtitulo);
        titulo.setSpacingAfter(10f);
        document.add(titulo);

        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.addCell("Apellido y nombre");
        tabla.addCell("C.U.I.L.");
        tabla.addCell("Sector");
        tabla.addCell("Firma");

        for (Attendee asistente :result.attendees) {
            tabla.addCell(asistente.nombreCompleto());
            tabla.addCell(asistente.cuil);
            tabla.addCell(asistente.sector);
            tabla.addCell("");
        }

        document.add(tabla);
    }

    class FooterCapacitacion extends PdfPageEventHelper {

        public void onEndPage(PdfWriter writer, Document document) {
            footer(document).writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());
        }

        private PdfPTable footer(Document document) {
            PdfPTable firmas = new PdfPTable(2);
            firmas.setTotalWidth(document.right() - document.left());

            PdfPCell celda1 = new PdfPCell(new Phrase("Firma representante de la empresa",fontTexto));
            PdfPCell celda2 = new PdfPCell(new Phrase("Firma instructor ART",fontTexto));

            celda1.setBorder(PdfPCell.NO_BORDER);
            celda2.setBorder(PdfPCell.NO_BORDER);
            celda1.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda2.setHorizontalAlignment(Element.ALIGN_CENTER);

            firmas.addCell(celda1);
            firmas.addCell(celda2);

            return firmas;
        }
    }

    private void contenidoRGRL(Document document, RGRLResult result) throws DocumentException {
        Paragraph datosGenerales = new Paragraph("Cuestionario",fontSubtitulo);
        datosGenerales.setSpacingAfter(10f);
        document.add(datosGenerales);

        float[] columnWidths = {0.5f,5,0.5f,0.5f,0.5f};
        PdfPTable tabla = new PdfPTable(columnWidths);
        tabla.setWidthPercentage(100);
        tabla.addCell("N°");
        tabla.addCell("Condiciones a cumplir");
        tabla.addCell("SI");
        tabla.addCell("NO");
        tabla.addCell("N/A");

        String categoria = "";

        for (Question pregunta: result.questions)
        {
            if(!pregunta.category.equals(categoria)){
                categoria = pregunta.category;
                insertarLineaCategoria(tabla,categoria);
            }

            tabla.addCell(String.valueOf(pregunta.id));
            tabla.addCell(pregunta.description);

            if(pregunta.answer_code == EnumAnswer.SI.id) {
                tabla.addCell("X");
            } else {
                tabla.addCell("");
            }

            if(pregunta.answer_code == EnumAnswer.NO.id) {
                tabla.addCell("X");
            } else {
                tabla.addCell("");
            }

            if(pregunta.answer_code == EnumAnswer.NOAPLICA.id) {
                tabla.addCell("X");
            } else {
                tabla.addCell("");
            }
        }

        document.add(tabla);

    }

    private void insertarLineaCategoria(PdfPTable tabla,String categoria) {
        Phrase phrase = new Phrase(categoria);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setColspan(5);
        cell.setBackgroundColor(BaseColor.GRAY);
        tabla.addCell(cell);
    }
}
