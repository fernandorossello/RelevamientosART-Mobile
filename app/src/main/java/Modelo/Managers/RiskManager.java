package Modelo.Managers;

import java.util.ArrayList;
import java.util.List;

import Modelo.Risk;

public class RiskManager {

    public ArrayList<Risk> obtenerRiesgos(){
        ArrayList<Risk> riesgos = new ArrayList<>();

        riesgos.add(new Risk(){{
            code="00001";
            description="Radiación";
        }});

        riesgos.add(new Risk(){{
            code="00002";
            description="Alturas";
        }});

        riesgos.add(new Risk(){{
            code="00003";
            description="Veneno";
        }});

        riesgos.add(new Risk(){{
            code="00004";
            description="Animales";
        }});

        return riesgos;
    }
}
