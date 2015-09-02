package mx.gob.conavi.sniiv.parsing;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.modelos.Fechas;

/**
 * Created by octavio.munguia on 27/08/2015.
 */
public class ParseFechasWeb extends ParseBase<Fechas []>{
    private static String TAG = ParseFechas.class.getSimpleName();
    public ParseFechasWeb() {
        super("get_fechas_act");
    }

    @Override
    public Fechas[] getDatos() {
        Document xml = getDocument();

        NodeList nList = xml.getElementsByTagName("get_fechas_actResponse");
        ArrayList<Fechas> datos = new ArrayList<>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                String jsonString = Utils.getTextContent(element, "get_fechas_actResult");

                try {
                    Fechas fechas = new Fechas(new JSONObject(jsonString));
                    datos.add(fechas);
                } catch (JSONException jse) {
                    Log.v(TAG, "Error parseando json FechasWeb");
                }
            }
        }

        return datos.toArray(new Fechas[0]);
    }
}
