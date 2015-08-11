package mx.gob.conavi.sniiv.modelos;

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
