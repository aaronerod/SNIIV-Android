package mx.gob.conavi.sniiv.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.datos.DatosValorVivienda;
import mx.gob.conavi.sniiv.modelos.ValorVivienda;
import mx.gob.conavi.sniiv.parsing.ParseValorVivienda;
import mx.gob.conavi.sniiv.sqlite.ValorViviendaRepository;

/**
 * Created by admin on 04/08/15.
 */
public class ValorViviendaFragment extends BaseFragment {
    public static final String TAG = "ValorViviendaFragment";

    private DatosValorVivienda datos;
    private ValorVivienda entidad;
    private ValorViviendaRepository repository;
    private boolean errorRetrievingData = false;
    private OfertaDialogFragment dialog;

    @Bind(R.id.txtTitleValorVivienda) TextView txtTitleValorVivienda;
    @Bind(R.id.txtEconomica) TextView txtEconomica;
    @Bind(R.id.txtPopular) TextView txtPopular;
    @Bind(R.id.txtTradicional) TextView txtTradicional;
    @Bind(R.id.txtMedia) TextView txtMedia;
    @Bind(R.id.txtTotal) TextView txtTotal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ValorViviendaRepository(getActivity());
        valueChangeListener = configuraValueChangeListener();
        setHasOptionsMenu(true);
    }

    protected void loadFromStorage() {
        ValorVivienda[] datosStorage = repository.loadFromStorage();
        if(datosStorage.length > 0) {
            datos = new DatosValorVivienda(getActivity(), datosStorage);
            entidad = datos.consultaNacional();
            pickerEstados.setEnabled(true);
        } else {
            Utils.alertDialogShow(getActivity(), getString(R.string.no_conectado));
        }

        asignaFechas();

        mostrarDatos();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_oferta, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_grafica) {
            dialog = new OfertaDialogFragment();
            Bundle args = new Bundle();
            args.putStringArrayList("parties", entidad.getParties());
            args.putLongArray("values", entidad.getValues());
            args.putString("centerText", "Valor Vivienda");
            args.putString("yValLegend","Porcentaje");
            args.putInt("estado", entidad.getCve_ent());
            dialog.setArguments(args);
            dialog.show(getFragmentManager(), "error");
        }


        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_valor_vivienda, container, false);
        ButterKnife.bind(this, rootView);

        pickerEstados = (NumberPicker) rootView.findViewById(R.id.pckEstados);
        configuraPickerView();

        return rootView;
    }

    protected void mostrarDatos() {
        if(entidad != null) {
            txtEconomica.setText(Utils.toString(entidad.getEconomica()));
            txtPopular.setText(Utils.toString(entidad.getPopular()));
            txtTradicional.setText(Utils.toString(entidad.getTradicional()));
            txtMedia.setText(Utils.toString(entidad.getMedia_residencial()));
            txtTotal.setText(Utils.toString(entidad.getTotal()));
        }

        if (fechas != null) {
            String valor = String.format("%s (%s)", getString(R.string.title_valor_vivienda),
                    fechas.getFecha_vv());
            txtTitleValorVivienda.setText(valor);
        }
    }

    @Override
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

    @Override
    protected AsyncTask<Void, Void, Void> getAsyncTask() {
        return new AsyncTaskRunner();
    }

    @Override
    protected String getKey() {
        return ValorVivienda.TABLE;
    }

    protected class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    null, getString(R.string.mensaje_espera));
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ParseValorVivienda parse = new ParseValorVivienda();
                ValorVivienda[] datosParse = parse.getDatos();
                repository.deleteAll();
                repository.saveAll(datosParse);

                datos = new DatosValorVivienda(getActivity(), datosParse);
                entidad = datos.consultaNacional();

                saveTimeLastUpdated();

                obtenerFechas();
            } catch (Exception e) {
                Log.v(TAG, "Error obteniendo datos");
                errorRetrievingData = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            if (!errorRetrievingData) {
                habilitaPantalla();
            } else {
                Utils.alertDialogShow(getActivity(), getString(R.string.mensaje_error_datos));
                progressDialog.dismiss();
            }
        }
    }
}
