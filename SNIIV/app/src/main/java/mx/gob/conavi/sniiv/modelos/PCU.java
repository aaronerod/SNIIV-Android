package mx.gob.conavi.sniiv.modelos;

import java.util.ArrayList;

/**
 * Created by admin on 06/08/15.
 */
public class PCU implements Modelo {
    private int cve_ent;
    private long u1;
    private long u2;
    private long u3;
    private long fc;
    private long nd;
    private long total;

    public static final String TABLE = "PCU";

    public PCU() {
    }

    public PCU(int cve_ent, long u1, long u2, long u3, long fc, long nd, long total) {
        this.cve_ent = cve_ent;
        this.u1 = u1;
        this.u2 = u2;
        this.u3 = u3;
        this.fc = fc;
        this.nd = nd;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getCve_ent() {
        return cve_ent;
    }

    public void setCve_ent(int cve_ent) {
        this.cve_ent = cve_ent;
    }

    public long getU1() {
        return u1;
    }

    public void setU1(long u1) {
        this.u1 = u1;
    }

    public long getU2() {
        return u2;
    }

    public void setU2(long u2) {
        this.u2 = u2;
    }

    public long getU3() {
        return u3;
    }

    public void setU3(long u3) {
        this.u3 = u3;
    }

    public long getFc() {
        return fc;
    }

    public void setFc(long fc) {
        this.fc = fc;
    }

    public long getNd() {
        return nd;
    }

    public void setNd(long nd) {
        this.nd = nd;
    }

    @Override
    public String toString() {
        return "PCU{" +
                "cve_ent=" + cve_ent +
                ", u1=" + u1 +
                ", u2=" + u2 +
                ", u3=" + u3 +
                ", fc=" + fc +
                ", nd=" + nd +
                ", total=" + total +
                '}';
    }

    @Override
    public long[] getValues() {
        long[] l=new long[]{
                u1,
                u2,
                u3,
                fc
        };
        return l;
    }

    @Override
    public ArrayList<String> getParties() {
        ArrayList<String> PARTIES = new ArrayList<>();
        PARTIES.add("Perímetro U1");
        PARTIES.add("Perímetro U2");
        PARTIES.add("Perímetro U3");
        PARTIES.add("Fuera de Contorno");
      //  PARTIES.add("No disponible");
        return PARTIES;
    }
}
