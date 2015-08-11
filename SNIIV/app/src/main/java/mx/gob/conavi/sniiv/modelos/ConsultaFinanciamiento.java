package mx.gob.conavi.sniiv.modelos;

/**
 * Created by admin on 10/08/15.
 */
public class ConsultaFinanciamiento {
    private FinanciamientoResultado viviendasNuevas;
    private FinanciamientoResultado viviendasUsadas;
    private FinanciamientoResultado mejoramientos;
    private FinanciamientoResultado otrosProgramas;
<<<<<<< HEAD
    private FinanciamientoResultado total;
=======
    private Consulta total;
>>>>>>> origin/tabs

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

<<<<<<< HEAD
    public FinanciamientoResultado getTotal() {
        return total;
    }

    public void setTotal(FinanciamientoResultado total) {
        this.total = total;
    }

    public class FinanciamientoResultado {
        private Consulta cofinanciamiento;
        private Consulta creditoIndividual;

        public Consulta getCofinanciamiento() {
            return cofinanciamiento;
        }

        public void setCofinanciamiento(Consulta cofinanciamiento) {
            this.cofinanciamiento = cofinanciamiento;
        }

        public Consulta getCreditoIndividual() {
            return creditoIndividual;
        }

        public void setCreditoIndividual(Consulta creditoIndividual) {
            this.creditoIndividual = creditoIndividual;
        }
=======
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
>>>>>>> origin/tabs
    }
}
