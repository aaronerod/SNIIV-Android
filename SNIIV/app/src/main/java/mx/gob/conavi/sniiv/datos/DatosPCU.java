package mx.gob.conavi.sniiv.datos;

import android.content.Context;

import mx.gob.conavi.sniiv.modelos.PCU;
import mx.gob.conavi.sniiv.sqlite.PCURepository;

/**
 * Created by admin on 06/08/15.
 */
public class DatosPCU {
    private PCU[] datos;
    private PCURepository repository;

    public DatosPCU(Context context, PCU[] datos) {
        this.datos = datos;
        repository = new PCURepository(context);
    }

    public PCU consultaNacional() {
        return repository.consultaNacional();
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
