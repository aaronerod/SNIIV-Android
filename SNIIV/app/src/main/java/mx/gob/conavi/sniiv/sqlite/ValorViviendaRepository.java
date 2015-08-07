package mx.gob.conavi.sniiv.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.modelos.ValorVivienda;

/**
 * Created by admin on 07/08/15.
 */
public class ValorViviendaRepository implements Repository<ValorVivienda> {
    private AdminSQLiteOpenHelper dbHelper;

    public ValorViviendaRepository(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    @Override
    public void saveAll(ValorVivienda[] elementos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (ValorVivienda elemento : elementos) {
            ContentValues values = new ContentValues();
            values.put("cve_ent", elemento.getCve_ent());
            values.put("economica", elemento.getEconomica());
            values.put("popular", elemento.getPopular());
            values.put("tradicional", elemento.getTradicional());
            values.put("media_residencial", elemento.getMedia_residencial());
            values.put("total", elemento.getTotal());

            db.insert(ValorVivienda.TABLE, null, values);
        }

        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ValorVivienda.TABLE, null, null);
        db.close();
    }

    @Override
    public ValorVivienda[] loadFromStorage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM " + ValorVivienda.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<ValorVivienda> datos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ValorVivienda dato = new ValorVivienda();
                dato.setCve_ent(cursor.getInt(cursor.getColumnIndex("cve_ent")));
                dato.setEconomica(cursor.getLong(cursor.getColumnIndex("economica")));
                dato.setPopular(cursor.getLong(cursor.getColumnIndex("popular")));
                dato.setTradicional(cursor.getLong(cursor.getColumnIndex("tradicional")));
                dato.setMedia_residencial(cursor.getLong(cursor.getColumnIndex("media_residencial")));
                dato.setTotal(cursor.getLong(cursor.getColumnIndex("total")));

                datos.add(dato);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return datos.toArray(new ValorVivienda[0]);
    }

    @Override
    public ValorVivienda consultaNacional() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM (SELECT SUM(economica) as economica, " +
                "SUM(popular) AS popular, " +
                "SUM(tradicional) AS tradicional, " +
                "SUM(media_residencial) AS media_residencial, " +
                "SUM(total) AS total " +
                "FROM " + ValorVivienda.TABLE + ") T";

        Cursor cursor = db.rawQuery(selectQuery, null);
        ValorVivienda elemento = new ValorVivienda();

        if (cursor.moveToFirst()) {
            elemento.setEconomica(cursor.getLong(cursor.getColumnIndex("economica")));
            elemento.setPopular(cursor.getLong(cursor.getColumnIndex("popular")));
            elemento.setTradicional(cursor.getLong(cursor.getColumnIndex("tradicional")));
            elemento.setMedia_residencial(cursor.getLong(cursor.getColumnIndex("media_residencial")));
            elemento.setTotal(cursor.getLong(cursor.getColumnIndex("total")));
        }

        cursor.close();
        db.close();

        return elemento;
    }
}
