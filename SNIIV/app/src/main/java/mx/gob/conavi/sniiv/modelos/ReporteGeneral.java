package mx.gob.conavi.sniiv.modelos;

/**
 * Created by admin on 31/07/15.
 */
public class ReporteGeneral {
    int cve_ent;
    long acc_finan;
    long mto_finan;
    long acc_subs;
    long mto_subs;
    long vv;
    long vr;

    public static final String TABLE = "ReporteGeneral";

    public ReporteGeneral() {
    }

    public ReporteGeneral(int cve_ent, long acc_finan, long mto_finan, long acc_subs, long mto_subs, long vv, long vr) {
        this.cve_ent = cve_ent;
        this.acc_finan = acc_finan;
        this.mto_finan = mto_finan;
        this.acc_subs = acc_subs;
        this.mto_subs = mto_subs;
        this.vv = vv;
        this.vr = vr;
    }

    public int getCve_ent() {
        return cve_ent;
    }

    public void setCve_ent(int cve_ent) {
        this.cve_ent = cve_ent;
    }

    public long getAcc_finan() {
        return acc_finan;
    }

    public void setAcc_finan(long acc_finan) {
        this.acc_finan = acc_finan;
    }

    public long getMto_finan() {
        return mto_finan;
    }

    public void setMto_finan(long mto_finan) {
        this.mto_finan = mto_finan;
    }

    public long getAcc_subs() {
        return acc_subs;
    }

    public void setAcc_subs(long acc_subs) {
        this.acc_subs = acc_subs;
    }

    public long getMto_subs() {
        return mto_subs;
    }

    public void setMto_subs(long mto_subs) {
        this.mto_subs = mto_subs;
    }

    public long getVv() {
        return vv;
    }

    public void setVv(long vv) {
        this.vv = vv;
    }

    public long getVr() {
        return vr;
    }

    public void setVr(long vr) {
        this.vr = vr;
    }

    @Override
    public String toString() {
        return "ReporteGeneral{" +
                "cve_ent=" + cve_ent +
                ", acc_finan=" + acc_finan +
                ", mto_finan=" + mto_finan +
                ", acc_subs=" + acc_subs +
                ", mto_subs=" + mto_subs +
                ", vv=" + vv +
                ", vr=" + vr +
                '}';
    }
}