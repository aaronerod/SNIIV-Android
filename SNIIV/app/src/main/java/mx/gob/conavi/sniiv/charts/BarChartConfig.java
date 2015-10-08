package mx.gob.conavi.sniiv.charts;

import java.util.ArrayList;

/**
 * Created by octavio.munguia on 08/10/2015.
 */
public class BarChartConfig {
    private ArrayList<String> parties;
    private ArrayList<String> xValues;
    private ArrayList<float[]> yValues;
    private String configuracion;
    private String description;
    private boolean showAcciones;

    public ArrayList<String> getParties() {
        return parties;
    }

    public void setParties(ArrayList<String> parties) {
        this.parties = parties;
    }

    public ArrayList<String> getxValues() {
        return xValues;
    }

    public void setxValues(ArrayList<String> xValues) {
        this.xValues = xValues;
    }

    public ArrayList<float[]> getyValues() {
        return yValues;
    }

    public void setyValues(ArrayList<float[]> yValues) {
        this.yValues = yValues;
    }

    public String getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(String configuracion) {
        this.configuracion = configuracion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isShowAcciones() {
        return showAcciones;
    }

    public void setShowAcciones(boolean showAcciones) {
        this.showAcciones = showAcciones;
    }
}
