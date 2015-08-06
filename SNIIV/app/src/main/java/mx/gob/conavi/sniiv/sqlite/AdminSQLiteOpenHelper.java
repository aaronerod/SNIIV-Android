package mx.gob.conavi.sniiv.sqlite;

/**
 * Created by admin on 31/07/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import mx.gob.conavi.sniiv.modelos.ReporteGeneral;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "sniiv.db";

    public AdminSQLiteOpenHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public AdminSQLiteOpenHelper(Context context, String nombre, CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    private void init(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE ReporteGeneral(" +
                        "cve_ent INTEGER" +
                        ",acc_finan INTEGER" +
                        ",mto_finan INTEGER" +
                        ",acc_subs INTEGER" +
                        ",mto_subs INTEGER" +
                        ",vv INTEGER" +
                        ",vr Integer" +
                        ")");
        db.execSQL(
                "CREATE TABLE Fechas(" +
                        "fecha_finan TEXT" +
                        ",fecha_subs TEXT" +
                        ",fecha_vv TEXT" +
                        ")");
        db.execSQL(
                "CREATE TABLE AvanceObra(" +
                        "cve_ent INTEGER" +
                        ",viv_proc_m50 INTEGER" +
                        ",viv_proc_50_99 INTEGER" +
                        ",viv_term_rec INTEGER" +
                        ",viv_term_ant INTEGER" +
                        ",total INTEGER" +
                        ")");

        db.execSQL(
                "CREATE TABLE PCU(" +
                        "cve_ent INTEGER" +
                        ",u1 INTEGER" +
                        ",u2 INTEGER" +
                        ",u3 INTEGER" +
                        ",fc INTEGER" +
                        ",nd INTEGER" +
                        ",total INTEGER" +
                        ")");

        db.execSQL(
                "CREATE TABLE TipoVivienda(" +
                        "cve_ent INTEGER" +
                        ",horizontal INTEGER" +
                        ",vertical INTEGER" +
                        ",total INTEGER" +
                        ")");

        db.execSQL(
                "CREATE TABLE ValorVivienda(" +
                        "cve_ent INTEGER" +
                        ",economica INTEGER" +
                        ",popular INTEGER" +
                        ",tradicional INTEGER" +
                        ",media_residencial INTEGER" +
                        ",total INTEGER" +
                        ")");

        db.execSQL(
                "CREATE TABLE Financiamiento(" +
                        "cve_ent INTEGER" +
                        ",organismo TEXT" +
                        ",destino TEXT" +
                        ",agrupacion TEXT" +
                        ",acciones INTEGER" +
                        ",monto REAL" +
                        ")");

        db.execSQL(
                "CREATE TABLE Subsidio(" +
                        "cve_ent INTEGER" +
                        ",tipo_ee TEXT" +
                        ",modalidad TEXT" +
                        ",acciones INTEGER" +
                        ",monto REAL" +
                        ")");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        init(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        db.execSQL("DROP TABLE IF EXISTS " + ReporteGeneral.TABLE);
        db.execSQL("DROP TABLE IF EXISTS Fechas");
        db.execSQL("DROP TABLE IF EXISTS AvanceObra");
        db.execSQL("DROP TABLE IF EXISTS PCU");
        db.execSQL("DROP TABLE IF EXISTS TipoVivienda");
        db.execSQL("DROP TABLE IF EXISTS ValorVivienda");
        db.execSQL("DROP TABLE IF EXISTS Financiamiento");
        db.execSQL("DROP TABLE IF EXISTS Subsidio");
        init(db);
    }
}

