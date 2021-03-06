package mx.gob.conavi.sniiv.modelos.demanda;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.modelos.Consulta;
import mx.gob.conavi.sniiv.modelos.Modelo;

/**
 * Created by admin on 10/08/15.
 */
public class ConsultaFinanciamiento implements Modelo {
    private FinanciamientoResultado viviendasNuevas;
    private FinanciamientoResultado viviendasUsadas;
    private FinanciamientoResultado mejoramientos;
    private FinanciamientoResultado otrosProgramas;
    private Consulta total;

    public ConsultaFinanciamiento() {
        viviendasNuevas = new FinanciamientoResultado();
        viviendasUsadas = new FinanciamientoResultado();
        mejoramientos = new FinanciamientoResultado();
        otrosProgramas = new FinanciamientoResultado();
        total = new Consulta();
    }

    public FinanciamientoResultado getViviendasNuevas() {
        return viviendasNuevas;
    }

    public void setViviendasNuevas(FinanciamientoResultado viviendasNuevas) {
        this.viviendasNuevas = viviendasNuevas;
    }

    public FinanciamientoResultado getViviendasUsadas() {
        return viviendasUsadas;
    }

    public void setViviendasUsadas(FinanciamientoResultado viviendasUsadas) {
        this.viviendasUsadas = viviendasUsadas;
    }

    public FinanciamientoResultado getMejoramientos() {
        return mejoramientos;
    }

    public void setMejoramientos(FinanciamientoResultado mejoramientos) {
        this.mejoramientos = mejoramientos;
    }

    public FinanciamientoResultado getOtrosProgramas() {
        return otrosProgramas;
    }

    public void setOtrosProgramas(FinanciamientoResultado otrosProgramas) {
        this.otrosProgramas = otrosProgramas;
    }

    public Consulta getTotal() {
        return total;
    }

    public void setTotal(Consulta total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ConsultaFinanciamiento{" +
                "viviendasNuevas=" + viviendasNuevas +
                ", viviendasUsadas=" + viviendasUsadas +
                ", mejoramientos=" + mejoramientos +
                ", otrosProgramas=" + otrosProgramas +
                ", total=" + total +
                '}';
    }

    @Override
    public long[] getValues() {
        long[] l=new long[]{
                (long)(viviendasNuevas.getCofinanciamiento().getMonto() +
                        viviendasNuevas.getCreditoIndividual().getMonto()),
                (long)(viviendasUsadas.getCofinanciamiento().getMonto() +
                        viviendasUsadas.getCreditoIndividual().getMonto()),
                (long)(mejoramientos.getCofinanciamiento().getMonto() +
                        mejoramientos.getCreditoIndividual().getMonto())
        };
        return l;
    }

    @Override
    public ArrayList<String> getParties() {
        ArrayList<String> parties = new ArrayList<>();
        parties.add("Viviendas Nuevas");
        parties.add("Viviendas Usadas");
        parties.add("Mejoramiento");
        return parties;
    }

    public long[] getAcciones() {
        return new long[] {
            viviendasNuevas.getCofinanciamiento().getAcciones(),
                viviendasNuevas.getCreditoIndividual().getAcciones(),
                viviendasUsadas.getCofinanciamiento().getAcciones(),
                viviendasUsadas.getCreditoIndividual().getAcciones(),
                mejoramientos.getCofinanciamiento().getAcciones(),
                mejoramientos.getCreditoIndividual().getAcciones(),
                otrosProgramas.getCreditoIndividual().getAcciones(),
                total.getAcciones()
        };
    }

    public double[] getMontos() {
        return new double[] {
                viviendasNuevas.getCofinanciamiento().getMonto(),
                viviendasNuevas.getCreditoIndividual().getMonto(),
                viviendasUsadas.getCofinanciamiento().getMonto(),
                viviendasUsadas.getCreditoIndividual().getMonto(),
                mejoramientos.getCofinanciamiento().getMonto(),
                mejoramientos.getCreditoIndividual().getMonto(),
                otrosProgramas.getCreditoIndividual().getMonto(),
                total.getMonto()
        };
    }
}
