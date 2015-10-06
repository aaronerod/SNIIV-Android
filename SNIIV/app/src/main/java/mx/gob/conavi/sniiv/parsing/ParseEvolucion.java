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
import mx.gob.conavi.sniiv.modelos.Evolucion;
import mx.gob.conavi.sniiv.modelos.EvolucionTipo;

/**
 * Created by octavio.munguia on 24/09/2015.
 */
public class ParseEvolucion extends ParseBase<Evolucion[]> {
    private static String TAG = ParseEvolucion.class.getSimpleName();
    private JSONObject object;
    private String[] actions = {"get_finan_evol_acum", "get_subs_evol_acum", "get_regviv_evol_acum"};
    private String[] tagNames = {"get_finan_evol_acumResponse", "get_subs_evol_acumResponse",
                                    "get_regviv_evol_acumResponse"};
    private String[] elements = {"get_finan_evol_acumResult", "get_subs_evol_acumResult",
                                    "get_regviv_evol_acumResult"};

    private int index = 0;

    public ParseEvolucion(EvolucionTipo tipo) {
        switch (tipo) {
            case FINANCIAMIENTOS:
                index = 0;
                break;
            case SUBSIDIOS:
                index = 1;
                break;
            case REGISTRO_VIVIENDA:
                index = 2;
                break;
        }

        setSoapAction(actions[index]);
    }

    @Override
    public Evolucion[] getDatos() {
        Document xml = getDocument();

        NodeList nList = xml.getElementsByTagName(tagNames[index]);
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                String jsonString = Utils.getTextContent(element, elements[index]);
                try {
                    object = new JSONObject(jsonString.trim());
                    return createModel(object);
                } catch (JSONException jse) {
                    Log.v(TAG, "Error parseando json Evolucion");
                }
            }
        }

        return new Evolucion[0];
    }

    public static Evolucion[] createModel(JSONObject object) throws JSONException {
        ArrayList<Evolucion> datos = new ArrayList<>();
        Iterator<?> keys = object.keys();

        Set<String> setKeys = new TreeSet<>();
        while( keys.hasNext() ) {
            setKeys.add((String)keys.next());
        }

        for(String key : setKeys) {
            JSONArray array = (JSONArray) object.get(key);
            JSONObject o = (JSONObject) array.get(0);
            Evolucion ef = new Evolucion(o);
            datos.add(ef);
        }

        return datos.toArray(new Evolucion[0]);
    }

    public JSONObject getJsonObject() {
        return object;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.object = jsonObject;
    }

}
