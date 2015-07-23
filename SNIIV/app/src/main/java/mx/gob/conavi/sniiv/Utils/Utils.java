package mx.gob.conavi.sniiv.Utils;


import java.util.ArrayList;

public class Utils {
   private ArrayList<String> listMenu;
   private ArrayList<String> listEstados;

    public Utils()
    {
        setListMenu(new ArrayList<String>());
        getListMenu().add("Reporte General");
        getListMenu().add("Oferta");
        getListMenu().add("Demanda");

        setListEstados(new ArrayList<String>());
        populateEstados();




    }

    public void populateEstados(){
        getListEstados().add("Nacional");
        getListEstados().add("Aguascalientes");
        getListEstados().add("Baja California");
        getListEstados().add("Baja California Sur");
        getListEstados().add("Campeche");
        getListEstados().add("Coahuila");
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
}
