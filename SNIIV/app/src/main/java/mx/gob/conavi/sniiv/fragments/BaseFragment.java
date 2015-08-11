package mx.gob.conavi.sniiv.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.NumberPicker;

import mx.gob.conavi.sniiv.Utils.Utils;

/**
 * Created by admin on 06/08/15.
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    protected ProgressDialog progressDialog;
    protected NumberPicker pickerEstados;
    protected NumberPicker.OnScrollListener scrollListener;

    protected abstract void loadFromStorage();
    protected abstract void mostrarDatos();

    @Override
    public void onResume() {
        super.onResume();

        if (Utils.isNetworkAvailable(getActivity())) {
            getAsyncTask().execute();
            return;
        }

        loadFromStorage();
    }

    protected void configuraPickerView() {
        pickerEstados.setMaxValue(Utils.listEdo.length - 1);
        pickerEstados.setMinValue(0);
        pickerEstados.setDisplayedValues(Utils.listEdo);
        pickerEstados.setOnScrollListener(scrollListener);
        pickerEstados.setEnabled(false);
    }

    protected void habilitaPantalla() {
        mostrarDatos();
        pickerEstados.setEnabled(true);
        progressDialog.dismiss();
    }

    protected abstract NumberPicker.OnScrollListener configuraScrollListener();
    protected abstract AsyncTask<Void, Void, Void> getAsyncTask();
}
