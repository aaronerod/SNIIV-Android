package mx.gob.conavi.sniiv.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;

/**
 * Created by admin on 31/07/15.
 */
public class ParseReporteGeneral extends ParseBase<ReporteGeneral[]> {
    private static String TAG = ParseReporteGeneral.class.getSimpleName();
    public ParseReporteGeneral() {
        super("Totales");
    }

    @Override
    public ReporteGeneral[] getDatos() {
        Document xml = getDocument();

        NodeList nList = xml.getElementsByTagName("app_sniiv_tot");
        ArrayList<ReporteGeneral> datos = new ArrayList<>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                ReporteGeneral reporte = new ReporteGeneral(Utils.parseInt(Utils.getTextContent(element, "cve_ent")),
                        Utils.parseLong(Utils.getTextContent(element, "acc_finan")),
                        Utils.parseLong(Utils.getTextContent(element, "mto_finan")),
                        Utils.parseLong(Utils.getTextContent(element, "acc_subs")),
                        Utils.parseLong(Utils.getTextContent(element, "mto_subs")),
                        Utils.parseLong(Utils.getTextContent(element, "vv")),
                        Utils.parseLong(Utils.getTextContent(element, "vr")));
                datos.add(reporte);
            }
        }

        return datos.toArray(new ReporteGeneral[0]);
    }
}