package mx.gob.conavi.sniiv.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.modelos.AvanceObra;

/**
 * Created by admin on 05/08/15.
 */
public class AvanceObraRepository implements Repository<AvanceObra> {
    private final AdminSQLiteOpenHelper dbHelper;

    public AvanceObraRepository(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    @Override
    public void saveAll(AvanceObra[] elementos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (AvanceObra elemento : elementos) {
            ContentValues values = new ContentValues();
            values.put("cve_ent", elemento.getCve_ent());
            values.put("viv_proc_m50", elemento.getViv_proc_m50());
            values.put("viv_proc_50_99", elemento.getViv_proc_50_99());
            values.put("viv_term_rec", elemento.getViv_term_rec());
            values.put("viv_term_ant", elemento.getViv_term_ant());
            values.put("total", elemento.getTotal());

            db.insert(AvanceObra.TABLE, null, values);
        }

        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(AvanceObra.TABLE, null, null);
        db.close();
    }

    @Override
    public AvanceObra[] loadFromStorage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM " + AvanceObra.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<AvanceObra> datos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                AvanceObra dato = new AvanceObra();
                dato.setCve_ent(cursor.getInt(cursor.getColumnIndex("cve_ent")));
                dato.setViv_proc_m50(cursor.getLong(cursor.getColumnIndex("viv_proc_m50")));
                dato.setViv_proc_50_99(cursor.getLong((cursor.getColumnIndex("viv_proc_50_99"))));
                dato.setViv_term_rec(cursor.getLong(cursor.getColumnIndex("viv_term_rec")));
                dato.setViv_term_ant(cursor.getLong(cursor.getColumnIndex("viv_term_ant")));
                dato.setTotal(cursor.getLong(cursor.getColumnIndex("total")));

                datos.add(dato);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return datos.toArray(new AvanceObra[0]);
    }

    public AvanceObra consultaNacional() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM (SELECT SUM(viv_proc_m50) as viv_proc_m50, " +
                "SUM(viv_proc_50_99) AS viv_proc_50_99, " +
                "SUM(viv_term_rec) AS viv_term_rec, " +
                "SUM(viv_term_ant) AS viv_term_ant, " +
                "SUM(total) AS total " +
                "FROM " + AvanceObra.TABLE + ") T";

        Cursor cursor = db.rawQuery(selectQuery, null);
        AvanceObra elemento = new AvanceObra();

        if (cursor.moveToFirst()) {
            elemento.setViv_proc_m50(cursor.getInt(cursor.getColumnIndex("viv_proc_m50")));
            elemento.setViv_proc_50_99(cursor.getLong(cursor.getColumnIndex("viv_proc_50_99")));
            elemento.setViv_term_rec(cursor.getLong((cursor.getColumnIndex("viv_term_rec"))));
            elemento.setViv_term_ant(cursor.getLong(cursor.getColumnIndex("viv_term_ant")));
            elemento.setTotal(cursor.getLong(cursor.getColumnIndex("total")));
        }

        cursor.close();
        db.close();

        return elemento;
    }
}
