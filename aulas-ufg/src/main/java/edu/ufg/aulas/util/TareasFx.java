package edu.ufg.aulas.util;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javafx.concurrent.Task;

public final class TareasFx {
    private TareasFx() {
    }

    public static <T> void ejecutar(Callable<T> operacion, Consumer<T> alCompletar,
            Consumer<Throwable> alFallar) {
        Task<T> tarea = new Task<>() {
            @Override
            protected T call() throws Exception {
                return operacion.call();
            }
        };
        tarea.setOnSucceeded(evento -> alCompletar.accept(tarea.getValue()));
        tarea.setOnFailed(evento -> alFallar.accept(tarea.getException()));

        Thread hilo = new Thread(tarea, "aulas-base-de-datos");
        hilo.setDaemon(true);
        hilo.start();
    }
}
