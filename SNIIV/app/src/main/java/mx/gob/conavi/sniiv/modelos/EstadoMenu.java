package mx.gob.conavi.sniiv.modelos;

import java.util.EnumSet;

/**
 * Created by octavio.munguia on 01/09/2015.
 */
public enum EstadoMenu {
    DATOS,
    GUARDAR,
    NINGUNO;

    public static final EnumSet<EstadoMenu> AMBOS = EnumSet.of(DATOS, GUARDAR);
}
