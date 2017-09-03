package Modelo.Managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Modelo.WorkingMan;

public class WorkingManManager extends Manager<WorkingMan> {
    @Override
    public void persist(WorkingMan item) throws SQLException {

    }


    public List<WorkingMan> trabajadoresEjemplo(){

        ArrayList<WorkingMan> lista = new ArrayList<>();

        lista.add(new WorkingMan()
        {{
            name = "Fernando";
            lastName = "Rosselló";
            cuil = "34585130";
        }});

        lista.add(new WorkingMan()
        {{
            name = "Tomás";
            lastName = "Ramos";
            cuil = "34898398";
        }});

        return lista;
    }
}
