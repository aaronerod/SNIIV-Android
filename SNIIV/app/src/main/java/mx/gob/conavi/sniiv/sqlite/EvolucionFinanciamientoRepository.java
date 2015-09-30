package mx.gob.conavi.sniiv.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import mx.gob.conavi.sniiv.modelos.Consulta;
import mx.gob.conavi.sniiv.modelos.EvolucionFinanciamiento;
import mx.gob.conavi.sniiv.modelos.EvolucionFinanciamientoResultado;
import mx.gob.conavi.sniiv.parsing.ParseEvolucionFinanciamiento;

/**
 * Created by octavio.munguia on 25/09/2015.
 */
public class EvolucionFinanciamientoRepository implements Repository<EvolucionFinanciamiento> {
    private static final String TAG = FinanciamientoRepository.class.getSimpleName();
    public static final String FILE_NAME = "evolucionFinanciamiento";
    private Context context;

    public EvolucionFinanciamientoRepository(Context context) {
        this.context = context;
    }

    @Override
    public void saveAll(EvolucionFinanciamiento[] elementos) {
        throw new NotImplementedException("saveAll");
    }

    public void saveAll(JSONObject object) {
        String filename = FILE_NAME;
        String string = object.toString();
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {
        context.deleteFile("evolucionFinanciamiento");
    }

    public EvolucionFinanciamiento[] loadFromStorage() {
        Log.v(TAG, "loadFromStorage");
        try {
            BufferedReader input;
            input = new BufferedReader(new InputStreamReader(
                    context.openFileInput("evolucionFinanciamiento")));
            StringBuffer content = new StringBuffer();
            char[] buffer = new char[1024];
            int num;
            while ((num = input.read(buffer)) > 0) {
                content.append(buffer, 0, num);
            }

            JSONObject object = new JSONObject(content.toString());
            return ParseEvolucionFinanciamiento.createModel(object);
        } catch (JSONException jse) {
            Log.v(TAG, "Error parseando json EvolucionFinanciamiento");
        } catch (IOException e) {
            Log.v(TAG, "Error leyendo del archivo");
        }

        return new EvolucionFinanciamiento[0];
    }
}
