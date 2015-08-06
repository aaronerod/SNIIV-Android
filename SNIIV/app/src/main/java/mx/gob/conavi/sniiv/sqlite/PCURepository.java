package mx.gob.conavi.sniiv.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.modelos.PCU;

/**
 * Created by admin on 06/08/15.
 */
public class PCURepository implements Repository<PCU> {
    private AdminSQLiteOpenHelper dbHelper;

    public PCURepository(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    @Override
    public void saveAll(PCU[] elementos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (PCU elemento : elementos) {
            ContentValues values = new ContentValues();
            values.put("cve_ent", elemento.getCve_ent());
            values.put("u1", elemento.getU1());
            values.put("u2", elemento.getU2());
            values.put("u3", elemento.getU3());
            values.put("fc", elemento.getFc());
            values.put("nd", elemento.getNd());
            values.put("total", elemento.getTotal());

            db.insert(PCU.TABLE, null, values);
        }

        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(PCU.TABLE, null, null);
        db.close();
    }

    @Override
    public PCU[] loadFromStorage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM " + PCU.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<PCU> datos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                PCU dato = new PCU();
                dato.setCve_ent(cursor.getInt(cursor.getColumnIndex("cve_ent")));
                dato.setU1(cursor.getLong(cursor.getColumnIndex("u1")));
                dato.setU2(cursor.getLong(cursor.getColumnIndex("u2")));
                dato.setU3(cursor.getLong(cursor.getColumnIndex("u3")));
                dato.setFc(cursor.getLong(cursor.getColumnIndex("fc")));
                dato.setNd(cursor.getLong(cursor.getColumnIndex("nd")));
                dato.setTotal(cursor.getLong(cursor.getColumnIndex("total")));

                datos.add(dato);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return datos.toArray(new PCU[0]);
    }

    @Override
    public PCU consultaNacional() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM (SELECT SUM(u1) as u1, " +
                "SUM(u2) AS u2, " +
                "SUM(u3) AS u3, " +
                "SUM(fc) AS fc, " +
                "SUM(nd) AS nd, " +
                "SUM(total) AS total " +
                "FROM " + PCU.TABLE + ") T";

        Cursor cursor = db.rawQuery(selectQuery, null);
        PCU elemento = new PCU();

        if (cursor.moveToFirst()) {
            elemento.setU1(cursor.getLong(cursor.getColumnIndex("u1")));
            elemento.setU2(cursor.getLong(cursor.getColumnIndex("u2")));
            elemento.setU3(cursor.getLong(cursor.getColumnIndex("u3")));
            elemento.setFc(cursor.getLong(cursor.getColumnIndex("fc")));
            elemento.setNd(cursor.getLong(cursor.getColumnIndex("nd")));
            elemento.setTotal(cursor.getLong(cursor.getColumnIndex("total")));
        }

        cursor.close();
        db.close();

        return elemento;
    }
}
