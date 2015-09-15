package mx.gob.conavi.sniiv.datos;

import android.content.Context;

import mx.gob.conavi.sniiv.modelos.AvanceObra;
import mx.gob.conavi.sniiv.sqlite.AvanceObraRepository;
import mx.gob.conavi.sniiv.sqlite.Repository;

/**
 * Created by octavio.munguia on 10/09/2015.
 */
public abstract class Datos<T> {
    protected T[] datos;
    protected Repository<T> repository;

    public Datos(T[] datos) {
        this.datos = datos;
    }

    public abstract T consultaNacional();

    public abstract T consultaEntidad(int entidad);
}
