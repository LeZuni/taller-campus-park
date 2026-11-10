package com.mycompany.campuspark;

public class AtraccionMecanica extends Atraccion {

    private int alturaMinimaCm;

    public AtraccionMecanica(int id, String nombre, int capacidadPorTurno, double precioBase,
            NivelRiesgo riesgo, int alturaMinimaCm) {
        super(id, nombre, capacidadPorTurno, precioBase, riesgo);
        setAlturaMinimaCm(alturaMinimaCm);
    }

    public int getAlturaMinimaCm() {
        return alturaMinimaCm;
    }

    public void setAlturaMinimaCm(int alturaMinimaCm) {
        if (alturaMinimaCm < 0 || alturaMinimaCm > 250) {
            throw new IllegalArgumentException("La altura mínima debe estar entre 0 y 250 cm.");
        }
        this.alturaMinimaCm = alturaMinimaCm;
    }

    @Override
    public boolean puedeIngresar(Visitante visitante) {
        return visitante.getAlturaCm() >= alturaMinimaCm;
    }

    @Override
    public String descripcionRestriccion() {
        return "altura mínima " + alturaMinimaCm + " cm";
    }

    @Override
    public String getTipo() {
        return "Mecánica";
    }

    @Override
    public double calcularCosto(TipoTicket tipoTicket) {
        double costo = super.calcularCosto(tipoTicket);
        if (getRiesgo() == NivelRiesgo.ALTO) {
            costo = costo * 1.15;
        }
        return costo;
    }
}
