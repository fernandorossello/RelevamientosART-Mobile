package Modelo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;

public abstract class FirebaseFile {

    @DatabaseField
    public String url_file;

    @Expose
    @DatabaseField
    public String url_cloud;

    public String obtenerNombreArchivo(){
        String[] carpetas = this.url_file.split("/");

        return carpetas[carpetas.length - 1];
    }

    public boolean enviado(){
        return !(url_cloud == null || url_cloud.isEmpty());
    }
}
