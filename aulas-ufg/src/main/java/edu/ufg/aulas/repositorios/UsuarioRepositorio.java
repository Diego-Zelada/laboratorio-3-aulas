package edu.ufg.aulas.repositorios;

import java.util.Optional;
import edu.ufg.aulas.entidades.Usuario;
import edu.ufg.aulas.util.Claves;
import edu.ufg.aulas.util.HibernateUtil;
import org.hibernate.Session;

public class UsuarioRepositorio {
    public Optional<Usuario> autenticar(String correo, char[] clave) {
        Optional<Usuario> usuario;
        try (Session sesion = HibernateUtil.obtenerFactory().openSession()) {
            usuario = sesion.createSelectionQuery(
                            "from Usuario u where u.correo = :correo", Usuario.class)
                    .setParameter("correo", correo)
                    .uniqueResultOptional();
        }
        return usuario.filter(u -> Claves.coincide(clave, u.getClaveHash()));
    }
}
