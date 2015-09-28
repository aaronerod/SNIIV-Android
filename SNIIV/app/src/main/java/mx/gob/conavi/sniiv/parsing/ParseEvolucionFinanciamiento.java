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

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.modelos.EvolucionFinanciamiento;

/**
 * Created by octavio.munguia on 24/09/2015.
 */
public class ParseEvolucionFinanciamiento extends ParseBase<EvolucionFinanciamiento[]> {
    private static String TAG = ParseFechas.class.getSimpleName();

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
                    JSONObject object = new JSONObject(jsonString.trim());
                    Iterator<?> keys = object.keys();

                    while( keys.hasNext() ) {
                        String key = (String)keys.next();
                        JSONArray array = (JSONArray) object.get(key);
                        JSONObject o = (JSONObject) array.get(0);
                        Log.v(TAG, key);
                        EvolucionFinanciamiento ef = new EvolucionFinanciamiento(o);
                        datos.add(ef);
                    }
                } catch (JSONException jse) {
                    Log.v(TAG, "Error parseando json EvolucionFinanciamiento");
                }
            }
        }

        return datos.toArray(new EvolucionFinanciamiento[0]);
    }
}
