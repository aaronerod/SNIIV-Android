package mx.gob.conavi.sniiv.parsing;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

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
                ReporteGeneral reporte = new ReporteGeneral(parseInt(getTextContent(element, "cve_ent")),
                        parseLong(getTextContent(element, "acc_finan")),
                        parseLong(getTextContent(element, "mto_finan")),
                        parseLong(getTextContent(element, "acc_subs")),
                        parseLong(getTextContent(element, "mto_subs")),
                        parseLong(getTextContent(element, "vv")),
                        parseLong(getTextContent(element, "vr")));
                Log.v(TAG, reporte.toString());
                datos.add(reporte);
            }
        }

        return datos.toArray(new ReporteGeneral[0]);
    }

    private String getTextContent(Element element, String tag) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

    private long parseLong(String string) {
        try {
           return Long.parseLong(string);
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }

    private int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }
}