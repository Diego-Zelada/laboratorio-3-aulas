package edu.ufg.aulas.util;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {
    private static SessionFactory factory;

    private HibernateUtil() {
    }

    public static synchronized SessionFactory obtenerFactory() {
        if (factory == null) {
            Path archivo = Path.of(System.getProperty("aulas.config", "config/aulas.properties"));
            Properties opciones = new Properties();
            try (Reader reader = Files.newBufferedReader(archivo)) {
                opciones.load(reader);
            } catch (IOException e) {
                throw new IllegalStateException("No se pudo leer " + archivo, e);
            }

            Configuration config = new Configuration().configure();
            config.setProperty("hibernate.connection.url", requerida(opciones, "db.url"));
            config.setProperty("hibernate.connection.username", requerida(opciones, "db.usuario"));
            config.setProperty("hibernate.connection.password", requerida(opciones, "db.clave"));
            factory = config.buildSessionFactory();
        }

        return factory;
    }

    private static String requerida(Properties opciones, String propiedad) {
        String valor = opciones.getProperty(propiedad);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Completa la propiedad " + propiedad);
        }
        return valor;
    }

    public static synchronized void cerrar() {
        if (factory != null) {
            factory.close();
            factory = null;
        }
    }
}
