package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import Excepciones.ValidationException;
import Helpers.ValidacionHelper;

@DatabaseTable
public class Attendee extends Employee implements Serializable {

    @DatabaseField(foreign = true)
    public CAPResult result;

    public void Validar() throws ValidationException {

        ValidacionHelper.NullOrEmpty(this.name,"nombre");
        ValidacionHelper.NullOrEmpty(this.last_name,"apellido");
        ValidacionHelper.CantidadCaracteres(this.cuil,11,"cuil");
        ValidacionHelper.NullOrEmpty(this.sector,"sector");

    }

    public void fill(Attendee attendee) {
        this.cuil = attendee.cuil;
        this.name = attendee.name;
        this.last_name = attendee.last_name;
        this.sector = attendee.sector;
    }
}
