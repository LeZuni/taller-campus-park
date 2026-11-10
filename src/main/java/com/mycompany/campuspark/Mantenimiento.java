package com.mycompany.campuspark;

/** Registro de un mantenimiento */
public class Mantenimiento {

    private String fecha;
    private Empleado responsable;
    private String notas;

    public Mantenimiento(String fecha, Empleado responsable, String notas) {
        if (fecha == null || fecha.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha no puede estar vacía.");
        }
        if (responsable == null) {
            throw new IllegalArgumentException("Debe haber un responsable.");
        }
        this.fecha = fecha.trim();
        this.responsable = responsable;
        this.notas = (notas == null) ? "" : notas.trim();
    }

    public String getFecha() {
        return fecha;
    }

    public Empleado getResponsable() {
        return responsable;
    }

    public String getNotas() {
        return notas;
    }

    @Override
    public String toString() {
        return fecha + " - " + responsable.getNombre() + " - " + notas;
    }
}
