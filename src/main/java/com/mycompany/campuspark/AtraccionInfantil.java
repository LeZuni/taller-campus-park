package com.mycompany.campuspark;

public class AtraccionInfantil extends Atraccion {

    private int edadMaxima;

    public AtraccionInfantil(int id, String nombre, int capacidadPorTurno, double precioBase,
            NivelRiesgo riesgo, int edadMaxima) {
        super(id, nombre, capacidadPorTurno, precioBase, riesgo);
        setEdadMaxima(edadMaxima);
    }

    public int getEdadMaxima() {
        return edadMaxima;
    }

    public void setEdadMaxima(int edadMaxima) {
        if (edadMaxima < 1 || edadMaxima > 17) {
            throw new IllegalArgumentException("La edad máxima debe estar entre 1 y 17.");
        }
        this.edadMaxima = edadMaxima;
    }

    @Override
    public boolean puedeIngresar(Visitante visitante) {
        return visitante.getEdad() <= edadMaxima;
    }

    @Override
    public String descripcionRestriccion() {
        return "edad máxima " + edadMaxima + " años";
    }

    @Override
    public String getTipo() {
        return "Infantil";
    }

    @Override
    public double calcularCosto(TipoTicket tipoTicket) {
        return super.calcularCosto(tipoTicket) * 0.90;
    }
}
