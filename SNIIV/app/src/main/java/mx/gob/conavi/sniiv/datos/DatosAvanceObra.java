package mx.gob.conavi.sniiv.datos;

import android.content.Context;

import mx.gob.conavi.sniiv.modelos.AvanceObra;
import mx.gob.conavi.sniiv.sqlite.AvanceObraRepository;

/**
 * Created by admin on 06/08/15.
 */
public class DatosAvanceObra {
    private AvanceObra[] datos;
    private AvanceObraRepository repository;

    public DatosAvanceObra(Context context, AvanceObra[] datos) {
        this.datos = datos;
        repository = new AvanceObraRepository(context);
    }

    public AvanceObra consultaNacional() {
        return repository.consultaNacional();
    }

    public AvanceObra consultaEntidad(int entidad) {
        AvanceObra resultado = null;
        for (AvanceObra dato : datos) {
            if (dato.getCve_ent() == entidad) {
                resultado = dato;
                break;
            }
        }

        return resultado;
    }
}
