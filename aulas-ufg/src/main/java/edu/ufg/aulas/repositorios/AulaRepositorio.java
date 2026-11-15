package edu.ufg.aulas.repositorios;

import edu.ufg.aulas.entidades.Aula;
import edu.ufg.aulas.util.HibernateUtil;
import org.hibernate.exception.ConstraintViolationException;

public class AulaRepositorio {
    public long guardar(Aula aula) {
        try {
            return HibernateUtil.obtenerFactory().fromTransaction(sesion -> {
                boolean existe = sesion.createSelectionQuery("""
                        select a.id from Aula a
                        where a.nombre = :nombre and a.edificio = :edificio
                        """, Long.class)
                        .setParameter("nombre", aula.getNombre())
                        .setParameter("edificio", aula.getEdificio())
                        .setMaxResults(1)
                        .uniqueResultOptional().isPresent();
                if (existe) {
                    throw new AulaDuplicadaException();
                }
                sesion.persist(aula);
                return aula.getId();
            });
        } catch (ConstraintViolationException e) {
            // MariaDB devuelve 1062 si otra transacción ya guardó el mismo par.
            if (e.getErrorCode() == 1062) {
                throw new AulaDuplicadaException(e);
            }
            throw e;
        }
    }

    public static class AulaDuplicadaException extends RuntimeException {
        public AulaDuplicadaException() {
            this(null);
        }

        private AulaDuplicadaException(Throwable causa) {
            super("Ya existe un aula con ese nombre en ese edificio.", causa);
        }
    }
}
