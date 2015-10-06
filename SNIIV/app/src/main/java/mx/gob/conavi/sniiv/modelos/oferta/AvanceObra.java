package mx.gob.conavi.sniiv.modelos.oferta;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.modelos.Modelo;

/**
 * Created by admin on 05/08/15.
 */
public class AvanceObra implements Modelo {
    private int cve_ent;
    private long viv_proc_m50;
    private long viv_proc_50_99;
    private long viv_term_rec;
    private long viv_term_ant;
    private long total;

    public static final String TABLE = "AvanceObra";


    public AvanceObra() {
    }

    public AvanceObra(int cve_ent, long viv_proc_m50, long viv_proc_50_99, long viv_term_rec, long viv_term_ant, long total) {
        this.cve_ent = cve_ent;
        this.viv_proc_m50 = viv_proc_m50;
        this.viv_proc_50_99 = viv_proc_50_99;
        this.viv_term_rec = viv_term_rec;
        this.viv_term_ant = viv_term_ant;
        this.total = total;
    }

    public int getCve_ent() {
        return cve_ent;
    }

    public void setCve_ent(int cve_ent) {
        this.cve_ent = cve_ent;
    }

    public long getViv_proc_m50() {
        return viv_proc_m50;
    }

    public void setViv_proc_m50(long viv_proc_m50) {
        this.viv_proc_m50 = viv_proc_m50;
    }

    public long getViv_proc_50_99() {
        return viv_proc_50_99;
    }

    public void setViv_proc_50_99(long viv_proc_50_99) {
        this.viv_proc_50_99 = viv_proc_50_99;
    }

    public long getViv_term_rec() {
        return viv_term_rec;
    }

    public void setViv_term_rec(long viv_term_rec) {
        this.viv_term_rec = viv_term_rec;
    }

    public long getViv_term_ant() {
        return viv_term_ant;
    }

    public void setViv_term_ant(long viv_term_ant) {
        this.viv_term_ant = viv_term_ant;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public long[] getValues() {
        long[] l=new long[]{
        viv_proc_m50,
        viv_proc_50_99,
        viv_term_rec,
        viv_term_ant
        };
        return l;
    }

    @Override
    public ArrayList<String> getParties() {
        ArrayList<String> parties = new ArrayList<>();
        parties.add("Hasta 50%");
        parties.add("Hasta 99%");
        parties.add("Recientes");
        parties.add("Antiguas");
        return parties;
    }
}
