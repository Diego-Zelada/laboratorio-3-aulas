package edu.ufg.aulas.controladores;

import edu.ufg.aulas.App;
import edu.ufg.aulas.entidades.EstadoAula;
import edu.ufg.aulas.entidades.TipoAula;
import edu.ufg.aulas.entidades.Usuario;
import edu.ufg.aulas.repositorios.AulaRepositorio.AulaDuplicadaException;
import edu.ufg.aulas.repositorios.CatalogoRepositorio;
import edu.ufg.aulas.servicios.RegistroAulas;
import edu.ufg.aulas.servicios.RegistroAulas.Registro;
import edu.ufg.aulas.util.TareasFx;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class HomeController {
    private static final Logger LOG = Logger.getLogger(HomeController.class.getName());
    private final CatalogoRepositorio catalogos = new CatalogoRepositorio();
    private final RegistroAulas registro = new RegistroAulas();

    @FXML private Label sesion;
    @FXML private Label mensaje;
    @FXML private GridPane formulario;
    @FXML private TextField nombre;
    @FXML private TextField edificio;
    @FXML private TextField capacidad;
    @FXML private TextField responsable;
    @FXML private ComboBox<Integer> planta;
    @FXML private ComboBox<TipoAula> tipoAula;
    @FXML private ComboBox<EstadoAula> estado;
    @FXML private Button botonGuardar;
    @FXML private Button botonLimpiar;
    @FXML private Button botonSalir;
    @FXML private Button botonRecargar;

    private App app;
    private Usuario usuario;

    public void inicializar(App aplicacion, Usuario autenticado) {
        app = aplicacion;
        usuario = autenticado;
        sesion.setText(usuario.getNombre());
        cargarOpciones();
    }

    @FXML
    private void cargarOpciones() {
        cambiarOcupado(true);
        mostrarRecarga(false);
        mostrarMensaje("Cargando opciones desde MariaDB…", "");
        TareasFx.ejecutar(catalogos::consultar, opciones -> {
            planta.getItems().setAll(opciones.plantas());
            tipoAula.getItems().setAll(opciones.tipos());
            estado.getItems().setAll(opciones.estados());
            cambiarOcupado(false);
            if (opciones.tieneOpciones()) {
                mostrarMensaje("", "");
                nombre.requestFocus();
            } else {
                mostrarRecarga(true);
                mostrarMensaje("Faltan opciones en los catálogos de MariaDB.", "error");
            }
        }, error -> {
            LOG.log(Level.WARNING, "No se pudieron cargar los catálogos.", error);
            cambiarOcupado(false);
            mostrarRecarga(true);
            mostrarMensaje("No se cargaron las opciones. Revisa MariaDB y vuelve a intentar.", "error");
        });
    }

    @FXML
    private void guardar() {
        Registro datos;
        try {
            datos = registro.validar(nombre.getText(), edificio.getText(), planta.getValue(),
                    capacidad.getText(), tipoAula.getValue(), responsable.getText(), estado.getValue());
        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage(), "error");
            return;
        }

        cambiarOcupado(true);
        mostrarMensaje("Guardando aula…", "");
        TareasFx.ejecutar(() -> registro.guardar(datos, usuario), id -> {
            cambiarOcupado(false);
            limpiarCampos();
            mostrarMensaje("Aula " + datos.nombre() + " guardada correctamente. ID " + id + ".", "exito");
        }, error -> {
            cambiarOcupado(false);
            if (error instanceof AulaDuplicadaException) {
                mostrarMensaje(error.getMessage(), "error");
            } else {
                LOG.log(Level.WARNING, "No se pudo guardar el aula.", error);
                mostrarMensaje("No se pudo guardar el aula. Revisa MariaDB e inténtalo de nuevo.", "error");
            }
        });
    }

    @FXML
    private void limpiar() {
        limpiarCampos();
        if (!botonRecargar.isVisible()) {
            mostrarMensaje("", "");
        }
    }

    private void limpiarCampos() {
        nombre.clear();
        edificio.clear();
        capacidad.clear();
        responsable.clear();
        planta.setValue(null);
        tipoAula.setValue(null);
        estado.setValue(null);
        nombre.requestFocus();
    }

    private void cambiarOcupado(boolean ocupado) {
        formulario.setDisable(ocupado);
        botonSalir.setDisable(ocupado);
        botonLimpiar.setDisable(ocupado);
        botonGuardar.setDisable(ocupado || planta.getItems().isEmpty()
                || tipoAula.getItems().isEmpty() || estado.getItems().isEmpty());
    }

    private void mostrarRecarga(boolean visible) {
        botonRecargar.setVisible(visible);
        botonRecargar.setManaged(visible);
    }

    private void mostrarMensaje(String texto, String estilo) {
        mensaje.setText(texto);
        mensaje.getStyleClass().removeAll("error", "exito");
        if (!estilo.isEmpty()) {
            mensaje.getStyleClass().add(estilo);
        }
    }

    @FXML
    private void cerrarSesion() {
        try {
            app.mostrarLogin();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "No se pudo abrir Login.", e);
            mostrarMensaje("No se pudo regresar al inicio de sesión.", "error");
        }
    }
}
