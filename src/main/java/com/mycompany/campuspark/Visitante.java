package com.mycompany.campuspark;

/** Visitante, hereda de Persona */
public class Visitante extends Persona {

    private int alturaCm;
    private boolean sabeNadar;
    private TipoTicket ticket; // si es null no ha comprado

    public Visitante(int id, String nombre, int edad, int alturaCm, boolean sabeNadar) {
        super(id, nombre, edad);
        setAlturaCm(alturaCm);
        this.sabeNadar = sabeNadar;
    }

    public int getAlturaCm() {
        return alturaCm;
    }

    public void setAlturaCm(int alturaCm) {
        if (alturaCm < 30 || alturaCm > 250) {
            throw new IllegalArgumentException("La altura debe estar entre 30 y 250 cm.");
        }
        this.alturaCm = alturaCm;
    }

    public boolean isSabeNadar() {
        return sabeNadar;
    }

    public void setSabeNadar(boolean sabeNadar) {
        this.sabeNadar = sabeNadar;
    }

    public TipoTicket getTicket() {
        return ticket;
    }

    public void setTicket(TipoTicket ticket) {
        this.ticket = ticket;
    }

    public boolean tieneTicket() {
        return ticket != null;
    }

    @Override
    public String descripcion() {
        return "Visitante #" + getId() + " - " + getNombre() + " (" + getEdad() + " años, "
                + alturaCm + " cm, sabe nadar: " + (sabeNadar ? "sí" : "no")
                + ", ticket: " + (ticket == null ? "ninguno" : ticket) + ")";
    }
}
