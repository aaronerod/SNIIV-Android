package mx.gob.conavi.sniiv.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.modelos.Fechas;

/**
 * Created by admin on 11/08/15.
 */
public class ParseFechas extends ParseBase<Fechas[]> {

    private static String TAG = ParseFechas.class.getSimpleName();
    public ParseFechas() {
        super("Fechas");
    }

    @Override
    public Fechas[] getDatos() {
        Document xml = getDocument();

        NodeList nList = xml.getElementsByTagName("app_sniiv_tot_date");
        ArrayList<Fechas> datos = new ArrayList<>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                Fechas fechas = new Fechas(Utils.getTextContent(element, "fecha_finan").replace("Datos al ", ""),
                        Utils.getTextContent(element, "fecha_subs"),
                        Utils.getTextContent(element, "fecha_vv"));
                datos.add(fechas);
            }
        }

        return datos.toArray(new Fechas[0]);
    }
}
