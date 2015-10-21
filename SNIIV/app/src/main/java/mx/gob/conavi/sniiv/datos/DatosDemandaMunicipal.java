package mx.gob.conavi.sniiv.datos;

import org.apache.commons.lang3.NotImplementedException;

import mx.gob.conavi.sniiv.modelos.demanda.DemandaMunicipal;

/**
 * Created by octavio.munguia on 07/10/2015.
 */
public class DatosDemandaMunicipal extends Datos<DemandaMunicipal> {
    public DatosDemandaMunicipal(DemandaMunicipal[] datos) {
        super(datos);
    }

    @Override
    public DemandaMunicipal consultaNacional() {
        throw new NotImplementedException("consultaNacional");
    }

    @Override
    public DemandaMunicipal consultaEntidad(int entidad) {
        DemandaMunicipal resultado = null;
        for (int i = 0; i < datos.length; i++) {
            if (i == entidad) {
                resultado = datos[i];
                break;
            }
        }

        return resultado;
    }
}
