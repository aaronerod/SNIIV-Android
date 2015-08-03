package mx.gob.conavi.sniiv.parsing;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by admin on 31/07/15.
 */
public abstract class ParseBase<T> {
    private String namespace;
    private String urlString;
    private String soapAction;

    public ParseBase(String action) {
        namespace= "http://www.conavi.gob.mx:8080/WS_App_SNIIV";
        urlString = "http://www.conavi.gob.mx:8080/WS_App_SNIIV.asmx?WSDL";
        soapAction = action;
    }

    public String getXml() {
        SoapToXml soap = new SoapToXml(namespace, urlString, soapAction);
        return soap.getXmlResponse();
    }

    public Document getDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document documentoXML;
        String xmlResponse = getXml();
        try {
            builder = factory.newDocumentBuilder();
            documentoXML = builder.parse(new InputSource(new StringReader(xmlResponse)));
            documentoXML.getDocumentElement().normalize();

            return documentoXML;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract T getDatos();
}