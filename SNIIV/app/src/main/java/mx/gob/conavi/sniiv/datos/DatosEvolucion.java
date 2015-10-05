package mx.gob.conavi.sniiv.datos;

import android.content.Context;

import mx.gob.conavi.sniiv.modelos.Evolucion;

/**
 * Created by octavio.munguia on 24/09/2015.
 */
public class DatosEvolucion extends Datos<Evolucion> {

    public DatosEvolucion(Evolucion[] datos) {
        super(datos);
    }

    @Override
    public Evolucion consultaNacional() {
        return consultaEntidad(0);
    }

    @Override
    public Evolucion consultaEntidad(int entidad) {
        Evolucion resultado = null;
        for (int i = 0; i < datos.length; i++) {
            if (i == entidad) {
                resultado = datos[i];
                break;
            }
        }

        return resultado;
    }
}
