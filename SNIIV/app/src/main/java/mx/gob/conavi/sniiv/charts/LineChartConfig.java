package mx.gob.conavi.sniiv.charts;

import java.util.ArrayList;

/**
 * Created by octavio.munguia on 02/10/2015.
 */
public class LineChartConfig {
    private ArrayList<String> parties;
    private ArrayList<double[]> yValues;
    private int[] xValues;
    private String configuracion;
    private String description;
    private boolean showAcciones;

    public ArrayList<String> getParties() {
        return parties;
    }

    public void setParties(ArrayList<String> parties) {
        this.parties = parties;
    }

    public ArrayList<double[]> getyValues() {
        return yValues;
    }

    public void setyValues(ArrayList<double[]> yValues) {
        this.yValues = yValues;
    }

    public int[] getxValues() {
        return xValues;
    }

    public void setxValues(int[] xValues) {
        this.xValues = xValues;
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
