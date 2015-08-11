package mx.gob.conavi.sniiv.datos;

import android.content.Context;

import mx.gob.conavi.sniiv.modelos.ConsultaFinanciamiento;
import mx.gob.conavi.sniiv.modelos.Financiamiento;
import mx.gob.conavi.sniiv.sqlite.FinanciamientoRepository;

/**
 * Created by admin on 10/08/15.
 */
public class DatosFinanciamiento {
    private Financiamiento[] datos;
    private FinanciamientoRepository repository;
    private ConsultaFinanciamiento nacional;

    public DatosFinanciamiento(Context context, Financiamiento[] datos) {
        this.datos = datos;
        repository = new FinanciamientoRepository(context);
    }

    public ConsultaFinanciamiento consultaNacional() {
        if (nacional == null) {
            nacional = repository.consultaNacional();
        }

        return nacional;
    }

    public ConsultaFinanciamiento consultaEntidad(int entidad) {
        return repository.consultaEntidad(entidad);
    }
}
