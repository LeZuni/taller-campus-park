package com.mycompany.campuspark;

import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.time.LocalDate;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class CampusPark {

    private static Parque parque = new Parque("CampusPark");
    private static ImageIcon logo = null;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 15));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 15));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 15));

        URL urlLogo = CampusPark.class.getResource("/logo.png");
        ImageIcon original = (urlLogo != null) ? new ImageIcon(urlLogo) : null;
        if (original != null && original.getIconWidth() > 0 && original.getIconHeight() > 0) {

            int altoDeseado = 130;
            int anchoDeseado = original.getIconWidth() * altoDeseado / original.getIconHeight();
            Image reducida = original.getImage().getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);
            logo = new ImageIcon(reducida);
        }

        cargarDatosDeEjemplo();

        String[] opciones = {
            "Registrar visitante",
            "Registrar empleado",
            "Registrar atracción",
            "Vender ticket",
            "Encolar visitante en una atracción",
            "Operar atracción (un turno)",
            "Registrar mantenimiento",
            "Cambiar estado de una atracción",
            "Reportes (colas y recaudo total)",
            "Ver listados",
            "Total gastado por visitante",
            "Salir"
        };

        boolean seguir = true;
        while (seguir) {
            String textoMenu = "<html><div style='width:280px'>"
                    + "<span style='font-size:20px; color:#1565C0'><b>CampusPark</b></span><br>"
                    + "<span style='color:gray'>Sistema de gestión del parque de atracciones</span><br><br>"
                    + "¿Qué quieres hacer?<br>"
                    + "<span style='color:#2E7D32'>Recaudo actual: <b>Q" + String.format("%.2f", parque.getRecaudoTotal())
                    + "</b></span></div></html>";
            Object elegida = JOptionPane.showInputDialog(null, textoMenu,
                    "CampusPark - Menú principal", JOptionPane.QUESTION_MESSAGE,
                    logo, opciones, opciones[0]);

            if (elegida == null || elegida.equals("Salir")) {
                seguir = false;
            } else {
                int opcion = 0;
                for (int i = 0; i < opciones.length; i++) {
                    if (opciones[i].equals(elegida)) {
                        opcion = i + 1;
                    }
                }
                ejecutarOpcion(opcion);
            }
        }
        JOptionPane.showMessageDialog(null, "¡Hasta luego!", "CampusPark", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void ejecutarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1:
                    registrarVisitante();
                    break;
                case 2:
                    registrarEmpleado();
                    break;
                case 3:
                    registrarAtraccion();
                    break;
                case 4:
                    venderTicket();
                    break;
                case 5:
                    encolarVisitante();
                    break;
                case 6:
                    operarAtraccion();
                    break;
                case 7:
                    registrarMantenimiento();
                    break;
                case 8:
                    cambiarEstado();
                    break;
                case 9:
                    mostrarReportes();
                    break;
                case 10:
                    verListados();
                    break;
                case 11:
                    mensaje(parque.reporteGastos());
                    break;
                case 0:
                    break;
                default:
                    mensaje("Opción no válida.");
            }
        } catch (CancelarException e) {
        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "No se pudo", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private static void registrarVisitante() {
        String nombre = leerTexto("Nombre del visitante:");
        int edad = leerEntero("Edad:");
        int altura = leerEntero("Altura en cm:");
        boolean sabeNadar = leerSiNo("¿Sabe nadar?");

        Visitante v = parque.registrarVisitante(nombre, edad, altura, sabeNadar);
        mensaje("Visitante registrado:\n" + v.descripcion());
    }

    private static void registrarEmpleado() {
        String nombre = leerTexto("Nombre del empleado:");
        int edad = leerEntero("Edad:");
        String cargo = leerTexto("Cargo:");

        Empleado e = parque.registrarEmpleado(nombre, edad, cargo);
        mensaje("Empleado registrado:\n" + e.descripcion());
    }

    private static void registrarAtraccion() {
        String[] tipos = {"Mecánica", "Acuática", "Infantil"};
        int tipo = JOptionPane.showOptionDialog(null, "¿Qué tipo de atracción?", "Nueva atracción",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        if (tipo == JOptionPane.CLOSED_OPTION) {
            return;
        }

        String nombre = leerTexto("Nombre de la atracción:");
        int capacidad = leerEntero("Capacidad por turno:");
        double precio = leerDecimal("Precio base:");
        NivelRiesgo riesgo = leerRiesgo();

        Atraccion nueva;
        if (tipo == 0) {
            int alturaMinima = leerEntero("Altura mínima en cm:");
            nueva = new AtraccionMecanica(parque.nuevoIdAtraccion(), nombre, capacidad, precio, riesgo, alturaMinima);
        } else if (tipo == 1) {
            boolean requiere = leerSiNo("¿Requiere saber nadar?");
            nueva = new AtraccionAcuatica(parque.nuevoIdAtraccion(), nombre, capacidad, precio, riesgo, requiere);
        } else {
            int edadMaxima = leerEntero("Edad máxima:");
            nueva = new AtraccionInfantil(parque.nuevoIdAtraccion(), nombre, capacidad, precio, riesgo, edadMaxima);
        }
        parque.registrarAtraccion(nueva);
        mensaje("Atracción registrada:\n" + nueva);
    }

    private static void venderTicket() {
        Visitante v = elegirVisitante();

        String[] tipos = {"DIARIO", "VIP"};
        int eleccion = JOptionPane.showOptionDialog(null, "Tipo de ticket para " + v.getNombre()
                + "\n(VIP tiene 20% de descuento en cada atracción)", "Vender ticket",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        if (eleccion == JOptionPane.CLOSED_OPTION) {
            return;
        }
        TipoTicket tipo = TipoTicket.values()[eleccion];

        parque.venderTicket(v, tipo);
        mensaje("Ticket " + tipo + " vendido a " + v.getNombre() + ".");
    }

    private static void encolarVisitante() {
        Visitante v = elegirVisitante();
        Atraccion a = elegirAtraccion();

        parque.encolarVisitante(v, a);
        mensaje(v.getNombre() + " quedó en la cola de " + a.getNombre()
                + " (posición " + a.tamanoCola() + ").");
    }

    private static void operarAtraccion() {
        Atraccion a = elegirAtraccion();

        int enCola = a.tamanoCola();
        double recaudo = parque.operarAtraccion(a);
        int subieron = Math.min(enCola, a.getCapacidadPorTurno());
        mensaje("Turno de " + a.getNombre() + " terminado.\n"
                + "Subieron: " + subieron + " visitante(s)\n"
                + "Recaudo del turno: $" + String.format("%.2f", recaudo) + "\n"
                + "Quedan en cola: " + a.tamanoCola() + "\n\n"
                + parque.reporteRecaudo());
    }

    private static void registrarMantenimiento() {
        if (parque.getEmpleados().isEmpty()) {
            throw new IllegalStateException("Primero registra al menos un empleado (opción 2).");
        }
        Atraccion a = elegirAtraccion();
        Empleado responsable = elegirEmpleado();
        String fecha = leerTextoConDefecto("Fecha del mantenimiento:", LocalDate.now().toString());
        String notas = leerTexto("Notas:");

        parque.registrarMantenimiento(a, fecha, responsable, notas);
        mensaje("Mantenimiento registrado. " + a.getNombre() + " ahora está " + a.getEstado() + ".");
    }

    private static void cambiarEstado() {
        Atraccion a = elegirAtraccion();

        String[] opciones = {"OPERATIVA", "EN_MANTENIMIENTO", "FUERA_DE_SERVICIO"};
        int eleccion = JOptionPane.showOptionDialog(null, "Estado actual de " + a.getNombre() + ": "
                + a.getEstado() + "\nNuevo estado:", "Cambiar estado",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (eleccion == JOptionPane.CLOSED_OPTION) {
            return;
        }
        a.setEstado(EstadoAtraccion.values()[eleccion]);
        mensaje(a.getNombre() + " ahora está " + a.getEstado() + ".");
    }

    private static void mostrarReportes() {
        mensaje(parque.reporteColas() + "\n\n" + parque.reporteRecaudo());
    }

    private static void verListados() {
        String[] opciones = {"Visitantes", "Empleados", "Atracciones", "Mantenimientos"};
        int eleccion = JOptionPane.showOptionDialog(null, "¿Qué quieres ver?", "Listados",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (eleccion == 0) {
            mensaje(parque.listarVisitantes());
        } else if (eleccion == 1) {
            mensaje(parque.listarEmpleados());
        } else if (eleccion == 2) {
            mensaje(parque.listarAtracciones());
        } else if (eleccion == 3) {
            Atraccion a = elegirAtraccion();
            mensaje(parque.historialMantenimiento(a));
        }
    }


    private static Visitante elegirVisitante() {
        if (parque.getVisitantes().isEmpty()) {
            throw new IllegalStateException("No hay visitantes registrados (opción 1).");
        }
        Object elegido = JOptionPane.showInputDialog(null, "Elige el visitante:", "Visitantes",
                JOptionPane.QUESTION_MESSAGE, logo, parque.getVisitantes().toArray(), null);
        if (elegido == null) {
            throw new CancelarException();
        }
        return (Visitante) elegido;
    }

    private static Empleado elegirEmpleado() {
        Object elegido = JOptionPane.showInputDialog(null, "Elige el empleado responsable:", "Empleados",
                JOptionPane.QUESTION_MESSAGE, logo, parque.getEmpleados().toArray(), null);
        if (elegido == null) {
            throw new CancelarException();
        }
        return (Empleado) elegido;
    }

    private static Atraccion elegirAtraccion() {
        if (parque.getAtracciones().isEmpty()) {
            throw new IllegalStateException("No hay atracciones registradas (opción 3).");
        }
        Object elegida = JOptionPane.showInputDialog(null, "Elige la atracción:", "Atracciones",
                JOptionPane.QUESTION_MESSAGE, logo, parque.getAtracciones().toArray(), null);
        if (elegida == null) {
            throw new CancelarException();
        }
        return (Atraccion) elegida;
    }

    private static NivelRiesgo leerRiesgo() {
        String[] opciones = {"BAJO", "MEDIO", "ALTO"};
        int eleccion = JOptionPane.showOptionDialog(null, "Nivel de riesgo:", "Riesgo",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (eleccion == JOptionPane.CLOSED_OPTION) {
            throw new CancelarException();
        }
        return NivelRiesgo.values()[eleccion];
    }


    private static class CancelarException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private static String leerTexto(String pregunta) {
        String r = JOptionPane.showInputDialog(null, pregunta);
        if (r == null) {
            throw new CancelarException();
        }
        return r;
    }

    private static String leerTextoConDefecto(String pregunta, String defecto) {
        String r = JOptionPane.showInputDialog(null, pregunta, defecto);
        if (r == null) {
            throw new CancelarException();
        }
        return r;
    }

    private static int leerEntero(String pregunta) {
        String r = leerTexto(pregunta);
        try {
            return Integer.parseInt(r.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Debes escribir un número entero.");
        }
    }

    private static double leerDecimal(String pregunta) {
        String r = leerTexto(pregunta);
        try {
            return Double.parseDouble(r.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Debes escribir un número (ejemplo: 12.5).");
        }
    }

    private static boolean leerSiNo(String pregunta) {
        int r = JOptionPane.showConfirmDialog(null, pregunta, "Pregunta", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.CLOSED_OPTION) {
            throw new CancelarException();
        }
        return r == JOptionPane.YES_OPTION;
    }

    private static void mensaje(String texto) {
        JOptionPane.showMessageDialog(null, texto, "CampusPark", JOptionPane.INFORMATION_MESSAGE, logo);
    }


    private static void cargarDatosDeEjemplo() {
        parque.registrarEmpleado("Carlos Pérez", 35, "Técnico de mantenimiento");
        parque.registrarEmpleado("Ana Gómez", 28, "Operadora");

        parque.registrarAtraccion(new AtraccionMecanica(parque.nuevoIdAtraccion(),
                "Montaña Rusa", 4, 10.0, NivelRiesgo.ALTO, 120));
        parque.registrarAtraccion(new AtraccionAcuatica(parque.nuevoIdAtraccion(),
                "Río Lento", 3, 8.0, NivelRiesgo.MEDIO, true));
        parque.registrarAtraccion(new AtraccionInfantil(parque.nuevoIdAtraccion(),
                "Carrusel", 5, 5.0, NivelRiesgo.BAJO, 10));
    }
}
