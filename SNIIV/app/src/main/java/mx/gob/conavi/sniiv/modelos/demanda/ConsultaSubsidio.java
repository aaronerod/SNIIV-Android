package mx.gob.conavi.sniiv.modelos.demanda;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.modelos.Consulta;
import mx.gob.conavi.sniiv.modelos.Modelo;

/**
 * Created by admin on 07/08/15.
 */
public class ConsultaSubsidio implements Modelo {
    private Consulta nueva;
    private Consulta usada;
    private Consulta autoproduccion;
    private Consulta mejoramiento;
    private Consulta lotes;
    private Consulta otros;
    private Consulta total;

    public ConsultaSubsidio() {
        nueva = new Consulta();
        usada = new Consulta();
        autoproduccion = new Consulta();
        mejoramiento = new Consulta();
        lotes = new Consulta();
        otros = new Consulta();
        total = new Consulta();
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

    @Override
    public long[] getValues() {
        long[] l=new long[]{
                (long)nueva.getMonto(),
                (long)usada.getMonto() ,
                (long)autoproduccion.getMonto(),
                (long)mejoramiento.getMonto()
        };
        return l;
    }

    @Override
    public ArrayList<String> getParties() {
        ArrayList<String> parties = new ArrayList<>();
        parties.add("Nueva");
        parties.add("Usada");
        parties.add("Autoproducción");
        parties.add("Mejoramiento");
        return parties;
    }

    public long[] getAcciones() {
        return new long[] {
                nueva.getAcciones(),
                usada.getAcciones() ,
                autoproduccion.getAcciones(),
                mejoramiento.getAcciones(),
                lotes.getAcciones(),
                otros.getAcciones(),
                total.getAcciones()
        };
    }

    public double[] getMontos() {
        return new double[] {
                nueva.getMonto(),
                usada.getMonto() ,
                autoproduccion.getMonto(),
                mejoramiento.getMonto(),
                lotes.getMonto(),
                otros.getMonto(),
                total.getMonto()
        };
    }
}
