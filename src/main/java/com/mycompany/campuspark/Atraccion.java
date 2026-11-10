package com.mycompany.campuspark;

import java.util.ArrayList;

public abstract class Atraccion implements Inspeccionable, CalculableCosto {

    private int id;
    private String nombre;
    private int capacidadPorTurno;
    private double precioBase;
    private EstadoAtraccion estado;
    private NivelRiesgo riesgo;
    private ArrayList<Visitante> cola;
    private ArrayList<Mantenimiento> mantenimientos;

    public Atraccion(int id, String nombre, int capacidadPorTurno, double precioBase, NivelRiesgo riesgo) {
        this.id = id;
        setNombre(nombre);
        setCapacidadPorTurno(capacidadPorTurno);
        setPrecioBase(precioBase);
        setRiesgo(riesgo);
        this.estado = EstadoAtraccion.OPERATIVA;
        this.cola = new ArrayList<>();
        this.mantenimientos = new ArrayList<>();
    }


    public abstract boolean puedeIngresar(Visitante visitante);

    public abstract String descripcionRestriccion();

    public abstract String getTipo();


    @Override
    public double calcularCosto(TipoTicket tipoTicket) {
        double costo = precioBase;
        if (tipoTicket == TipoTicket.VIP) {
            costo = costo * 0.80;
        }
        return costo;
    }


    public boolean estaOperativa() {
        return estado == EstadoAtraccion.OPERATIVA;
    }

    public boolean estaEnCola(Visitante visitante) {
        return cola.contains(visitante);
    }

    public void encolar(Visitante visitante) {
        cola.add(visitante);
    }

    public Visitante desencolar() {
        return cola.remove(0);
    }

    public int tamanoCola() {
        return cola.size();
    }

    public ArrayList<Visitante> getCola() {
        return cola;
    }


    @Override
    public void registrarMantenimiento(Mantenimiento mantenimiento) {
        mantenimientos.add(mantenimiento);
        estado = EstadoAtraccion.EN_MANTENIMIENTO;
    }

    @Override
    public ArrayList<Mantenimiento> getHistorialMantenimiento() {
        return mantenimientos;
    }


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la atracción no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public int getCapacidadPorTurno() {
        return capacidadPorTurno;
    }

    public void setCapacidadPorTurno(int capacidadPorTurno) {
        if (capacidadPorTurno <= 0 || capacidadPorTurno > 200) {
            throw new IllegalArgumentException("La capacidad por turno debe estar entre 1 y 200.");
        }
        this.capacidadPorTurno = capacidadPorTurno;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio base no puede ser negativo.");
        }
        this.precioBase = precioBase;
    }

    public EstadoAtraccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAtraccion estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        this.estado = estado;
    }

    public NivelRiesgo getRiesgo() {
        return riesgo;
    }

    public void setRiesgo(NivelRiesgo riesgo) {
        if (riesgo == null) {
            throw new IllegalArgumentException("El riesgo no puede ser nulo.");
        }
        this.riesgo = riesgo;
    }

    @Override
    public String toString() {
        return "#" + id + " " + nombre + " [" + getTipo() + "] - estado: " + estado
                + ", riesgo: " + riesgo + ", capacidad: " + capacidadPorTurno
                + ", precio base:   Q" + String.format("%.2f", precioBase)
                + ", restricción: " + descripcionRestriccion();
    }
}
