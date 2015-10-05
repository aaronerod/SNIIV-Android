package mx.gob.conavi.sniiv.modelos;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import mx.gob.conavi.sniiv.parsing.ParseFechas;

/**
 * Created by octavio.munguia on 24/09/2015.
 */
public class EvolucionFinanciamiento {
    private static String TAG = ParseFechas.class.getSimpleName();
    public static final String FINANCIAMIENTO = "Financiamientos";
    public static final String SUBSIDIO = "Subsidios";

    Map<String, EvolucionFinanciamientoResultado> periodos;

    public EvolucionFinanciamiento() {
        periodos = new TreeMap<>();
    }

    public EvolucionFinanciamiento(Map<String, EvolucionFinanciamientoResultado> periodos) {
        this.periodos = periodos;
    }

    public EvolucionFinanciamiento(JSONObject json) throws JSONException {
        this();

        Iterator<?> keys = json.keys();
        while(keys.hasNext()) {
            String key = (String)keys.next();
            JSONArray array = (JSONArray) json.get(key);
            EvolucionFinanciamientoResultado efr = new EvolucionFinanciamientoResultado(array);
            periodos.put(key, efr);
        }
    }

    public Map<String, EvolucionFinanciamientoResultado> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(Map<String, EvolucionFinanciamientoResultado> periodos) {
        this.periodos = periodos;
    }

    public ArrayList<String> getParties() {
        ArrayList<String> parties = new ArrayList<>(periodos.keySet());
        return parties;
    }

    public ArrayList<double[]> getYValuesAcciones() {
        ArrayList<double[]> yValues = new ArrayList<>();
        for (String key : periodos.keySet()) {
            EvolucionFinanciamientoResultado resultado = periodos.get(key);
            Consulta[] meses = resultado.getMeses();
            double[] values = new double[meses.length];
            for (int i = 0; i < meses.length; i++) {
                if (meses[i] == null) {break;}
                values[i] = meses[i].getAcciones();
            }
            yValues.add(values);
        }

        return yValues;
    }

    public ArrayList<double[]> getYValuesMontos() {
        ArrayList<double[]> yValues = new ArrayList<>();
        for (String key : periodos.keySet()) {
            EvolucionFinanciamientoResultado resultado = periodos.get(key);
            Consulta[] meses = resultado.getMeses();
            double[] values = new double[meses.length];
            for (int i = 0; i < meses.length; i++) {
                if (meses[i] == null) {break;}
                values[i] = meses[i].getMonto();
            }
            yValues.add(values);
        }

        return yValues;
    }
}
