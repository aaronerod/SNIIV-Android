package mx.gob.conavi.sniiv.modelos;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by octavio.munguia on 08/10/2015.
 */
public class DemandaMunicipalResultado {
    private String municipio;
    private Map<String, Consulta> consultas;

    private String[] nombreCamposFinan = {"vn_cs", "vn_ci", "vu_cs", "vu_ci", "mj_cs", "mj_ci",
            "ot_cs", "ot_ci"};
    private String[] nombreEtiqutasFinan = {"V. Nueva CS", "V. Nueva CI", "V. Usada CS", "V. Usada CI",
            "Mejoramiento CS", "Mejoramiento CI", "Otros CS", "Otros CI"};
    private String[] nombreCamposSub = {"nueva", "usada", "autoproduccion", "mejoramiento", "lotes",
            "otros"};
    private String[] nombreEtiquetasSub = {"Nueva", "Usada", "Autoproducción", "Mejoramiento", "Lotes",
            "Otros"};

    public DemandaMunicipalResultado(JSONObject object, DemandaMunicipalTipo tipo) throws JSONException {
        this.municipio = object.getString("Municipio");
        switch (tipo) {
            case FINANCIAMIENTOS:
                this.consultas = consultaFinanciamientos(object, nombreCamposFinan, nombreEtiqutasFinan);
                break;
            case SUBSIDIOS:
                this.consultas = consultaFinanciamientos(object, nombreCamposSub, nombreEtiquetasSub);
                break;
            default:
                throw new IllegalArgumentException("tipo");
        }
    }

    public Map<String, Consulta> consultaFinanciamientos(JSONObject object, String[] nombres, String[] nombresEtiquetas) throws JSONException {
        Map<String, Consulta> consultaMap = new LinkedHashMap<>();
        int i = 0;
        for (String campo : nombres) {
            String etiqueta = nombresEtiquetas[i];
            i++;
            try {
                long acciones = object.getLong(String.format("acc_%s", campo));
                double monto = object.getDouble(String.format("mto_%s", campo));
                Consulta consulta = new Consulta(acciones, monto);
                consultaMap.put(etiqueta, consulta);
            } catch (JSONException ex) {
                Consulta consulta = new Consulta(0, 0);
                consultaMap.put(etiqueta, consulta);
            }
        }

        return consultaMap;
    }

    public float[] getValuesAcciones() {
        float[] values = new float[consultas.size()];
        int i = 0;
        for (String key : consultas.keySet()) {
            values[i] = (float)consultas.get(key).getAcciones();
            i++;
        }

        return values;
    }

    public ArrayList<String> getCamposAcciones() {
        String[] values = new String[consultas.size()];
        int i = 0;
        for (String key : consultas.keySet()) {
            values[i] = key;
            i++;
        }

        return new ArrayList<>(Arrays.asList(values));
    }

    public float[] getValuesMontos() {
        float[] values = new float[consultas.size()];
        int i = 0;
        for (String key : consultas.keySet()) {
            values[i] = (float)consultas.get(key).getMonto();
            i++;
        }

        return values;
    }

    public ArrayList<String> getCamposMontos() {
        String[] values = new String[consultas.size()];
        int i = 0;
        for (String key : consultas.keySet()) {
            values[i] = key;
            i++;
        }

        return new ArrayList<>(Arrays.asList(values));
    }

    public String getMunicipio() {
        return municipio;
    }
}
