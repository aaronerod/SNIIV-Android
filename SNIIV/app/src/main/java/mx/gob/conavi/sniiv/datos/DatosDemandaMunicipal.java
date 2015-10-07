package mx.gob.conavi.sniiv.datos;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by octavio.munguia on 07/10/2015.
 */
public class DatosDemandaMunicipal extends Datos<DatosDemandaMunicipal> {
    public DatosDemandaMunicipal(DatosDemandaMunicipal[] datos) {
        super(datos);
    }

    @Override
    public DatosDemandaMunicipal consultaNacional() {
        throw new NotImplementedException("consultaNacional");
    }

    @Override
    public DatosDemandaMunicipal consultaEntidad(int entidad) {
        DatosDemandaMunicipal resultado = null;
        for (int i = 0; i < datos.length; i++) {
            if (i == entidad) {
                resultado = datos[i];
                break;
            }
        }

        return resultado;
    }
}
