package mx.gob.conavi.sniiv.modelos;

/**
 * Created by octavio.munguia on 07/10/2015.
 */
public enum DemandaMunicipalTipo {
    FINANCIAMIENTOS(0),
    SUBSIDIOS(1);

    private final int value;
    private DemandaMunicipalTipo(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DemandaMunicipalTipo fromInteger(int x) {
        switch(x) {
            case 0:
                return FINANCIAMIENTOS;
            case 1:
                return SUBSIDIOS;
        }

        return null;
    }
}
