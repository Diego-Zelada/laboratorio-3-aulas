package edu.ufg.aulas.repositorios;

import edu.ufg.aulas.entidades.EstadoAula;
import edu.ufg.aulas.entidades.TipoAula;
import edu.ufg.aulas.util.HibernateUtil;
import java.util.List;

public class CatalogoRepositorio {
    public Catalogos consultar() {
        return HibernateUtil.obtenerFactory().fromTransaction(sesion -> {
            List<Integer> plantas = sesion.createSelectionQuery(
                    "select p.numero from Planta p order by p.numero", Integer.class).list();
            List<TipoAula> tipos = sesion.createSelectionQuery(
                    "from TipoAula t order by t.id", TipoAula.class).list();
            List<EstadoAula> estados = sesion.createSelectionQuery(
                    "from EstadoAula e order by e.id", EstadoAula.class).list();
            return new Catalogos(plantas, tipos, estados);
        });
    }

    public record Catalogos(List<Integer> plantas, List<TipoAula> tipos, List<EstadoAula> estados) {
        public boolean tieneOpciones() {
            return !plantas.isEmpty() && !tipos.isEmpty() && !estados.isEmpty();
        }
    }
}
