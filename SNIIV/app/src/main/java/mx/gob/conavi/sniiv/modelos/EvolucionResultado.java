package mx.gob.conavi.sniiv.modelos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by octavio.munguia on 24/09/2015.
 */
public class EvolucionResultado {
    private Consulta[] meses;

    public EvolucionResultado() {
        meses = new Consulta[12];
    }

    public EvolucionResultado(JSONArray array) throws JSONException {
        this();

        JSONObject obj = array.getJSONObject(0);
        if (obj.has("acciones")) {
            for (int i = 0, size = array.length(); i < size; i++) {
                JSONObject consulta = array.getJSONObject(i);
                long acciones = consulta.getLong("acciones");
                double monto = consulta.getDouble("monto");

                meses[i] = new Consulta(acciones, monto);
            }
        } else {
            for (int i = 0, size = array.length(); i < size; i++) {
                JSONObject consulta = array.getJSONObject(i);
                int numVivReg = consulta.getInt("num_viv_reg");

                meses[i] = new Consulta(numVivReg, 0);
            }
        }
    }

    public EvolucionResultado(Consulta[] meses) {
        this.meses = meses;
    }

    public Consulta[] getMeses() {
        return meses;
    }

    public void setMeses(Consulta[] meses) {
        this.meses = meses;
    }
}
