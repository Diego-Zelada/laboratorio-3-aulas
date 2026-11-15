package edu.ufg.aulas.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipos_aula")
public class TipoAula {
    @Id
    private Integer id;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    protected TipoAula() {
    }

    @Override
    public String toString() {
        return nombre;
    }
}
