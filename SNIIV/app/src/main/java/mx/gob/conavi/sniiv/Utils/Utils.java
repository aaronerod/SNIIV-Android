package mx.gob.conavi.sniiv.Utils;


import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.commons.lang3.text.WordUtils;
import org.w3c.dom.Element;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Utils {
    private static final Locale locale = new Locale("es", "MX");
    public static final DecimalFormat decimalFormat = new DecimalFormat("###,###.#");
    public static final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", locale);
    public static final SimpleDateFormat fmtDMY = new SimpleDateFormat("dd/MM/yyyy", locale);
    public static final SimpleDateFormat fmtDiaMes = new SimpleDateFormat("d MMMM yyyy", locale);
    public static final SimpleDateFormat fmtMes = new SimpleDateFormat("MMMM yyyy", locale);
    public static final int THOUSAND = 1000;
    public static final int MILLION = 1000_000;

    public static final ArrayList<String> listMenu = new ArrayList<>();
    public static final ArrayList<String> listEstados = new ArrayList<>();
    public static final String[] listEdo = new String[33];
    public static final String[] listEdoNoNacional = new String[32];

    static {
        listMenu.add("Reporte General");
        listMenu.add("Oferta");
        listMenu.add("Demanda");
        listMenu.add("Evolución Mensual");
        listMenu.add("Demanda Municipal");
        populateEstados();
        populateEdos();
        populateEdosNoNacional();
    }

    public static String toString(long numero) {
        return decimalFormat.format(numero);
    }
    public static String toString(double numero) {
        return decimalFormat.format(numero);
    }

    public static String toStringDivide(long numero, int divide) {
        return decimalFormat.format((double)numero / divide);
    }

    public static String toStringDivide(double numero, int divide) {
        return decimalFormat.format(numero / divide);
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

    public static String formatoDiaMes(String fecha) {
        try {

            return WordUtils.capitalize(fmtDiaMes.format(fmtDMY.parseObject(fecha)));
        } catch (ParseException e) {
            return "";
        }
    }

    public static String formatoMes(String fecha) {
        try {
            return WordUtils.capitalize(fmtMes.format(fmtDMY.parseObject(fecha)));
        } catch (ParseException e) {
            return "";
        }
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

    private static void populateEdosNoNacional(){
        //listEdoNoNacional[0]="--------------";
        listEdoNoNacional[0]="Aguascalientes";
        listEdoNoNacional[1]="Baja California";
        listEdoNoNacional[2]="Baja California Sur";
        listEdoNoNacional[3]="Campeche";
        listEdoNoNacional[4]="Coahuila";
        listEdoNoNacional[5]="Colima";
        listEdoNoNacional[6]="Chiapas";
        listEdoNoNacional[7]="Chihuahua";
        listEdoNoNacional[8]="Distrito Federal";
        listEdoNoNacional[9]="Durango";
        listEdoNoNacional[10]="Guanajuato";
        listEdoNoNacional[11]="Guerrero";
        listEdoNoNacional[12]="Hidalgo";
        listEdoNoNacional[13]="Jalisco";
        listEdoNoNacional[14]="México";
        listEdoNoNacional[15]="Michoacán";
        listEdoNoNacional[16]="Morelos";
        listEdoNoNacional[17]="Nayarit";
        listEdoNoNacional[18]="Nuevo León";
        listEdoNoNacional[19]="Oaxaca";
        listEdoNoNacional[20]="Puebla";
        listEdoNoNacional[21]="Querétaro";
        listEdoNoNacional[22]="Quintana Roo";
        listEdoNoNacional[23]="San Luis Potosí";
        listEdoNoNacional[24]="Sinaloa";
        listEdoNoNacional[25]="Sonora";
        listEdoNoNacional[26]="Tabasco";
        listEdoNoNacional[27]="Tamaulipas";
        listEdoNoNacional[28]="Tlaxcala";
        listEdoNoNacional[29]="Veracruz";
        listEdoNoNacional[30]="Yucatán";
        listEdoNoNacional[31]="Zacatecas";
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

    public static final String[] ENTIDAD_ABR = {
        "NACIONAL", "AGS", "BC", "BCS", "CAMP", "COAH", "COL", "CHIS", "CHIH", "DF", "DGO",
            "GTO", "GRO", "HGO", "JAL", "MEX", "MICH", "MOR", "NAY", "NL", "OAX", "PUE",
            "QRO", "QROO", "SLP", "SIN", "SON", "TAB", "TAMPS", "TLAX", "VER", "YUC", "ZAC"
    };
}
