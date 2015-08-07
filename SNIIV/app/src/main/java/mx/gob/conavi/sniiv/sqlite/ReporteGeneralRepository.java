package mx.gob.conavi.sniiv.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.modelos.ReporteGeneral;

/**
 * Created by admin on 31/07/15.
 */
public class ReporteGeneralRepository implements Repository<ReporteGeneral> {

    private final AdminSQLiteOpenHelper dbHelper;

    public ReporteGeneralRepository(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    public void saveAll(ReporteGeneral[] elementos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (ReporteGeneral elemento : elementos) {
            ContentValues values = new ContentValues();
            values.put("cve_ent", elemento.getCve_ent());
            values.put("acc_finan", elemento.getAcc_finan());
            values.put("mto_finan", elemento.getMto_finan());
            values.put("acc_subs", elemento.getAcc_subs());
            values.put("mto_subs", elemento.getMto_subs());
            values.put("vv", elemento.getVv());
            values.put("vr", elemento.getVr());

            db.insert(ReporteGeneral.TABLE, null, values);
        }

        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ReporteGeneral.TABLE, null, null);
        db.close();
    }

    public ReporteGeneral[] loadFromStorage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM " + ReporteGeneral.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<ReporteGeneral> datos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ReporteGeneral reporte = new ReporteGeneral();
                reporte.setCve_ent(cursor.getInt(cursor.getColumnIndex("cve_ent")));
                reporte.setAcc_finan(cursor.getLong(cursor.getColumnIndex("acc_finan")));
                reporte.setMto_finan(cursor.getLong((cursor.getColumnIndex("mto_finan"))));
                reporte.setAcc_subs(cursor.getLong(cursor.getColumnIndex("acc_subs")));
                reporte.setMto_subs(cursor.getLong(cursor.getColumnIndex("mto_subs")));
                reporte.setVv(cursor.getLong(cursor.getColumnIndex("vv")));
                reporte.setVr(cursor.getLong(cursor.getColumnIndex("vr")));

                datos.add(reporte);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return datos.toArray(new ReporteGeneral[0]);
    }

    public ReporteGeneral consultaNacional() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM (SELECT SUM(acc_finan) as acc_finan, " +
                "SUM(mto_finan) AS mto_finan, " +
                "SUM(acc_subs) AS acc_subs, " +
                "SUM(mto_subs) AS mto_subs, " +
                "SUM(vv) AS vv, " +
                "SUM(vr) AS vr " +
                "FROM " + ReporteGeneral.TABLE + ") T";

        Cursor cursor = db.rawQuery(selectQuery, null);
        ReporteGeneral reporte = new ReporteGeneral();

        if (cursor.moveToFirst()) {
            reporte.setAcc_finan(cursor.getInt(cursor.getColumnIndex("acc_finan")));
            reporte.setMto_finan(cursor.getLong(cursor.getColumnIndex("mto_finan")));
            reporte.setAcc_subs(cursor.getLong((cursor.getColumnIndex("acc_subs"))));
            reporte.setMto_subs(cursor.getLong(cursor.getColumnIndex("mto_subs")));
            reporte.setVv(cursor.getLong(cursor.getColumnIndex("vv")));
            reporte.setVr(cursor.getLong(cursor.getColumnIndex("vr")));
        }

        cursor.close();
        db.close();

        return reporte;
    }
}
