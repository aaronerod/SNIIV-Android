package mx.gob.conavi.sniiv.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.modelos.Consulta;
import mx.gob.conavi.sniiv.modelos.ConsultaSubsidio;
import mx.gob.conavi.sniiv.modelos.Subsidio;

/**
 * Created by admin on 07/08/15.
 */
public class SubsidioRepository implements Repository<Subsidio> {
    private static final String TAG = SubsidioRepository.class.getSimpleName();
    private final AdminSQLiteOpenHelper dbHelper;
    private static String queryEntidad = "SELECT id FROM TipoEntidadEjecutora WHERE descripcion = UPPER(?)";
    private static String queryModalidad = "SELECT id FROM Modalidad WHERE descripcion = ?";

    public SubsidioRepository(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    @Override
    public void saveAll(Subsidio[] elementos) {
        SQLiteDatabase dbw = dbHelper.getWritableDatabase();
        SQLiteDatabase dbr = dbHelper.getReadableDatabase();

        String query = "INSERT INTO Subsidio(cve_ent, tipo_ee_id, modalidad_id, acciones, monto)" +
                "VALUES(?, ?, ?, ?, ?)";

        for (Subsidio elemento : elementos) {
            dbw.execSQL(query, new String[]{
                    String.valueOf(elemento.getCve_ent()),
                    obtenerEntidadEjecutora(dbr, elemento),
                    obtenerModalidad(dbr, elemento),
                    String.valueOf(elemento.getAcciones()),
                    String.valueOf(elemento.getMonto())
            });
        }

        dbw.close();
        dbr.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Subsidio.TABLE, null, null);
        db.close();
    }

    @Override
    public Subsidio[] loadFromStorage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT cve_ent, T.descripcion tipo_ee," +
                "M.descripcion modalidad, acciones, monto FROM " + Subsidio.TABLE + " S " +
                " LEFT JOIN TipoEntidadEjecutora T ON S.tipo_ee_id = T.id" +
                " LEFT JOIN Modalidad M ON S.modalidad_id = M.id";

        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Subsidio> datos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Subsidio dato = new Subsidio();
                dato.setCve_ent(cursor.getInt(cursor.getColumnIndex("cve_ent")));
                dato.setTipo_ee(cursor.getString(cursor.getColumnIndex("tipo_ee")));
                dato.setModalidad(cursor.getString(cursor.getColumnIndex("modalidad")));
                dato.setAcciones(cursor.getLong(cursor.getColumnIndex("acciones")));
                dato.setMonto(cursor.getDouble(cursor.getColumnIndex("monto")));

                datos.add(dato);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return datos.toArray(new Subsidio[0]);
    }

    public ConsultaSubsidio consultaNacional() {
        return consulta();
    }

    public ConsultaSubsidio consultaEntidad(int entidad) {
        return consulta(String.valueOf(entidad));
    }

    private String obtenerEntidadEjecutora(SQLiteDatabase dbr, Subsidio elemento) {
        Cursor cursor = dbr.rawQuery(queryEntidad, new String[]{elemento.getTipo_ee()});
        String resultado = "null";

        if (cursor.moveToFirst()) {
            resultado = String.valueOf(cursor.getInt(cursor.getColumnIndex("id")));
        }

        cursor.close();

        return resultado;
    }

    private String obtenerModalidad(SQLiteDatabase dbr, Subsidio elemento) {
        Cursor cursor = dbr.rawQuery(queryModalidad, new String[]{elemento.getModalidad()});
        String resultado = "null";

        if (cursor.moveToFirst()) {
            resultado = String.valueOf(cursor.getInt(cursor.getColumnIndex("id")));
        }

        cursor.close();

        return resultado;
    }

    private ConsultaSubsidio consulta() {
        return consulta(null);
    }

    private ConsultaSubsidio consulta(String filtroEntidad) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] args = null;
        String selectQuery =  "SELECT modalidad_id, SUM(acciones) sumAcciones, SUM(monto) sumMonto " + "FROM " + Subsidio.TABLE + " ";
        if (filtroEntidad != null) {
            selectQuery += "WHERE cve_ent = ? ";
            args = new String[] {filtroEntidad};
        }
        selectQuery += "GROUP BY modalidad_id;";

        Cursor cursor = db.rawQuery(selectQuery, args);
        ConsultaSubsidio dato = new ConsultaSubsidio();
        if (cursor.moveToFirst()) {
            do {
                int modalidadId = cursor.getInt(cursor.getColumnIndex("modalidad_id"));
                long acciones = cursor.getLong(cursor.getColumnIndex("sumAcciones"));
                double monto = cursor.getLong(cursor.getColumnIndex("sumMonto"));
                Consulta consulta = new Consulta(acciones, monto);
                switch (modalidadId){
                    case 1:
                        dato.setNueva(consulta); break;
                    case 2:
                        dato.setUsada(consulta); break;
                    case 3:
                        dato.setAutoproduccion(consulta); break;
                    case 4:
                        dato.setMejoramiento(consulta); break;
                    case 5:
                        dato.setLotes(consulta); break;
                    case 6:
                        dato.setOtros(consulta); break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return dato;
    }
}
