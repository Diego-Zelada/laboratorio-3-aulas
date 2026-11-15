package edu.ufg.aulas.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "plantas")
public class Planta {
    @Id
    private Integer numero;

    protected Planta() {
    }
}
