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
   private ArrayList<DemandaMunicipalResultado> municipios;

    public DemandaMunicipal(JSONObject object, DemandaMunicipalTipo tipo) throws JSONException {
        ArrayList<DemandaMunicipalResultado> datos = new ArrayList<>();
        Iterator<?> keys = object.keys();

        Set<String> setKeys = new TreeSet<>();
        while( keys.hasNext() ) {
            setKeys.add((String)keys.next());
        }

        for(String key : setKeys) {
            JSONObject o = (JSONObject) object.get(key);
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


}
