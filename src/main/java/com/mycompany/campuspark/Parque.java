package com.mycompany.campuspark;

import java.util.ArrayList;

public class Parque {

    private String nombre;
    private ArrayList<Visitante> visitantes;
    private ArrayList<Empleado> empleados;
    private ArrayList<Atraccion> atracciones;
    private double recaudoTotal;
    private int siguienteIdPersona;
    private int siguienteIdAtraccion;

    public Parque(String nombre) {
        this.nombre = nombre;
        this.visitantes = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.atracciones = new ArrayList<>();
        this.recaudoTotal = 0;
        this.siguienteIdPersona = 1;
        this.siguienteIdAtraccion = 1;
    }


    public Visitante registrarVisitante(String nombre, int edad, int alturaCm, boolean sabeNadar) {
        Visitante v = new Visitante(siguienteIdPersona, nombre, edad, alturaCm, sabeNadar);
        siguienteIdPersona++;
        visitantes.add(v);
        return v;
    }

    public Empleado registrarEmpleado(String nombre, int edad, String cargo) {
        Empleado e = new Empleado(siguienteIdPersona, nombre, edad, cargo);
        siguienteIdPersona++;
        empleados.add(e);
        return e;
    }

    public Atraccion registrarAtraccion(Atraccion a) {
        atracciones.add(a);
        return a;
    }

    public int nuevoIdAtraccion() {
        int id = siguienteIdAtraccion;
        siguienteIdAtraccion++;
        return id;
    }


    public Visitante buscarVisitante(int id) {
        for (Visitante v : visitantes) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    public Empleado buscarEmpleado(int id) {
        for (Empleado e : empleados) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public Atraccion buscarAtraccion(int id) {
        for (Atraccion a : atracciones) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }


    public void venderTicket(Visitante visitante, TipoTicket tipo) {
        visitante.setTicket(tipo);
    }

    public void encolarVisitante(Visitante visitante, Atraccion atraccion) {
        if (!visitante.tieneTicket()) {
            throw new IllegalStateException("El visitante no tiene ticket. Primero debe comprarlo.");
        }
        if (!atraccion.estaOperativa()) {
            throw new IllegalStateException("La atracción no está operativa (estado: "
                    + atraccion.getEstado() + ").");
        }
        if (!atraccion.puedeIngresar(visitante)) {
            throw new IllegalStateException("El visitante no cumple la restricción: "
                    + atraccion.descripcionRestriccion() + ".");
        }
        if (atraccion.estaEnCola(visitante)) {
            throw new IllegalStateException("El visitante ya está en la cola de esta atracción.");
        }
        atraccion.encolar(visitante);
    }

    public double operarAtraccion(Atraccion atraccion) {
        if (!atraccion.estaOperativa()) {
            throw new IllegalStateException("La atracción no está operativa (estado: "
                    + atraccion.getEstado() + ").");
        }
        if (atraccion.tamanoCola() == 0) {
            throw new IllegalStateException("No hay nadie en la cola de esta atracción.");
        }

        double recaudoTurno = 0;
        int subieron = 0;
        while (subieron < atraccion.getCapacidadPorTurno() && atraccion.tamanoCola() > 0) {
            Visitante v = atraccion.desencolar();
            double costo = atraccion.calcularCosto(v.getTicket());
            v.registrarGasto(costo);
            recaudoTurno = recaudoTurno + costo;
            subieron++;
        }
        recaudoTotal = recaudoTotal + recaudoTurno;
        return recaudoTurno;
    }

    public void registrarMantenimiento(Atraccion atraccion, String fecha, Empleado responsable, String notas) {
        Mantenimiento m = new Mantenimiento(fecha, responsable, notas);
        atraccion.registrarMantenimiento(m);
    }


    public String reporteColas() {
        if (atracciones.isEmpty()) {
            return "No hay atracciones registradas.";
        }
        String texto = "=== COLAS POR ATRACCIÓN ===\n";
        for (Atraccion a : atracciones) {
            texto += "\n" + a.getNombre() + " (" + a.getEstado() + ") - en cola: " + a.tamanoCola();
            int posicion = 1;
            for (Visitante v : a.getCola()) {
                texto += "\n    " + posicion + ". " + v.getNombre() + " (" + v.getTicket() + ")";
                posicion++;
            }
        }
        return texto;
    }

    public String reporteRecaudo() {
        return "Recaudo total de " + nombre + ": $" + String.format("%.2f", recaudoTotal);
    }

    public String reporteGastos() {
        if (visitantes.isEmpty()) {
            return "No hay visitantes registrados.";
        }
        String texto = "=== GASTO POR VISITANTE ===\n";
        double suma = 0;
        for (Visitante v : visitantes) {
            texto += "\n" + v.getNombre() + " - " + v.getAtraccionesUsadas() + " atracción(es): Q"
                    + String.format("%.2f", v.getTotalGastado());
            suma = suma + v.getTotalGastado();
        }
        texto += "\n\nTOTAL GASTADO: Q" + String.format("%.2f", suma);
        return texto;
    }

    public String listarVisitantes() {
        if (visitantes.isEmpty()) {
            return "(no hay visitantes)";
        }
        String texto = "";
        for (Visitante v : visitantes) {
            texto += v.descripcion() + "\n";
        }
        return texto;
    }

    public String listarEmpleados() {
        if (empleados.isEmpty()) {
            return "(no hay empleados)";
        }
        String texto = "";
        for (Empleado e : empleados) {
            texto += e.descripcion() + "\n";
        }
        return texto;
    }

    public String listarAtracciones() {
        if (atracciones.isEmpty()) {
            return "(no hay atracciones)";
        }
        String texto = "";
        for (Atraccion a : atracciones) {
            texto += a.toString() + "\n";
        }
        return texto;
    }

    public String historialMantenimiento(Atraccion atraccion) {
        if (atraccion.getHistorialMantenimiento().isEmpty()) {
            return "Esta atracción no tiene mantenimientos registrados.";
        }
        String texto = "Mantenimientos de " + atraccion.getNombre() + ":\n";
        for (Mantenimiento m : atraccion.getHistorialMantenimiento()) {
            texto += "- " + m + "\n";
        }
        return texto;
    }


    public String getNombre() {
        return nombre;
    }

    public double getRecaudoTotal() {
        return recaudoTotal;
    }

    public ArrayList<Visitante> getVisitantes() {
        return visitantes;
    }

    public ArrayList<Empleado> getEmpleados() {
        return empleados;
    }

    public ArrayList<Atraccion> getAtracciones() {
        return atracciones;
    }
}
