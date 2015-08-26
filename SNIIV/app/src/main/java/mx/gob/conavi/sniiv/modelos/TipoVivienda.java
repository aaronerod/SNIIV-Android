package mx.gob.conavi.sniiv.modelos;

import java.util.ArrayList;

/**
 * Created by admin on 06/08/15.
 */
public class TipoVivienda implements Modelo{
    private int cve_ent;
    private long horizontal;
    private long vertical;
    private long total;

    public static final String TABLE = "TipoVivienda";

    public TipoVivienda() {
    }

    public TipoVivienda(int cve_ent, long horizontal, long vertical, long total) {
        this.cve_ent = cve_ent;
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.total = total;
    }

    public int getCve_ent() {
        return cve_ent;
    }

    public void setCve_ent(int cve_ent) {
        this.cve_ent = cve_ent;
    }

    public long getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(long horizontal) {
        this.horizontal = horizontal;
    }

    public long getVertical() {
        return vertical;
    }

    public void setVertical(long vertical) {
        this.vertical = vertical;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "TipoVivienda{" +
                "cve_ent=" + cve_ent +
                ", horizontal=" + horizontal +
                ", vertical=" + vertical +
                ", total=" + total +
                '}';
    }



    @Override
    public long[] getValues() {
        long[] l=new long[]{
                horizontal,
                vertical,

        };
        return l;
    }

    @Override
    public ArrayList<String> getParties() {
        ArrayList<String> PARTIES = new ArrayList<>();
        PARTIES.add("Horizontal");
        PARTIES.add("Vertical");

        return PARTIES;
    }
}
