package mx.gob.conavi.sniiv.parsing;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.modelos.DemandaMunicipalTipo;
import mx.gob.conavi.sniiv.modelos.Evolucion;
import mx.gob.conavi.sniiv.modelos.demanda.DemandaMunicipal;

/**
 * Created by octavio.munguia on 07/10/2015.
 */
public class ParseDemandaMunicipal extends ParseBase<DemandaMunicipal[]> {
    private static String TAG = ParseDemandaMunicipal.class.getSimpleName();

    private JSONObject object;
    private String[] actions = {"get_finan_rg_mun", "get_subs_rg_mun"};
    private String[] tagNames = {"get_finan_rg_munResponse", "get_subs_rg_munResponse"};
    private String[] elements = {"get_finan_rg_munResult", "get_subs_rg_munResult"};

    private int index = 0;
    private DemandaMunicipalTipo tipo;

    public ParseDemandaMunicipal(DemandaMunicipalTipo tipo) {
        this.tipo = tipo;

        switch (tipo) {
            case FINANCIAMIENTOS:
                index = 0;
                break;
            case SUBSIDIOS:
                index = 1;
                break;
        }

        setSoapAction(actions[index]);
    }

    @Override
    public DemandaMunicipal[] getDatos() {
        Document xml = getDocument();

        NodeList nList = xml.getElementsByTagName(tagNames[index]);
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                String jsonString = Utils.getTextContent(element, elements[index]);
                try {
                    object = new JSONObject(jsonString.trim());
                    return createModel(object, tipo);
                } catch (JSONException jse) {
                    Log.v(TAG, "Error parseando json Evolucion");
                }
            }
        }

        return new DemandaMunicipal[0];
    }

    public static DemandaMunicipal[] createModel(JSONObject object, DemandaMunicipalTipo tipo) throws JSONException {
        ArrayList<DemandaMunicipal> datos = new ArrayList<>();
        Iterator<?> keys = object.keys();

        Set<String> setKeys = new TreeSet<>();
        while( keys.hasNext() ) {
            setKeys.add((String)keys.next());
        }

        for(String key : setKeys) {
            JSONArray array = (JSONArray) object.get(key);
            JSONObject o = (JSONObject) array.get(0);
            DemandaMunicipal dm = new DemandaMunicipal(o, tipo);
            datos.add(dm);
        }

        return datos.toArray(new DemandaMunicipal[0]);
    }

    public JSONObject getJsonObject() {
        return object;
    }
}
