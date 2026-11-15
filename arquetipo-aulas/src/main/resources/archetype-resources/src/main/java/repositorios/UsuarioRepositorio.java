package ${package}.repositorios;

import java.util.Optional;
import ${package}.entidades.Usuario;
import ${package}.util.Claves;
import ${package}.util.HibernateUtil;
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
