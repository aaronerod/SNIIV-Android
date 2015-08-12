package mx.gob.conavi.sniiv.datos;

import android.content.Context;

import mx.gob.conavi.sniiv.modelos.ReporteGeneral;
import mx.gob.conavi.sniiv.sqlite.ReporteGeneralRepository;

/**
 * Created by admin on 03/08/15.
 */
public class DatosReporteGeneral {
    private final ReporteGeneral[] datos;
    private final ReporteGeneralRepository repository;

    public DatosReporteGeneral(Context context, ReporteGeneral[] datos) {
        this.datos = datos;
        repository = new ReporteGeneralRepository(context);
    }

    public ReporteGeneral consultaNacional() {
        return repository.consultaNacional();
    }

    public ReporteGeneral consultaEntidad(int entidad) {
        ReporteGeneral resultado = null;
        for (ReporteGeneral reporte : datos) {
            if (reporte.getCve_ent() == entidad) {
                resultado = reporte;
                break;
            }
        }

        return resultado;
    }
}
