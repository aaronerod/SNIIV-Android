package mx.gob.conavi.sniiv.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.modelos.TipoVivienda;

/**
 * Created by admin on 06/08/15.
 */
public class ParseTipoVivienda extends ParseBase<TipoVivienda[]> {
    private static String TAG = ParseTipoVivienda.class.getSimpleName();

    public ParseTipoVivienda() {
        super("Tipo_Vivienda_Vigente");
    }

    @Override
    public TipoVivienda[] getDatos() {
        Document xml = getDocument();

        NodeList nList = xml.getElementsByTagName("app_sniiv_vv_x_pcu");
        ArrayList<TipoVivienda> datos = new ArrayList<>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                TipoVivienda dato = new TipoVivienda(Utils.parseInt(Utils.getTextContent(element, "cve_ent")),
                        Utils.parseLong(Utils.getTextContent(element, "horizontal")),
                        Utils.parseLong(Utils.getTextContent(element, "horizontal")),
                        Utils.parseLong(Utils.getTextContent(element, "total")));
                datos.add(dato);
            }
        }

        return datos.toArray(new TipoVivienda[0]);
    }
}
