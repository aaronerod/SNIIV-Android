package mx.gob.conavi.sniiv.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.NumberPicker;

import java.util.Date;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.activities.SniivApplication;
import mx.gob.conavi.sniiv.modelos.Fechas;
import mx.gob.conavi.sniiv.parsing.ParseFechas;
import mx.gob.conavi.sniiv.sqlite.FechasRepository;

/**
 * Created by admin on 06/08/15.
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    protected ProgressDialog progressDialog;
    protected NumberPicker pickerEstados;
    protected NumberPicker.OnValueChangeListener valueChangeListener;
    protected Fechas fechas;
    protected FechasRepository fechasRepository;

    protected abstract void loadFromStorage();
    protected abstract void mostrarDatos();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fechasRepository = new FechasRepository(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isDataLoaded() && Utils.isNetworkAvailable(getActivity())) {
            getAsyncTask().execute();
            return;
        }

        loadFromStorage();
    }

    protected void configuraPickerView() {
        pickerEstados.setMaxValue(Utils.listEdo.length - 1);
        pickerEstados.setMinValue(0);
        pickerEstados.setDisplayedValues(Utils.listEdo);
        pickerEstados.setEnabled(false);
        pickerEstados.setOnValueChangedListener(valueChangeListener);
    }

    protected void habilitaPantalla() {
        mostrarDatos();
        pickerEstados.setEnabled(true);
        progressDialog.dismiss();
    }

    protected abstract NumberPicker.OnValueChangeListener configuraValueChangeListener();
    protected abstract AsyncTask<Void, Void, Void> getAsyncTask();
    protected abstract String getKey();

    protected boolean isDataLoaded() {
        SniivApplication app = (SniivApplication)getActivity().getApplicationContext();
        long time = app.getTimeLastUpdated(getKey());
        return Utils.equalDays(new Date(time), new Date());
    }

    protected void saveTimeLastUpdated() {
        SniivApplication app = (SniivApplication)getActivity().getApplicationContext();
        app.setTimeLastUpdated(getKey(), new Date().getTime());
    }

    protected void obtenerFechas() {
        ParseFechas parseFechas = new ParseFechas();
        Fechas[] fechasWeb = parseFechas.getDatos();
        fechasRepository.deleteAll();
        fechasRepository.saveAll(fechasWeb);

        asignaFechas();
    }

    protected void asignaFechas() {
        Fechas[] fechasStorage = fechasRepository.loadFromStorage();
        if(fechasStorage.length > 0) {
            fechas = fechasStorage[0];
        }
    }
}
