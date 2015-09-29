package mx.gob.conavi.sniiv.datos;

import android.content.Context;

import mx.gob.conavi.sniiv.modelos.EvolucionFinanciamiento;
import mx.gob.conavi.sniiv.sqlite.EvolucionFinanciamientoRepository;

/**
 * Created by octavio.munguia on 24/09/2015.
 */
public class DatosEvolucionFinanciamiento extends Datos<EvolucionFinanciamiento> {
    public DatosEvolucionFinanciamiento(Context context, EvolucionFinanciamiento[] datos) {
        super(datos);
        // repository = new EvolucionFinanciamientoRepository(context);
    }

    @Override
    public EvolucionFinanciamiento consultaNacional() {
        return consultaEntidad(0);
    }

    @Override
    public EvolucionFinanciamiento consultaEntidad(int entidad) {
        EvolucionFinanciamiento resultado = null;
        for (int i = 0; i < datos.length; i++) {
            if (i == entidad) {
                resultado = datos[i];
                break;
            }
        }

        return resultado;
    }
}
