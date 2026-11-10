package com.mycompany.campuspark;

public class Empleado extends Persona {

    private String cargo;

    public Empleado(int id, String nombre, int edad, String cargo) {
        super(id, nombre, edad);
        setCargo(cargo);
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new IllegalArgumentException("El cargo no puede estar vacío.");
        }
        this.cargo = cargo.trim();
    }

    @Override
    public String descripcion() {
        return "Empleado #" + getId() + " - " + getNombre() + " (" + getEdad() + " años, cargo: " + cargo + ")";
    }
}
