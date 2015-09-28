package mx.gob.conavi.sniiv.modelos.demanda;

/**
 * Created by admin on 07/08/15.
 */
public class Subsidio {
    private int cve_ent;
    private String tipo_ee;
    private String modalidad;
    private long acciones;
    private double monto;

    public static final String TABLE = "Subsidio";

    public Subsidio() {
    }

    public Subsidio(int cve_ent, String tipo_ee, String modalidad, long acciones, double monto) {
        this.cve_ent = cve_ent;
        this.tipo_ee = tipo_ee;
        this.modalidad = modalidad;
        this.acciones = acciones;
        this.monto = monto;
    }

    public int getCve_ent() {
        return cve_ent;
    }

    public void setCve_ent(int cve_ent) {
        this.cve_ent = cve_ent;
    }

    public String getTipo_ee() {
        return tipo_ee;
    }

    public void setTipo_ee(String tipo_ee) {
        this.tipo_ee = tipo_ee;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public long getAcciones() {
        return acciones;
    }

    public void setAcciones(long acciones) {
        this.acciones = acciones;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "Subsidio{" +
                "cve_ent=" + cve_ent +
                ", tipo_ee='" + tipo_ee + '\'' +
                ", modalidad='" + modalidad + '\'' +
                ", acciones=" + acciones +
                ", monto=" + monto +
                '}';
    }
}
