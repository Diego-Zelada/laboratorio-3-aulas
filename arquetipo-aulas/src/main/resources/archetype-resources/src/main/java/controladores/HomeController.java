package ${package}.controladores;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ${package}.App;
import ${package}.entidades.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {
    private static final Logger LOG = Logger.getLogger(HomeController.class.getName());

    @FXML private Label sesion;
    @FXML private Label mensaje;
    private App app;

    public void inicializar(App aplicacion, Usuario usuario) {
        app = aplicacion;
        sesion.setText(usuario.getNombre());
    }

    @FXML
    private void cerrarSesion() {
        try {
            app.mostrarLogin();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "No se pudo abrir Login.", e);
            mensaje.setText("No se pudo regresar al inicio de sesión.");
        }
    }
}
