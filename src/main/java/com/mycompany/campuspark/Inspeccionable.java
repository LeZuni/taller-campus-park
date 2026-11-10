package com.mycompany.campuspark;

import java.util.ArrayList;

public interface Inspeccionable {

    void registrarMantenimiento(Mantenimiento mantenimiento);

    ArrayList<Mantenimiento> getHistorialMantenimiento();
}
