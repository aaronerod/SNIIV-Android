package mx.gob.conavi.sniiv.modelos.demanda;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import mx.gob.conavi.sniiv.modelos.Consulta;
import mx.gob.conavi.sniiv.modelos.DemandaMunicipalTipo;

/**
 * Created by octavio.munguia on 07/10/2015.
 */
public class DemandaMunicipal {
    String municipio;
    Map<String, Consulta> consultas;

    private String[] nombreCamposFinan = {"vn_cs", "vn_ci", "vu_cs", "vu_ci", "mj_cs", "mj_ci",
                                "ot_cs", "ot_ci"};
    private String[] nombreCamposSub = {"nueva", "usada", "autoproduccion", "mejoramiento", "lotes",
                                "otros"};

    public DemandaMunicipal(JSONObject object, DemandaMunicipalTipo tipo) throws JSONException {
        this.municipio = object.getString("Municipio");
        switch (tipo) {
            case FINANCIAMIENTOS:
                this.consultas = consultaFinanciamientos(object, nombreCamposFinan);
                break;
            case SUBSIDIOS:
                this.consultas = consultaFinanciamientos(object, nombreCamposSub);
                break;
            default:
                throw new IllegalArgumentException("tipo");
        }
    }

    public Map<String, Consulta> consultaFinanciamientos(JSONObject object, String[] nombres) throws JSONException {
        Map<String, Consulta> consultaMap = new LinkedHashMap<>();
        for (String campo : nombres) {
            long acciones = object.getLong(String.format("acc_%s", campo));
            double monto = object.getDouble(String.format("mto_%s", campo));
            Consulta consulta = new Consulta(acciones, monto);
            consultaMap.put(campo, consulta);
        }

        long accionesTotal = object.getLong("acciones");
        double montoTotal = object.getDouble("monto");
        Consulta consultaTotal = new Consulta(accionesTotal, montoTotal);
        consultaMap.put("total", consultaTotal);

        return consultaMap;
    }
}
