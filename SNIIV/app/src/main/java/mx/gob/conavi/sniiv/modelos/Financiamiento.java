package mx.gob.conavi.sniiv.modelos;

/**
 * Created by admin on 10/08/15.
 */
public class Financiamiento {
    private int cve_ent;
    private String organismo;
    private String destino;
    private String agrupacion;
    private long acciones;
    private double monto;

<<<<<<< HEAD
=======
    public static final String TABLE = "Financiamiento";

>>>>>>> origin/tabs
    public Financiamiento() {
    }

    public Financiamiento(int cve_ent, String organismo, String destino, String agrupacion, long acciones, double monto) {
        this.cve_ent = cve_ent;
        this.organismo = organismo;
        this.destino = destino;
        this.agrupacion = agrupacion;
        this.acciones = acciones;
        this.monto = monto;
    }

    public int getCve_ent() {
        return cve_ent;
    }

    public void setCve_ent(int cve_ent) {
        this.cve_ent = cve_ent;
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getAgrupacion() {
        return agrupacion;
    }

    public void setAgrupacion(String agrupacion) {
        this.agrupacion = agrupacion;
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
        return "Financiamiento{" +
                "cve_ent=" + cve_ent +
                ", organismo='" + organismo + '\'' +
                ", destino='" + destino + '\'' +
                ", agrupacion='" + agrupacion + '\'' +
                ", acciones=" + acciones +
                ", monto=" + monto +
                '}';
    }
}
