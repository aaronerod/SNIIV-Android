package mx.gob.conavi.sniiv.sqlite;

/**
 * Created by admin on 31/07/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import mx.gob.conavi.sniiv.modelos.AvanceObra;
import mx.gob.conavi.sniiv.modelos.PCU;
import mx.gob.conavi.sniiv.modelos.ReporteGeneral;
import mx.gob.conavi.sniiv.modelos.TipoVivienda;
import mx.gob.conavi.sniiv.modelos.ValorVivienda;

class AdminSQLiteOpenHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 6;
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
                "CREATE TABLE TipoEntidadEjecutora(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                        ",descripcion TEXT" +
                        ")");

        db.execSQL(
                "CREATE TABLE Modalidad(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                        ",descripcion TEXT" +
                        ")");

        db.execSQL(
                "CREATE TABLE Subsidio(" +
                        "cve_ent INTEGER" +
                        ",tipo_ee_id INTEGER" +
                        ",modalidad_id INTEGER" +
                        ",acciones INTEGER" +
                        ",monto REAL" +
                        ",FOREIGN KEY(tipo_ee_id) REFERENCES TipoEntidadEjecutora(id)" +
                        ",FOREIGN KEY(modalidad_id) REFERENCES Modalidad(id)" +
                        ")");

        db.execSQL(
                "CREATE TABLE Organismo(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                        ",descripcion TEXT" +
                        ")");

        db.execSQL(
                "CREATE TABLE Destino(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                        ",descripcion TEXT" +
                        ")");

        db.execSQL(
                "CREATE TABLE Agrupacion(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                        ",descripcion TEXT" +
                        ")");

        db.execSQL(
                "CREATE TABLE Financiamiento(" +
                        "cve_ent INTEGER" +
                        ",organismo_id INTEGER" +
                        ",destino_id INTEGER" +
                        ",agrupacion_id INTEGER" +
                        ",acciones INTEGER" +
                        ",monto REAL" +
                        ",FOREIGN KEY(organismo_id) REFERENCES Organismo(id)" +
                        ",FOREIGN KEY(destino_id) REFERENCES Destino(id)" +
                        ",FOREIGN KEY(agrupacion_id) REFERENCES Agrupacion(id)" +
                        ")");

        insertTipoEntidadEjecutora(db);
        insertModalidad(db);
        insertOrganismo(db);
        insertDestino(db);
        insertAgrupacion(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        init(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        db.execSQL("DROP TABLE IF EXISTS " + ReporteGeneral.TABLE);
        db.execSQL("DROP TABLE IF EXISTS Fechas");
        db.execSQL("DROP TABLE IF EXISTS " + AvanceObra.TABLE );
        db.execSQL("DROP TABLE IF EXISTS " + PCU.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TipoVivienda.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ValorVivienda.TABLE);
        db.execSQL("DROP TABLE IF EXISTS TipoEntidadEjecutora");
        db.execSQL("DROP TABLE IF EXISTS Modalidad");
        db.execSQL("DROP TABLE IF EXISTS Subsidio");
        db.execSQL("DROP TABLE IF EXISTS Organismo");
        db.execSQL("DROP TABLE IF EXISTS Destino");
        db.execSQL("DROP TABLE IF EXISTS Agrupacion");
        db.execSQL("DROP TABLE IF EXISTS Financiamiento");
        init(db);
    }

    private void insertTipoEntidadEjecutora(SQLiteDatabase db) {
        db.execSQL("INSERT INTO TipoEntidadEjecutora(descripcion) VALUES ('INFONAVIT')");
        db.execSQL("INSERT INTO TipoEntidadEjecutora(descripcion) VALUES ('FOVISSSTE')");
        db.execSQL("INSERT INTO TipoEntidadEjecutora(descripcion) VALUES ('FUERZAS ARMADAS')");
        db.execSQL("INSERT INTO TipoEntidadEjecutora(descripcion) VALUES ('OREVIS')");
        db.execSQL("INSERT INTO TipoEntidadEjecutora(descripcion) VALUES ('PROGRAMA DE MEJORAMIENTO " +
                "DE UNIDAD HABITACIONAL')");
        db.execSQL("INSERT INTO TipoEntidadEjecutora(descripcion) VALUES ('OTRAS ENTIDADES EJECUTORAS')");
    }

    private void insertModalidad(SQLiteDatabase db) {
        db.execSQL("INSERT INTO Modalidad(descripcion) VALUES ('Nueva')");
        db.execSQL("INSERT INTO Modalidad(descripcion) VALUES ('Usada')");
        db.execSQL("INSERT INTO Modalidad(descripcion) VALUES ('Autoproducción')");
        db.execSQL("INSERT INTO Modalidad(descripcion) VALUES ('Mejoramiento')");
        db.execSQL("INSERT INTO Modalidad(descripcion) VALUES ('Lotes con servicio')");
        db.execSQL("INSERT INTO Modalidad(descripcion) VALUES ('Otros')");
    }

    private void insertOrganismo(SQLiteDatabase db) {
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('FOVISSSTE')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('INFONAVIT')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('SHF (FONDEO)')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('BANJERCITO')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('BANCA (CNBV)')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('CONAVI')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('FONHAPO')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('INDIVI')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('COVEG')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('IMEVIS')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('IVEM')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('ISSFAM')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('CFE')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('PEMEX')");
        db.execSQL("INSERT INTO Organismo(descripcion) VALUES ('HABITAT MEXICO')");
    }

    private void insertDestino(SQLiteDatabase db) {
        db.execSQL("INSERT INTO Destino(descripcion) VALUES ('Viviendas Nuevas')");
        db.execSQL("INSERT INTO Destino(descripcion) VALUES ('Viviendas Usadas')");
        db.execSQL("INSERT INTO Destino(descripcion) VALUES ('Mejoramientos')");
        db.execSQL("INSERT INTO Destino(descripcion) VALUES ('Otros programas')");
    }

    private void insertAgrupacion(SQLiteDatabase db) {
        db.execSQL("INSERT INTO Agrupacion(descripcion) VALUES ('Cofinanciamientos y Subsidios')");
        db.execSQL("INSERT INTO Agrupacion(descripcion) VALUES ('Crédito Individual')");
    }
}

