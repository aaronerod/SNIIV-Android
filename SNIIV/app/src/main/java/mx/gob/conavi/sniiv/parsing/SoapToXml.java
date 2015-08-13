package mx.gob.conavi.sniiv.parsing;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


/**
 * Created by admin on 30/07/15.
 */
public class SoapToXml {
    private static final String TAG = SoapToXml.class.getSimpleName();
    private static final String SOAP_ACTION_PREFIX = "/";
    private final String namespace;
    private final String urlString;
    private final String soapAction;

    public SoapToXml(String namespace, String url, String action) {
        this.namespace = namespace;
        urlString = url;
        soapAction = action;
    }

    public String getXmlResponse() {
        String response = null;

        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            SoapObject request = new SoapObject(namespace, soapAction);
            PropertyInfo p = new PropertyInfo();
            p.setName("dat");
            p.setValue("Sniiv Android");
            p.setType(String.class);
            request.addProperty(p);
            envelope.setOutputSoapObject(request);

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(urlString, 5000);
            transport.debug = true;
            transport.call(namespace + SOAP_ACTION_PREFIX + soapAction, envelope);

            if (envelope.bodyIn != null) {
                response = transport.responseDump;
            }
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }

        return response;
    }
}
