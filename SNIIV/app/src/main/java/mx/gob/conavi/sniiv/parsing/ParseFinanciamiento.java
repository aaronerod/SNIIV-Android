package mx.gob.conavi.sniiv.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.modelos.demanda.Financiamiento;

/**
 * Created by admin on 10/08/15.
 */
public class ParseFinanciamiento extends ParseBase<Financiamiento[]> {
    public ParseFinanciamiento() {
        super("Financiamientos");
    }

    @Override
    public Financiamiento[] getDatos() {
        Document xml = getDocument();

        NodeList nList = xml.getElementsByTagName("app_sniiv_rep_finan");
        ArrayList<Financiamiento> datos = new ArrayList<>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                Financiamiento dato = new Financiamiento(Utils.parseInt(Utils.getTextContent(element, "cve_ent")),
                        Utils.getTextContent(element, "organismo"),
                        Utils.getTextContent(element, "destino"),
                        Utils.getTextContent(element, "agrupacion"),
                        Utils.parseLong(Utils.getTextContent(element, "acciones")),
                        Utils.parseDouble(Utils.getTextContent(element, "monto")));
                datos.add(dato);
            }
        }

        return datos.toArray(new Financiamiento[0]);
    }
}
