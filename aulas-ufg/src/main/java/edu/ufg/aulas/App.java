package edu.ufg.aulas;

import java.io.IOException;
import edu.ufg.aulas.controladores.HomeController;
import edu.ufg.aulas.controladores.LoginController;
import edu.ufg.aulas.entidades.Usuario;
import edu.ufg.aulas.util.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private Stage ventana;

    @Override
    public void start(Stage stage) throws IOException {
        ventana = stage;
        ventana.setTitle("Aulas UFG");
        ventana.setMinWidth(730);
        ventana.setMinHeight(620);
        mostrarLogin();
        ventana.show();
    }

    public void mostrarLogin() throws IOException {
        FXMLLoader loader = cargar("Login.fxml");
        Parent raiz = loader.load();
        LoginController login = loader.getController();
        login.inicializar(this);
        mostrar(raiz);
    }

    public void mostrarHome(Usuario usuario) throws IOException {
        FXMLLoader loader = cargar("Home.fxml");
        Parent raiz = loader.load();
        HomeController home = loader.getController();
        home.inicializar(this, usuario);
        mostrar(raiz);
    }

    private FXMLLoader cargar(String vista) {
        return new FXMLLoader(App.class.getResource("/vistas/" + vista));
    }

    private void mostrar(Parent raiz) {
        if (ventana.getScene() == null) {
            ventana.setScene(new Scene(raiz, 900, 625));
        } else {
            ventana.getScene().setRoot(raiz);
        }
    }

    @Override
    public void stop() {
        HibernateUtil.cerrar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
