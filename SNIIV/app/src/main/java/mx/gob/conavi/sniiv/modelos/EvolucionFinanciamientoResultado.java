package mx.gob.conavi.sniiv.modelos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by octavio.munguia on 24/09/2015.
 */
public class EvolucionFinanciamientoResultado {
    private Consulta[] meses;

    public EvolucionFinanciamientoResultado() {
        meses = new Consulta[12];
    }

    public EvolucionFinanciamientoResultado(JSONArray array) throws JSONException {
        this();
        for (int i = 0, size = array.length(); i < size; i++) {
            JSONObject consulta = array.getJSONObject(i);
            long acciones = consulta.getLong("acciones");
            double monto = consulta.getDouble("monto");

            meses[i] = new Consulta(acciones, monto);
        }
    }

    public EvolucionFinanciamientoResultado(Consulta[] meses) {
        this.meses = meses;
    }

    public Consulta[] getMeses() {
        return meses;
    }

    public void setMeses(Consulta[] meses) {
        this.meses = meses;
    }
}
