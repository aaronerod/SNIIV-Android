package mx.gob.conavi.sniiv.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.modelos.Consulta;
import mx.gob.conavi.sniiv.modelos.ConsultaFinanciamiento;
import mx.gob.conavi.sniiv.modelos.Financiamiento;
import mx.gob.conavi.sniiv.modelos.FinanciamientoResultado;

/**
 * Created by admin on 10/08/15.
 */
public class FinanciamientoRepository implements Repository<Financiamiento> {
    private static final String TAG = FinanciamientoRepository.class.getSimpleName();
    private final AdminSQLiteOpenHelper dbHelper;
    private static String queryOrganismo = "SELECT id FROM Organismo WHERE descripcion = UPPER(?)";

    public FinanciamientoRepository(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    @Override
    public void saveAll(Financiamiento[] elementos) {
        SQLiteDatabase dbw = dbHelper.getWritableDatabase();
        SQLiteDatabase dbr = dbHelper.getReadableDatabase();

        String query = "INSERT INTO Financiamiento(cve_ent, organismo_id, destino_id, agrupacion_id, acciones, monto)" +
                "VALUES(?, ?, ?, ?, ?, ?)";

        for (Financiamiento elemento : elementos) {
            dbw.execSQL(query, new String[]{
                    String.valueOf(elemento.getCve_ent()),
                    obtenerOrganismo(dbr, elemento),
                    obtenerDestino(dbr, elemento),
                    obtenerAgrupacion(dbr, elemento),
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
        db.delete(Financiamiento.TABLE, null, null);
        db.close();
    }

    @Override
    public Financiamiento[] loadFromStorage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT cve_ent, O.descripcion organismo," +
                "D.descripcion destino, A.descripcion agrupacion, " +
                "acciones, monto FROM " + Financiamiento.TABLE + " F " +
                " LEFT JOIN Organismo O ON F.organismo_id = O.id" +
                " LEFT JOIN Destino D ON F.destino_id = D.id" +
                " LEFT JOIN Agrupacion A ON F.agrupacion_id = A.id";

        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Financiamiento> datos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Financiamiento dato = new Financiamiento();
                dato.setCve_ent(cursor.getInt(cursor.getColumnIndex("cve_ent")));
                dato.setOrganismo(cursor.getString(cursor.getColumnIndex("organismo")));
                dato.setDestino(cursor.getString(cursor.getColumnIndex("destino")));
                dato.setAgrupacion(cursor.getString(cursor.getColumnIndex("agrupacion")));
                dato.setAcciones(cursor.getLong(cursor.getColumnIndex("acciones")));
                dato.setMonto(cursor.getDouble(cursor.getColumnIndex("monto")));

                datos.add(dato);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return datos.toArray(new Financiamiento[0]);
    }

    private String obtenerAgrupacion(SQLiteDatabase dbr, Financiamiento elemento) {
        String queryAgrupacion = "SELECT id FROM Agrupacion WHERE descripcion = ?";
        Cursor cursor = dbr.rawQuery(queryAgrupacion, new String[]{elemento.getAgrupacion()});
        String resultado = "null";

        if (cursor.moveToFirst()) {
            resultado = String.valueOf(cursor.getInt(cursor.getColumnIndex("id")));
        }

        cursor.close();

        return resultado;
    }

    private String obtenerDestino(SQLiteDatabase dbr, Financiamiento elemento) {
        String queryDestino = "SELECT id FROM Destino WHERE descripcion = ?";
        Cursor cursor = dbr.rawQuery(queryDestino, new String[]{elemento.getDestino()});
        String resultado = "null";

        if (cursor.moveToFirst()) {
            resultado = String.valueOf(cursor.getInt(cursor.getColumnIndex("id")));
        }

        cursor.close();

        return resultado;
    }

    private String obtenerOrganismo(SQLiteDatabase dbr, Financiamiento elemento) {
        Cursor cursor = dbr.rawQuery(queryOrganismo, new String[]{elemento.getOrganismo()});
        String resultado = "null";

        if (cursor.moveToFirst()) {
            resultado = String.valueOf(cursor.getInt(cursor.getColumnIndex("id")));
        }

        cursor.close();

        return resultado;
    }

    public ConsultaFinanciamiento consultaNacional() {
        return consulta(null);
    }

    public ConsultaFinanciamiento consultaEntidad(int entidad) {
        return consulta(String.valueOf(entidad));
    }

    private ConsultaFinanciamiento consulta(String filtroEntidad) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] args = null;
        String selectQuery =  "SELECT DISTINCT destino_id FROM " + Financiamiento.TABLE  + " ";
        if (filtroEntidad != null) {
            selectQuery += "WHERE cve_ent = ? ";
            args = new String[] {filtroEntidad};
        }

        Cursor cursor = db.rawQuery(selectQuery, args);
        ConsultaFinanciamiento dato = new ConsultaFinanciamiento();
        if (cursor.moveToFirst()) {
            do {
                int destinoId = cursor.getInt(cursor.getColumnIndex("destino_id"));
                FinanciamientoResultado resultado = obtieneResultado(destinoId, filtroEntidad);
                switch (destinoId) {
                    case 1:
                        dato.setViviendasNuevas(resultado);
                        break;
                    case 2:
                        dato.setViviendasUsadas(resultado);
                        break;
                    case 3:
                        dato.setMejoramientos(resultado);
                        break;
                    case 4:
                        dato.setOtrosProgramas(resultado);
                        break;
                }
            } while (cursor.moveToNext());
        }

        dato.setTotal(obtieneTotal(filtroEntidad));

        cursor.close();
        db.close();

        return dato;
    }

    private FinanciamientoResultado obtieneResultado(int destinoId, String filtroEntidad) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] args;
        String selectQuery = "SELECT agrupacion_id, " +
                "SUM(acciones) sumAcciones, SUM(monto) sumMonto FROM " + Financiamiento.TABLE + " " +
                "WHERE destino_id = ?";

