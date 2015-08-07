package mx.gob.conavi.sniiv.datos;

import android.content.Context;

import mx.gob.conavi.sniiv.modelos.ValorVivienda;
import mx.gob.conavi.sniiv.sqlite.ValorViviendaRepository;

/**
 * Created by admin on 07/08/15.
 */
public class DatosValorVivienda {
    private ValorVivienda[] datos;
    private ValorViviendaRepository repository;

    public DatosValorVivienda(Context context, ValorVivienda[] datos) {
        this.datos = datos;
        repository = new ValorViviendaRepository(context);
    }

    public ValorVivienda consultaNacional() {
        return repository.consultaNacional();
    }

    public ValorVivienda consultaEntidad(int entidad) {
        ValorVivienda resultado = null;
        for (ValorVivienda dato : datos) {
            if (dato.getCve_ent() == entidad) {
                resultado = dato;
                break;
            }
        }

        return resultado;
    }
}
