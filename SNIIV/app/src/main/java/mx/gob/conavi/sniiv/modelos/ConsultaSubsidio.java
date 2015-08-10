package mx.gob.conavi.sniiv.modelos;

/**
 * Created by admin on 07/08/15.
 */
public class ConsultaSubsidio {
    private Consulta nueva;
    private Consulta usada;
    private Consulta autoproduccion;
    private Consulta mejoramiento;
    private Consulta lotes;
    private Consulta otros;
    private Consulta total;

    public ConsultaSubsidio() {
    }

    public Consulta getNueva() {
        return nueva;
    }

    public void setNueva(Consulta nueva) {
        this.nueva = nueva;
    }

    public Consulta getUsada() {
        return usada;
    }

    public void setUsada(Consulta usada) {
        this.usada = usada;
    }

    public Consulta getAutoproduccion() {
        return autoproduccion;
    }

    public void setAutoproduccion(Consulta autoproduccion) {
        this.autoproduccion = autoproduccion;
    }

    public Consulta getMejoramiento() {
        return mejoramiento;
    }

    public void setMejoramiento(Consulta mejoramiento) {
        this.mejoramiento = mejoramiento;
    }

    public Consulta getLotes() {
        return lotes;
    }

    public void setLotes(Consulta lotes) {
        this.lotes = lotes;
    }

    public Consulta getOtros() {
        return otros;
    }

    public void setOtros(Consulta otros) {
        this.otros = otros;
    }

    public Consulta getTotal() {
        return total;
    }

    public void setTotal(Consulta total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ConsultaSubsidio{" +
                "nueva=" + nueva +
                ", usada=" + usada +
                ", autoproduccion=" + autoproduccion +
                ", mejoramiento=" + mejoramiento +
                ", lotes=" + lotes +
                ", otros=" + otros +
                ", total=" + total +
                '}';
    }
}
