package ${package}.controladores;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import ${package}.App;
import ${package}.repositorios.UsuarioRepositorio;
import ${package}.util.TareasFx;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());
    private final UsuarioRepositorio usuarios = new UsuarioRepositorio();

    @FXML private TextField correo;
    @FXML private PasswordField clave;
    @FXML private Button botonIngresar;
    @FXML private Label mensaje;
    private App app;

    public void inicializar(App aplicacion) {
        app = aplicacion;
    }

    @FXML
    private void ingresar() {
        if (botonIngresar.isDisabled()) {
            return;
        }
        String email = correo.getText().strip().toLowerCase(Locale.ROOT);
        if (email.isBlank() || clave.getText().isBlank()) {
            mostrarMensaje("Escribe el correo y la clave.", true);
            return;
        }
        if (email.length() > 254 || !email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            mostrarMensaje("Escribe un correo válido.", true);
            return;
        }

        char[] intento = clave.getText().toCharArray();
        clave.clear();
        botonIngresar.setDisable(true);
        mostrarMensaje("Verificando acceso…", false);

        TareasFx.ejecutar(() -> {
            try {
                return usuarios.autenticar(email, intento);
            } finally {
                Arrays.fill(intento, '\0');
            }
        }, usuario -> {
            botonIngresar.setDisable(false);
            if (usuario.isEmpty()) {
                mostrarMensaje("Correo o clave incorrectos.", true);
                clave.requestFocus();
                return;
            }
            try {
                app.mostrarHome(usuario.orElseThrow());
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "No se pudo abrir Home.", e);
                mostrarMensaje("No se pudo abrir el formulario.", true);
            }
        }, error -> {
            LOG.log(Level.WARNING, "Error al consultar el usuario.", error);
            botonIngresar.setDisable(false);
            mostrarMensaje("Revisa MariaDB y el archivo config/aulas.properties.", true);
        });
    }

    private void mostrarMensaje(String texto, boolean error) {
        mensaje.setText(texto);
        mensaje.getStyleClass().remove("error");
        if (error) {
            mensaje.getStyleClass().add("error");
        }
    }
}
