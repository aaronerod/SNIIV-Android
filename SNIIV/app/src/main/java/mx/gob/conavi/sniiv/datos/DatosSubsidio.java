package mx.gob.conavi.sniiv.datos;

import android.content.Context;

import mx.gob.conavi.sniiv.modelos.ConsultaSubsidio;
import mx.gob.conavi.sniiv.modelos.Subsidio;
import mx.gob.conavi.sniiv.sqlite.SubsidioRepository;

/**
 * Created by admin on 10/08/15.
 */
public class DatosSubsidio {
    private Subsidio[] datos;
    private SubsidioRepository repository;
    private ConsultaSubsidio nacional;

    public DatosSubsidio(Context context, Subsidio[] datos) {
        this.datos = datos;
        repository = new SubsidioRepository(context);
    }

    public ConsultaSubsidio consultaNacional() {
        if (nacional == null) {
            nacional = repository.consultaNacional();
        }

        return  nacional;
    }

    public ConsultaSubsidio consultaEntidad(int entidad) {
        return repository.consultaEntidad(entidad);
    }
}
