package mx.gob.conavi.sniiv.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.modelos.ValorVivienda;

/**
 * Created by admin on 07/08/15.
 */
public class ParseValorVivienda extends ParseBase<ValorVivienda[]> {
    private static String TAG = ParseTipoVivienda.class.getSimpleName();

    public ParseValorVivienda() {
        super("Valor_Vivienda_Vigente");
    }

    @Override
    public ValorVivienda[] getDatos() {
        Document xml = getDocument();

        NodeList nList = xml.getElementsByTagName("app_sniiv_vv_x_val");
        ArrayList<ValorVivienda> datos = new ArrayList<>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                ValorVivienda dato = new ValorVivienda(Utils.parseInt(Utils.getTextContent(element, "cve_ent")),
                        Utils.parseLong(Utils.getTextContent(element, "economica")),
                        Utils.parseLong(Utils.getTextContent(element, "popular")),
                        Utils.parseLong(Utils.getTextContent(element, "tradicional")),
                        Utils.parseLong(Utils.getTextContent(element, "media_residencial")),
                        Utils.parseLong(Utils.getTextContent(element, "total")));
                datos.add(dato);
            }
        }

        return datos.toArray(new ValorVivienda[0]);
    }
}
