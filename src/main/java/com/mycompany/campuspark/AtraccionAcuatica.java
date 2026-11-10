package com.mycompany.campuspark;

/** Atraccion acuatica (saber nadar) */
public class AtraccionAcuatica extends Atraccion {

    private boolean requiereSaberNadar;

    public AtraccionAcuatica(int id, String nombre, int capacidadPorTurno, double precioBase,
            NivelRiesgo riesgo, boolean requiereSaberNadar) {
        super(id, nombre, capacidadPorTurno, precioBase, riesgo);
        this.requiereSaberNadar = requiereSaberNadar;
    }

    public boolean isRequiereSaberNadar() {
        return requiereSaberNadar;
    }

    public void setRequiereSaberNadar(boolean requiereSaberNadar) {
        this.requiereSaberNadar = requiereSaberNadar;
    }

    @Override
    public boolean puedeIngresar(Visitante visitante) {
        if (requiereSaberNadar) {
            return visitante.isSabeNadar();
        }
        return true;
    }

    @Override
    public String descripcionRestriccion() {
        return requiereSaberNadar ? "debe saber nadar" : "sin restricción";
    }

    @Override
    public String getTipo() {
        return "Acuática";
    }

    /** se le suman 2 por la toalla */
    @Override
    public double calcularCosto(TipoTicket tipoTicket) {
        return super.calcularCosto(tipoTicket) + 2.0;
    }
}
