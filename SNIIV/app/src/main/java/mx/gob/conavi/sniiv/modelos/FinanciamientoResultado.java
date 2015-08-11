package mx.gob.conavi.sniiv.modelos;

/**
 * Created by admin on 10/08/15.
 */
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

    @Override
    public String toString() {
        return "FinanciamientoResultado{" +
                "cofinanciamiento=" + cofinanciamiento +
                ", creditoIndividual=" + creditoIndividual +
                '}';
    }
}