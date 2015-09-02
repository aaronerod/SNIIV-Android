package mx.gob.conavi.sniiv.modelos;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 11/08/15.
 */
public class Fechas {
    public static final String TABLE = "Fechas";
    private String fecha_finan;
    private String fecha_subs;
    private String fecha_vv;

    public Fechas(String fecha_finan, String fecha_subs, String fecha_vv) {
        this.fecha_finan = fecha_finan;
        this.fecha_subs = fecha_subs;
        this.fecha_vv = fecha_vv;
    }

    public Fechas() {
    }

    public Fechas(JSONObject jsonFechas) throws JSONException {
        this.fecha_finan = jsonFechas.getString("fecha_finan");
        this.fecha_subs = jsonFechas.getString("fecha_subs");
        this.fecha_vv = jsonFechas.getString("fecha_vv");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fechas fechas = (Fechas) o;

        if (fecha_finan != null ? !fecha_finan.equals(fechas.fecha_finan) : fechas.fecha_finan != null)
            return false;
        if (fecha_subs != null ? !fecha_subs.equals(fechas.fecha_subs) : fechas.fecha_subs != null)
            return false;
        return !(fecha_vv != null ? !fecha_vv.equals(fechas.fecha_vv) : fechas.fecha_vv != null);

    }

    @Override
    public int hashCode() {
        int result = fecha_finan != null ? fecha_finan.hashCode() : 0;
        result = 31 * result + (fecha_subs != null ? fecha_subs.hashCode() : 0);
        result = 31 * result + (fecha_vv != null ? fecha_vv.hashCode() : 0);
        return result;
    }

    public String getFecha_finan() {
        return fecha_finan;
    }

    public void setFecha_finan(String fecha_finan) {
        this.fecha_finan = fecha_finan;
    }

    public String getFecha_subs() {
        return fecha_subs;
    }

    public void setFecha_subs(String fecha_subs) {
        this.fecha_subs = fecha_subs;
    }

    public String getFecha_vv() {
        return fecha_vv;
    }

    public void setFecha_vv(String fecha_vv) {
        this.fecha_vv = fecha_vv;
    }

    @Override
    public String toString() {
        return "Fechas{" +
                "fecha_finan='" + fecha_finan + '\'' +
                ", fecha_subs='" + fecha_subs + '\'' +
                ", fecha_vv='" + fecha_vv + '\'' +
                '}';
    }
}
