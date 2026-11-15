package edu.ufg.aulas.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "aulas", uniqueConstraints = {
    @UniqueConstraint(name = "uk_aulas_edificio_nombre", columnNames = {"edificio", "nombre"})
})
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String edificio;

    @Column(nullable = false)
    private int planta;

    @Column(nullable = false)
    private int capacidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_aula_id", nullable = false)
    private TipoAula tipoAula;

    @Column(nullable = false, length = 120)
    private String responsable;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado_aula_id", nullable = false)
    private EstadoAula estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registrada_por", nullable = false)
    private Usuario registradaPor;

    protected Aula() {
    }

    public Aula(String nombre, String edificio, int planta, int capacidad,
            TipoAula tipoAula, String responsable, EstadoAula estado, Usuario registradaPor) {
        this.nombre = nombre;
        this.edificio = edificio;
        this.planta = planta;
        this.capacidad = capacidad;
        this.tipoAula = tipoAula;
        this.responsable = responsable;
        this.estado = estado;
        this.registradaPor = registradaPor;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEdificio() {
        return edificio;
    }

    public Long getId() {
        return id;
    }
}
