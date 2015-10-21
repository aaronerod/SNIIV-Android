package mx.gob.conavi.sniiv.modelos.demanda;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import mx.gob.conavi.sniiv.modelos.Consulta;
import mx.gob.conavi.sniiv.modelos.DemandaMunicipalResultado;
import mx.gob.conavi.sniiv.modelos.DemandaMunicipalTipo;

/**
 * Created by octavio.munguia on 07/10/2015.
 */
public class DemandaMunicipal {
    private static final String TAG = DemandaMunicipal.class.getSimpleName();
    public static final String FINANCIAMIENTO = TAG + "Financiamientos";
    public static final String SUBSIDIO = TAG + "Subsidios";

    private ArrayList<DemandaMunicipalResultado> municipios;


    public DemandaMunicipal(JSONArray array, DemandaMunicipalTipo tipo) throws JSONException {
        ArrayList<DemandaMunicipalResultado> datos = new ArrayList<>();

        for (int i = 0, size = array.length(); i < size; i++) {
            JSONObject o = array.getJSONObject(i);
            DemandaMunicipalResultado dmr = new DemandaMunicipalResultado(o, tipo);
            datos.add(dmr);
        }

        this.municipios = datos;
    }

    public ArrayList<String> getParties() {
        ArrayList<String> parties = new ArrayList<>();

        for (DemandaMunicipalResultado r : municipios) {
            parties.add(r.getMunicipio());
        }

        return parties;
    }

    public ArrayList<float[]> getYValuesAcciones() {
        ArrayList<float[]> yValues = new ArrayList<>();

        for (DemandaMunicipalResultado r : municipios) {
            yValues.add(r.getValuesAcciones());
        }

        return yValues;
    }

    public ArrayList<String> getXValuesAcciones() {
        return municipios.get(0).getCamposAcciones();
    }

    public ArrayList<float[]> getYValuesMontos() {
        ArrayList<float[]> yValues = new ArrayList<>();

        for (DemandaMunicipalResultado r : municipios) {
            yValues.add(r.getValuesMontos());
        }

        return yValues;
    }

    public ArrayList<String> getXValuesMontos() {
        return municipios.get(0).getCamposMontos();
    }
}
