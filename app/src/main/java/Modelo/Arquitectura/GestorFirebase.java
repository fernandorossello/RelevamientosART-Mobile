package Modelo.Arquitectura;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.SQLException;

import Modelo.FirebaseFile;

public abstract class GestorFirebase {

    private StorageReference mStorageRef;

    public GestorFirebase(){
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void subirArchivo(final FirebaseFile archivo) {
        Uri uri = Uri.parse(archivo.url_file);
        StorageReference imgRef = mStorageRef.child(getNombreCarpetaRemota() + archivo.obtenerNombreArchivo());
        imgRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        archivo.url_cloud = taskSnapshot.getDownloadUrl().toString();
                        try {
                            persistirEntidad(archivo);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                })
        ;
    }

    public abstract void persistirEntidad(FirebaseFile archivo) throws SQLException;

    public abstract String getNombreCarpetaRemota();
}
