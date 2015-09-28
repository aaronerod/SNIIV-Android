package mx.gob.conavi.sniiv.datos;

import android.content.Context;

import mx.gob.conavi.sniiv.modelos.oferta.AvanceObra;
import mx.gob.conavi.sniiv.sqlite.AvanceObraRepository;

/**
 * Created by admin on 06/08/15.
 */
public class DatosAvanceObra extends Datos<AvanceObra> {

    public DatosAvanceObra(Context context, AvanceObra[] datos) {
        super(datos);
        repository = new AvanceObraRepository(context);
    }

    public AvanceObra consultaNacional() {
        return ((AvanceObraRepository)repository).consultaNacional();
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
