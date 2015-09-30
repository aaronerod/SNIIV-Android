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
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.modelos.EvolucionFinanciamiento;

/**
 * Created by octavio.munguia on 24/09/2015.
 */
public class ParseEvolucionFinanciamiento extends ParseBase<EvolucionFinanciamiento[]> {
    private static String TAG = ParseEvolucionFinanciamiento.class.getSimpleName();
    private JSONObject object;

    public ParseEvolucionFinanciamiento() {
        super("get_finan_evol_acum");
    }

    @Override
    public EvolucionFinanciamiento[] getDatos() {
        Document xml = getDocument();

        NodeList nList = xml.getElementsByTagName("get_finan_evol_acumResponse");
        ArrayList<EvolucionFinanciamiento> datos = new ArrayList<>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                String jsonString = Utils.getTextContent(element, "get_finan_evol_acumResult");
                try {
                    object = new JSONObject(jsonString.trim());
                    return createModel(object);
                } catch (JSONException jse) {
                    Log.v(TAG, "Error parseando json EvolucionFinanciamiento");
                }
            }
        }

        return new EvolucionFinanciamiento[0];
    }

    public static EvolucionFinanciamiento[] createModel(JSONObject object) throws JSONException {
        ArrayList<EvolucionFinanciamiento> datos = new ArrayList<>();
        Iterator<?> keys = object.keys();

        Set<String> setKeys = new TreeSet<>();
        while( keys.hasNext() ) {
            setKeys.add((String)keys.next());
        }

        for(String key : setKeys) {
            JSONArray array = (JSONArray) object.get(key);
            JSONObject o = (JSONObject) array.get(0);
            EvolucionFinanciamiento ef = new EvolucionFinanciamiento(o);
            datos.add(ef);
        }

        return datos.toArray(new EvolucionFinanciamiento[0]);
    }

    public JSONObject getJsonObject() {
        return object;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.object = jsonObject;
    }

}
