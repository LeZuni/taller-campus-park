package com.mycompany.campuspark;

import java.util.ArrayList;

/** Interfaz para el mantenimiento */
public interface Inspeccionable {

    void registrarMantenimiento(Mantenimiento mantenimiento);

    ArrayList<Mantenimiento> getHistorialMantenimiento();
}
