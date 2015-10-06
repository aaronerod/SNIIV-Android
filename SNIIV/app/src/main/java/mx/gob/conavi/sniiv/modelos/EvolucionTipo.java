package mx.gob.conavi.sniiv.modelos;

/**
 * Created by octavio.munguia on 02/10/2015.
 */
public enum EvolucionTipo {
    FINANCIAMIENTOS(0),
    SUBSIDIOS(1),
    REGISTRO_VIVIENDA(2);

    private final int value;
    private EvolucionTipo(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EvolucionTipo fromInteger(int x) {
        switch(x) {
            case 0:
                return FINANCIAMIENTOS;
            case 1:
                return SUBSIDIOS;
            case 2:
                return REGISTRO_VIVIENDA;
        }

        return null;
    }
}
