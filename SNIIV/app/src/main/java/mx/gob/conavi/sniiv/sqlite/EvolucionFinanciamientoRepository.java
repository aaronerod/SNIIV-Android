package mx.gob.conavi.sniiv.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Map;

import mx.gob.conavi.sniiv.modelos.Consulta;
import mx.gob.conavi.sniiv.modelos.EvolucionFinanciamiento;
import mx.gob.conavi.sniiv.modelos.EvolucionFinanciamientoResultado;

/**
 * Created by octavio.munguia on 25/09/2015.
 */
public class EvolucionFinanciamientoRepository implements Repository<EvolucionFinanciamiento> {
    private static final String TAG = FinanciamientoRepository.class.getSimpleName();
    private final AdminSQLiteOpenHelper dbHelper;

    public EvolucionFinanciamientoRepository(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    @Override
    public void saveAll(EvolucionFinanciamiento[] elementos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int entidad = 0;
        for (EvolucionFinanciamiento elemento : elementos) {
            Map<String, EvolucionFinanciamientoResultado> periodos = elemento.getPeriodos();
            for (Map.Entry<String, EvolucionFinanciamientoResultado> entry : periodos.entrySet()) {
                String anio = entry.getKey();
                EvolucionFinanciamientoResultado resultado = entry.getValue();
                Consulta[] meses = resultado.getMeses();

                for (int i = 0; i < meses.length; i++) {
                    if (meses[i] == null) {break;}
                    ContentValues values = new ContentValues();
                    values.put("cve_ent", entidad);
                    values.put("anio", anio);
                    values.put("mes", i);
                    values.put("acciones", meses[i].getAcciones());
                    values.put("monto", meses[i].getMonto());
                    db.insert(EvolucionFinanciamiento.TABLE, null, values);
                }
            }

            entidad++;
        }

        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(EvolucionFinanciamiento.TABLE, null, null);
        db.close();
    }

    // TODO: Generar modelo a partir de la consulta
    @Override
    public EvolucionFinanciamiento[] loadFromStorage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM " + EvolucionFinanciamiento.TABLE +
                " ORDER BY cve_ent, anio, mes";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int cve_ent = cursor.getInt(cursor.getColumnIndex("cve_ent"));
                int anio = cursor.getInt(cursor.getColumnIndex("anio"));
                int mes = cursor.getInt((cursor.getColumnIndex("mes")));
                long acciones = cursor.getLong(cursor.getColumnIndex("acciones"));
                double monto = cursor.getDouble(cursor.getColumnIndex("monto"));

                Log.v(TAG + " loadFromStorage", String.format("%2d %4d %2d %10d %12.2f", cve_ent, anio, mes, acciones, monto));
            } while (cursor.moveToNext());
        }

        return new EvolucionFinanciamiento[0];
    }

   /* // TODO: Completar consulta
    public EvolucionFinanciamiento consultaNacional() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM " + EvolucionFinanciamiento.TABLE +
                " WHERE cve_ent = 0";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int cve_ent = cursor.getInt(cursor.getColumnIndex("cve_ent"));
                int anio = cursor.getInt(cursor.getColumnIndex("anio"));
                int mes = cursor.getInt((cursor.getColumnIndex("mes")));
                long acciones = cursor.getLong(cursor.getColumnIndex("acciones"));
                double monto = cursor.getDouble(cursor.getColumnIndex("monto"));

                Log.v(TAG, String.format("%2d %4d %2d %10d %12.2f", cve_ent, anio, mes, acciones, monto));
            } while (cursor.moveToNext());
        }

        return new EvolucionFinanciamiento();
    }*/
}
