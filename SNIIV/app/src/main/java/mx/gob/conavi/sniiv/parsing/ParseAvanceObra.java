package mx.gob.conavi.sniiv.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.modelos.AvanceObra;

/**
 * Created by admin on 06/08/15.
 */
public class ParseAvanceObra extends ParseBase<AvanceObra[]> {
    private static String TAG = ParseAvanceObra.class.getSimpleName();
    public ParseAvanceObra() {
        super("Avance_Vivienda_Vigente");
    }

    @Override
    public AvanceObra[] getDatos() {
        Document xml = getDocument();

        NodeList nList = xml.getElementsByTagName("app_sniiv_vv_x_avanc");
        ArrayList<AvanceObra> datos = new ArrayList<>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                AvanceObra avance = new AvanceObra(Utils.parseInt(Utils.getTextContent(element, "cve_ent")),
                        Utils.parseLong(Utils.getTextContent(element, "viv_proc_m50")),
                        Utils.parseLong(Utils.getTextContent(element, "viv_proc_50_99")),
                        Utils.parseLong(Utils.getTextContent(element, "viv_term_rec")),
                        Utils.parseLong(Utils.getTextContent(element, "viv_term_ant")),
                        Utils.parseLong(Utils.getTextContent(element, "total")));
                datos.add(avance);
            }
        }

        return datos.toArray(new AvanceObra[0]);
    }
}
