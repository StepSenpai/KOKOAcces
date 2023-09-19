package org.example;

import java.util.List;

public interface Dao<T> {

    List<T> obtenerTodos();

    void insertar(T t);

    void actualizar(T t);

    void borrar(T t);
}