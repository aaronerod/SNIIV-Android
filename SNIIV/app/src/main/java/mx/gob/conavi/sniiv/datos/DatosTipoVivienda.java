package mx.gob.conavi.sniiv.datos;

import android.content.Context;

import mx.gob.conavi.sniiv.modelos.TipoVivienda;
import mx.gob.conavi.sniiv.sqlite.TipoViviendaRepository;

/**
 * Created by admin on 06/08/15.
 */
public class DatosTipoVivienda extends Datos<TipoVivienda> {

    public DatosTipoVivienda(Context context, TipoVivienda[] datos) {
        super(datos);
        repository = new TipoViviendaRepository(context);
    }

    public TipoVivienda consultaNacional() {
        return ((TipoViviendaRepository)repository).consultaNacional();
    }

    public TipoVivienda consultaEntidad(int entidad) {
        TipoVivienda resultado = null;
        for (TipoVivienda dato : datos) {
            if (dato.getCve_ent() == entidad) {
                resultado = dato;
                break;
            }
        }

        return resultado;
    }
}
