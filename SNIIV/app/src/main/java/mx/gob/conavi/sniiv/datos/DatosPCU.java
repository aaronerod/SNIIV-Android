package mx.gob.conavi.sniiv.datos;

import android.content.Context;

import mx.gob.conavi.sniiv.modelos.oferta.PCU;
import mx.gob.conavi.sniiv.sqlite.PCURepository;

/**
 * Created by admin on 06/08/15.
 */
public class DatosPCU extends Datos<PCU>{

    public DatosPCU(Context context, PCU[] datos) {
        super(datos);
        repository = new PCURepository(context);
    }

    public PCU consultaNacional() {
        return ((PCURepository)repository).consultaNacional();
    }

    public PCU consultaEntidad(int entidad) {
        PCU resultado = null;
        for (PCU dato : datos) {
            if (dato.getCve_ent() == entidad) {
                resultado = dato;
                break;
            }
        }

        return resultado;
    }
}
