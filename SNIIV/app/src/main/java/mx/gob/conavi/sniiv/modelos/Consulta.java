package mx.gob.conavi.sniiv.modelos;

/**
 * Created by admin on 07/08/15.
 */
public class Consulta {
    private long acciones;
    private double monto;

    public Consulta() {
    }

    public Consulta(long acciones, double monto) {
        this.acciones = acciones;
        this.monto = monto;
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
}
