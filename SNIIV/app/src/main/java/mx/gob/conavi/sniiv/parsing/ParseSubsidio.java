package mx.gob.conavi.sniiv.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.modelos.demanda.Subsidio;

/**
 * Created by admin on 10/08/15.
 */
public class ParseSubsidio extends ParseBase<Subsidio[]> {
    public ParseSubsidio() {
        super("Subsidios");
    }
    @Override
    public Subsidio[] getDatos() {
        Document xml = getDocument();

        NodeList nList = xml.getElementsByTagName("app_sniiv_rep_subs");
        ArrayList<Subsidio> datos = new ArrayList<>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                Subsidio dato = new Subsidio(Utils.parseInt(Utils.getTextContent(element, "cve_ent")),
                        Utils.getTextContent(element, "tipo_ee"),
                        Utils.getTextContent(element, "modalidad"),
                        Utils.parseLong(Utils.getTextContent(element, "acciones")),
                        Utils.parseDouble(Utils.getTextContent(element, "monto")));
                datos.add(dato);
            }
        }

        return datos.toArray(new Subsidio[0]);
    }
}
