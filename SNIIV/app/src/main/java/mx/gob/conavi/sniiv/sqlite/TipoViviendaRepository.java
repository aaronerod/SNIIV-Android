package mx.gob.conavi.sniiv.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.modelos.TipoVivienda;

/**
 * Created by admin on 06/08/15.
 */
public class TipoViviendaRepository implements Repository<TipoVivienda> {
    private final AdminSQLiteOpenHelper dbHelper;

    public TipoViviendaRepository(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    @Override
    public void saveAll(TipoVivienda[] elementos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (TipoVivienda elemento : elementos) {
            ContentValues values = new ContentValues();
            values.put("cve_ent", elemento.getCve_ent());
            values.put("horizontal", elemento.getHorizontal());
            values.put("vertical", elemento.getVertical());
            values.put("total", elemento.getTotal());

            db.insert(TipoVivienda.TABLE, null, values);
        }

        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TipoVivienda.TABLE, null, null);
        db.close();
    }

    @Override
    public TipoVivienda[] loadFromStorage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM " + TipoVivienda.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<TipoVivienda> datos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                TipoVivienda dato = new TipoVivienda();
                dato.setCve_ent(cursor.getInt(cursor.getColumnIndex("cve_ent")));
                dato.setHorizontal(cursor.getLong(cursor.getColumnIndex("horizontal")));
                dato.setVertical(cursor.getLong(cursor.getColumnIndex("vertical")));
                dato.setTotal(cursor.getLong(cursor.getColumnIndex("total")));

                datos.add(dato);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return datos.toArray(new TipoVivienda[0]);
    }

    public TipoVivienda consultaNacional() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM (SELECT SUM(horizontal) as horizontal, " +
                "SUM(vertical) AS vertical, " +
                "SUM(total) AS total " +
                "FROM " + TipoVivienda.TABLE + ") T";

        Cursor cursor = db.rawQuery(selectQuery, null);
        TipoVivienda elemento = new TipoVivienda();

        if (cursor.moveToFirst()) {
            elemento.setHorizontal(cursor.getLong(cursor.getColumnIndex("horizontal")));
            elemento.setVertical(cursor.getLong(cursor.getColumnIndex("vertical")));
            elemento.setTotal(cursor.getLong(cursor.getColumnIndex("total")));
        }

        cursor.close();
        db.close();

        return elemento;
    }
}