        if (filtroEntidad != null) {
            selectQuery += " AND cve_ent = ? ";
            args = new String[] {String.valueOf(destinoId), filtroEntidad};
        } else {
            args = new String[] {String.valueOf(destinoId)};
        }
        selectQuery += "GROUP BY agrupacion_id;";

        Cursor cursor = db.rawQuery(selectQuery, args);
        FinanciamientoResultado resultado = new FinanciamientoResultado();
        if (cursor.moveToFirst()) {
            do {
                int agrupacionId = cursor.getInt(cursor.getColumnIndex("agrupacion_id"));
                long acciones = cursor.getLong(cursor.getColumnIndex("sumAcciones"));
                double monto = cursor.getLong(cursor.getColumnIndex("sumMonto"));
                Consulta consulta = new Consulta(acciones, monto);
                switch (agrupacionId){
                    case 1:
                        resultado.setCofinanciamiento(consulta);
                        break;
                    case 2:
                        resultado.setCreditoIndividual(consulta);
                        break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return resultado;
    }

    private Consulta obtieneTotal(String filtroEntidad) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT SUM(acciones) sumAcciones, SUM(monto) sumMonto FROM " +
                Financiamiento.TABLE;

        String[] args = null;
        if (filtroEntidad != null) {
            selectQuery += " WHERE cve_ent = ? ";
            args = new String[] {filtroEntidad};
        }

        Cursor cursor = db.rawQuery(selectQuery, args);
        Consulta consulta = new Consulta();
        if (cursor.moveToFirst()) {
            do {
                long acciones = cursor.getLong(cursor.getColumnIndex("sumAcciones"));
                double monto = cursor.getLong(cursor.getColumnIndex("sumMonto"));
                consulta = new Consulta(acciones, monto);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return consulta;
    }
}
