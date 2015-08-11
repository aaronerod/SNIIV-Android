package mx.gob.conavi.sniiv.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.modelos.Fechas;

/**
 * Created by admin on 11/08/15.
 */
public class FechasRepository implements Repository<Fechas> {
    private final AdminSQLiteOpenHelper dbHelper;

    public FechasRepository(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    @Override
    public void saveAll(Fechas[] elementos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Fechas elemento : elementos) {
            ContentValues values = new ContentValues();
            values.put("fecha_finan", elemento.getFecha_finan());
            values.put("fecha_subs", elemento.getFecha_subs());
            values.put("fecha_vv", elemento.getFecha_vv());

            db.insert(Fechas.TABLE, null, values);
        }

        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Fechas.TABLE, null, null);
        db.close();
    }

    @Override
    public Fechas[] loadFromStorage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM " + Fechas.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Fechas> datos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Fechas fechas = new Fechas();
                fechas.setFecha_finan(cursor.getString(cursor.getColumnIndex("fecha_finan")));
                fechas.setFecha_subs(cursor.getString(cursor.getColumnIndex("fecha_subs")));
                fechas.setFecha_vv(cursor.getString((cursor.getColumnIndex("fecha_vv"))));

                datos.add(fechas);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return datos.toArray(new Fechas[0]);
    }
}
