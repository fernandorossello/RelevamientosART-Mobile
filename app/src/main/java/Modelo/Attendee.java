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
        ValidacionHelper.NullOrEmpty(this.lastName,"apellido");
        ValidacionHelper.CantidadCaracteres(this.cuil,11,"cuil");

    }

    public void fill(Attendee attendee) {
        this.cuil = attendee.cuil;
        this.name = attendee.name;
        this.lastName = attendee.lastName;
    }
}
