package mx.gob.conavi.sniiv.modelos;

import java.util.EnumSet;

/**
 * Created by octavio.munguia on 01/09/2015.
 */
public enum EstadoMenuOferta {
    DATOS,
    GUARDAR,
    NINGUNO;

    public static final EnumSet<EstadoMenuOferta> AMBOS = EnumSet.of(DATOS, GUARDAR);
}
