package mx.gob.conavi.sniiv.sqlite;

import android.content.Context;
import android.util.Log;

import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import mx.gob.conavi.sniiv.modelos.Evolucion;
import mx.gob.conavi.sniiv.modelos.EvolucionTipo;
import mx.gob.conavi.sniiv.parsing.ParseEvolucion;

/**
 * Created by octavio.munguia on 25/09/2015.
 */
public class EvolucionRepository implements Repository<Evolucion> {
    private static final String TAG = EvolucionRepository.class.getSimpleName();
    public static final String[] FILE_NAMES = {"evolucionFinanciamiento", "evolucionSubsidios",
            "evolucionRegistroVivienda"};
    private Context context;
    private int index = 0;

    public EvolucionRepository(Context context, EvolucionTipo tipo) {
        this.context = context;
        switch (tipo) {
            case FINANCIAMIENTOS:
                index = 0;
                break;
            case SUBSIDIOS:
                index = 1;
                break;
            case REGISTRO_VIVIENDA:
                index = 2;
                break;
        }
    }

    @Override
    public void saveAll(Evolucion[] elementos) {
        throw new NotImplementedException("saveAll");
    }

    public void saveAll(JSONObject object) {
        String filename = FILE_NAMES[index];
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
        context.deleteFile(FILE_NAMES[index]);
    }

    public Evolucion[] loadFromStorage() {
        try {
            BufferedReader input;
            input = new BufferedReader(new InputStreamReader(
                    context.openFileInput(FILE_NAMES[index])));
            StringBuffer content = new StringBuffer();
            char[] buffer = new char[1024];
            int num;
            while ((num = input.read(buffer)) > 0) {
                content.append(buffer, 0, num);
            }

            JSONObject object = new JSONObject(content.toString());
            return ParseEvolucion.createModel(object);
        } catch (JSONException jse) {
            Log.v(TAG, "Error parseando json " + FILE_NAMES[index]);
        } catch (IOException e) {
            Log.v(TAG, "Error leyendo del archivo");
        }

        return new Evolucion[0];
    }
}
