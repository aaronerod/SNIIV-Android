package mx.gob.conavi.sniiv.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.modelos.oferta.PCU;

/**
 * Created by admin on 06/08/15.
 */
public class ParsePCU extends ParseBase<PCU[]> {
    private static String TAG = ParsePCU.class.getSimpleName();

    public ParsePCU() {
        super("PCU_Vivienda_Vigente");
    }

    @Override
    public PCU[] getDatos() {
        Document xml = getDocument();

        NodeList nList = xml.getElementsByTagName("app_sniiv_vv_x_pcu");
        ArrayList<PCU> datos = new ArrayList<>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                PCU dato = new PCU(Utils.parseInt(Utils.getTextContent(element, "cve_ent")),
                        Utils.parseLong(Utils.getTextContent(element, "u1")),
                        Utils.parseLong(Utils.getTextContent(element, "u2")),
                        Utils.parseLong(Utils.getTextContent(element, "u3")),
                        Utils.parseLong(Utils.getTextContent(element, "fc")),
                        Utils.parseLong(Utils.getTextContent(element, "nd")),
                        Utils.parseLong(Utils.getTextContent(element, "total")));
                datos.add(dato);
            }
        }

        return datos.toArray(new PCU[0]);
    }
}
