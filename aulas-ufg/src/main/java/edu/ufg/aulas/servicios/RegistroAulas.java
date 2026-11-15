package edu.ufg.aulas.servicios;

import edu.ufg.aulas.entidades.Aula;
import edu.ufg.aulas.entidades.EstadoAula;
import edu.ufg.aulas.entidades.TipoAula;
import edu.ufg.aulas.entidades.Usuario;
import edu.ufg.aulas.repositorios.AulaRepositorio;

public class RegistroAulas {
    private final AulaRepositorio aulas = new AulaRepositorio();

    public Registro validar(String nombre, String edificio, Integer planta, String capacidad,
            TipoAula tipo, String responsable, EstadoAula estado) {
        String nombreLimpio = texto(nombre, "el nombre del aula", 80);
        String edificioLimpio = texto(edificio, "el edificio", 100);
        if (planta == null || planta <= 0) {
            throw new IllegalArgumentException("Selecciona una planta mayor que 0.");
        }
        int personas = capacidad(capacidad);
        if (tipo == null) {
            throw new IllegalArgumentException("Selecciona el tipo de aula.");
        }
        String responsableLimpio = texto(responsable, "el responsable", 120);
        if (estado == null) {
            throw new IllegalArgumentException("Selecciona el estado del aula.");
        }
        return new Registro(nombreLimpio, edificioLimpio, planta, personas, tipo, responsableLimpio, estado);
    }

    public long guardar(Registro datos, Usuario usuario) {
        Aula aula = new Aula(datos.nombre(), datos.edificio(), datos.planta(),
                datos.capacidad(), datos.tipo(), datos.responsable(), datos.estado(), usuario);
        return aulas.guardar(aula);
    }

    private String texto(String valor, String campo, int limite) {
        String limpio = valor.strip();
        if (limpio.isEmpty()) {
            throw new IllegalArgumentException("Completa " + campo + ".");
        }
        if (limpio.length() > limite) {
            throw new IllegalArgumentException("Revisa " + campo + ". Máximo " + limite + " caracteres.");
        }
        return limpio;
    }

    private int capacidad(String texto) {
        int numero;
        try {
            numero = Integer.parseInt(texto.strip());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La capacidad debe ser un entero mayor que 0.");
        }
        if (numero <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser un entero mayor que 0.");
        }
        return numero;
    }

    public record Registro(String nombre, String edificio, int planta, int capacidad,
            TipoAula tipo, String responsable, EstadoAula estado) {
    }
}
