package mx.gob.conavi.sniiv.modelos;

import android.graphics.AvoidXfermode;

import java.util.ArrayList;

/**
 * Created by admin on 07/08/15.
 */
public class ValorVivienda implements Modelo{
    private int cve_ent;
    private long economica;
    private long popular;
    private long tradicional;
    private long media_residencial;
    private long total;

    public static final String TABLE = "ValorVivienda";

    public ValorVivienda() {
    }

    public ValorVivienda(int cve_ent, long economica, long popular, long tradicional, long media_residencial, long total) {
        this.cve_ent = cve_ent;
        this.economica = economica;
        this.popular = popular;
        this.tradicional = tradicional;
        this.media_residencial = media_residencial;
        this.total = total;
    }

    public int getCve_ent() {
        return cve_ent;
    }

    public void setCve_ent(int cve_ent) {
        this.cve_ent = cve_ent;
    }

    public long getEconomica() {
        return economica;
    }

    public void setEconomica(long economica) {
        this.economica = economica;
    }

    public long getPopular() {
        return popular;
    }

    public void setPopular(long popular) {
        this.popular = popular;
    }

    public long getTradicional() {
        return tradicional;
    }

    public void setTradicional(long tradicional) {
        this.tradicional = tradicional;
    }

    public long getMedia_residencial() {
        return media_residencial;
    }

    public void setMedia_residencial(long media_residencial) {
        this.media_residencial = media_residencial;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ValorVivienda{" +
                "cve_ent=" + cve_ent +
                ", economica=" + economica +
                ", popular=" + popular +
                ", tradicional=" + tradicional +
                ", media_residencial=" + media_residencial +
                ", total=" + total +
                '}';
    }

    @Override
    public long[] getValues() {
        long[] l=new long[]{
            //    economica,
                popular,
                tradicional,
                media_residencial

        };
        return l;
    }

    @Override
    public ArrayList<String> getParties() {
        ArrayList<String> PARTIES = new ArrayList<>();
      //  PARTIES.add("Económica");
        PARTIES.add("Popular");
        PARTIES.add("Tradicional");
        PARTIES.add("Media Residencial");
        //  PARTIES.add("No disponible");
        return PARTIES;
    }
}
