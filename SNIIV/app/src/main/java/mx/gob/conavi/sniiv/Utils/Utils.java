package mx.gob.conavi.sniiv.Utils;


import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.w3c.dom.Element;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###.#");
    private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

    public static final ArrayList<String> listMenu = new ArrayList<>();
    public static final ArrayList<String> listEstados = new ArrayList<>();
    public static final String[] listEdo = new String[33];

    static {
        listMenu.add("Reporte General");
        listMenu.add("Oferta");
        listMenu.add("Demanda");
        populateEstados();
        populateEdos();
    }

    public static String toString(long numero) {
        return decimalFormat.format(numero);
    }

    public static String toStringDivide(long numero, int divide) {
        return decimalFormat.format((double)numero / divide);
    }

    public static String toStringDivide(long numero) {
        return toStringDivide(numero, 1000);
    }

    public static String getTextContent(Element element, String tag) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

    public static long parseLong(String string) {
        try {
            return Long.parseLong(string);
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }

    public static double parseDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }

    public static int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }

    public static void alertDialogShow(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    public static void alertDialogShow(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setTitle(title)
                .create()
                .show();
    }

    public static boolean equalDays(Date date1, Date date2) {
        return fmt.format(date1).equals(fmt.format(date2));
    }

    private static void populateEdos(){
        listEdo[0]="Nacional";
        listEdo[1]="Aguascalientes";
        listEdo[2]="Baja California";
        listEdo[3]="Baja California Sur";
        listEdo[4]="Campeche";
        listEdo[5]="Coahuila";
        listEdo[6]="Colima";
        listEdo[7]="Chiapas";
        listEdo[8]="Chihuahua";
        listEdo[9]="Distrito Federal";
        listEdo[10]="Durango";
        listEdo[11]="Guanajuato";
        listEdo[12]="Guerrero";
        listEdo[13]="Hidalgo";
        listEdo[14]="Jalisco";
        listEdo[15]="México";
        listEdo[16]="Michoacán";
        listEdo[17]="Morelos";
        listEdo[18]="Nayarit";
        listEdo[19]="Nuevo León";
        listEdo[20]="Oaxaca";
        listEdo[21]="Puebla";
        listEdo[22]="Querétaro";
        listEdo[23]="Quintana Roo";
        listEdo[24]="San Luis Potosí";
        listEdo[25]="Sinaloa";
        listEdo[26]="Sonora";
        listEdo[27]="Tabasco";
        listEdo[28]="Tamaulipas";
        listEdo[29]="Tlaxcala";
        listEdo[30]="Veracruz";
        listEdo[31]="Yucatán";
        listEdo[32]="Zacatecas";
    }

    private static void populateEstados(){
        listEstados.add("Nacional");
        listEstados.add("Aguascalientes");
        listEstados.add("Baja California");
        listEstados.add("Baja California Sur");
        listEstados.add("Campeche");
        listEstados.add("Coahuila");
        listEstados.add("Colima");
        listEstados.add("Chiapas");
        listEstados.add("Chihuahua");
        listEstados.add("Distrito Federal");
        listEstados.add("Durango");
        listEstados.add("Guanajuato");
        listEstados.add("Guerrero");
        listEstados.add("Hidalgo");
        listEstados.add("Jalisco");
        listEstados.add("México");
        listEstados.add("Michoacán");
        listEstados.add("Morelos");
        listEstados.add("Nayarit");
        listEstados.add("Nuevo León");
        listEstados.add("Oaxaca");
        listEstados.add("Puebla");
        listEstados.add("Querétaro");
        listEstados.add("Quintana Roo");
        listEstados.add("San Luis Potosí");
        listEstados.add("Sinaloa");
        listEstados.add("Sonora");
        listEstados.add("Tabasco");
        listEstados.add("Tamaulipas");
        listEstados.add("Tlaxcala");
        listEstados.add("Veracruz");
        listEstados.add("Yucatán");
        listEstados.add("Zacatecas");
    }
}
