package com.tcna.primeraweb.services;

import java.util.List;

public interface ICRUD<T, K> {

    public List<T> listarTodos();

    public T obtenerPorId(K id);

    public T crear(T objeto);

    public T actualizar(K id, T objeto);

    public void eliminar(K id);

}

