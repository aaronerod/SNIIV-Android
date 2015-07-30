package mx.gob.conavi.sniiv.Utils;


import java.util.ArrayList;

public class Utils {
   private ArrayList<String> listMenu;
   private ArrayList<String> listEstados;
   private String[] listEdo;


    public Utils()
    {
        setListMenu(new ArrayList<String>());
        getListMenu().add("Reporte General");
        getListMenu().add("Oferta");
        getListMenu().add("Demanda");

        setListEstados(new ArrayList<String>());
        populateEstados();

        setListEdo(new String[33]);
        populateEdos();




    }
    public void populateEdos(){
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



    public void populateEstados(){
        getListEstados().add("Nacional");
        getListEstados().add("Aguascalientes");
        getListEstados().add("Baja California");
        getListEstados().add("Baja California Sur");
        getListEstados().add("Campeche");
        getListEstados().add("Coahuila");
        getListEstados().add("Colima");
        getListEstados().add("Chiapas");
        getListEstados().add("Chihuahua");
        getListEstados().add("Distrito Federal");
        getListEstados().add("Durango");
        getListEstados().add("Guanajuato");
        getListEstados().add("Guerrero");
        getListEstados().add("Hidalgo");
        getListEstados().add("Jalisco");
        getListEstados().add("México");
        getListEstados().add("Michoacán");
        getListEstados().add("Morelos");
        getListEstados().add("Nayarit");
        getListEstados().add("Nuevo León");
        getListEstados().add("Oaxaca");
        getListEstados().add("Puebla");
        getListEstados().add("Querétaro");
        getListEstados().add("Quintana Roo");
        getListEstados().add("San Luis Potosí");
        getListEstados().add("Sinaloa");
        getListEstados().add("Sonora");
        getListEstados().add("Tabasco");
        getListEstados().add("Tamaulipas");
        getListEstados().add("Tlaxcala");
        getListEstados().add("Veracruz");
        getListEstados().add("Yucatán");
        getListEstados().add("Zacatecas");


    }
    public ArrayList<String> getListMenu() {
        return listMenu;
    }

    public void setListMenu(ArrayList<String> listMenu) {
        this.listMenu = listMenu;
    }

    public ArrayList<String> getListEstados() {
        return listEstados;
    }

    public void setListEstados(ArrayList<String> listEstados) {
        this.listEstados = listEstados;
    }

    public String[] getListEdo() {
        return listEdo;
    }

    public void setListEdo(String[] listEdo) {
        this.listEdo = listEdo;
    }
}
