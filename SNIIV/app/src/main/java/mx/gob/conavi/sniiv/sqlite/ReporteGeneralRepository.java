package mx.gob.conavi.sniiv.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.parsing.ReporteGeneral;

/**
 * Created by admin on 31/07/15.
 */
public class ReporteGeneralRepository implements Repository<ReporteGeneral> {

    private AdminSQLiteOpenHelper dbHelper;

    public ReporteGeneralRepository(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    public void save(ReporteGeneral reporte) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cve_ent", reporte.getCve_ent());
        values.put("acc_finan", reporte.getAcc_finan());
        values.put("mto_finan", reporte.getMto_finan());
        values.put("acc_subs", reporte.getAcc_subs());
        values.put("mto_subs", reporte.getMto_finan());
        values.put("vv", reporte.getVv());
        values.put("vr", reporte.getVr());

        db.insert(ReporteGeneral.TABLE, null, values);
        db.close();
    }

    public void saveAll(ReporteGeneral[] elementos) {
        for (ReporteGeneral r : elementos) {
            save(r);
        }
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
}
