package mx.gob.conavi.sniiv.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import java.text.ParseException;
import java.util.Date;

import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.activities.SniivApplication;
import mx.gob.conavi.sniiv.datos.Datos;
import mx.gob.conavi.sniiv.modelos.Fechas;
import mx.gob.conavi.sniiv.parsing.ParseFechas;
import mx.gob.conavi.sniiv.sqlite.FechasRepository;
import mx.gob.conavi.sniiv.sqlite.Repository;

/**
 * Created by admin on 06/08/15.
 */
public abstract class BaseFragment<T> extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    protected ProgressDialog progressDialog;
    protected NumberPicker pickerEstados;
    protected NumberPicker.OnValueChangeListener valueChangeListener;
    protected Fechas fechas;
    protected FechasRepository fechasRepository;
    protected T entidad;
    protected Datos<T> datos;
    protected Repository<T> repository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fechasRepository = new FechasRepository(getActivity());
        valueChangeListener = configuraValueChangeListener();
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, rootView);

        pickerEstados = (NumberPicker) rootView.findViewById(R.id.pckEstados);
        configuraPickerView();

        return rootView;
    }

    protected NumberPicker.OnValueChangeListener configuraValueChangeListener() {
        return new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 0) {
                    entidad = datos.consultaNacional();
                } else {
                    entidad = datos.consultaEntidad(newVal);
                }

                mostrarDatos();
            }
        };
    }

    protected void loadFromStorage() {
        T[] datosStorage = repository.loadFromStorage();
        if(datosStorage.length > 0) {
            datos = getDatos(datosStorage);
            entidad = datos.consultaNacional();
            pickerEstados.setEnabled(true);
        } else {
            Utils.alertDialogShow(getActivity(), getString(R.string.no_conectado));
        }

        loadFechasStorage();

        mostrarDatos();
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

    protected boolean isDataLoaded() {
        SniivApplication app = (SniivApplication)getActivity().getApplicationContext();
        long time = app.getTimeLastUpdated(getKey());
        Date fechaActualizacion = getFechaActualizacion();
        Log.v(TAG, Utils.fmtDMY.format(new Date(time)) + " - " + Utils.fmtDMY.format(fechaActualizacion));
        return Utils.equalDays(new Date(time), fechaActualizacion);
    }

    protected void saveTimeLastUpdated(long lastTime) {
        SniivApplication app = (SniivApplication)getActivity().getApplicationContext();
        app.setTimeLastUpdated(getKey(), lastTime);
    }

    protected void loadFechasStorage() {
        Fechas[] fechasStorage = fechasRepository.loadFromStorage();
        if(fechasStorage.length > 0) {
            fechas = fechasStorage[0];
        }
    }

    protected Date getFechaActualizacion(){
        Fechas[] fechasStorage = fechasRepository.loadFromStorage();
        if(fechasStorage.length > 0) {
            fechas = fechasStorage[0];
            try {
                return Utils.fmtDMY.parse(getFechaAsString());
            } catch (ParseException e) {
                return new Date(0);
            }
        }

        return new Date(0);
    }

    protected abstract void mostrarDatos();
    protected abstract AsyncTask<Void, Void, Void> getAsyncTask();
    protected abstract String getKey();
    protected abstract String getFechaAsString();
    protected int getLayoutId() {return 0;}
    protected Datos<T> getDatos(T[] datos) {return null;}
}
