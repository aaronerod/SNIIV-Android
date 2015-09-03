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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Constants;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.charts.PieChartBuilder;
import mx.gob.conavi.sniiv.datos.DatosValorVivienda;
import mx.gob.conavi.sniiv.listeners.OnChartValueSelected;
import mx.gob.conavi.sniiv.modelos.EstadoMenuOferta;
import mx.gob.conavi.sniiv.modelos.ValorVivienda;
import mx.gob.conavi.sniiv.parsing.ParseValorVivienda;
import mx.gob.conavi.sniiv.sqlite.ValorViviendaRepository;

/**
 * Created by admin on 04/08/15.
 */
public class ValorViviendaFragment extends OfertaBaseFragment {
    public static final String TAG = "ValorViviendaFragment";

    private DatosValorVivienda datos;
    private ValorVivienda entidad;
    private ValorViviendaRepository repository;
    private boolean errorRetrievingData = false;

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

        loadFechasStorage();

        mostrarDatos();
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
                    Utils.formatoMes(fechas.getFecha_vv()));
            txtTitleValorVivienda.setText(valor);
        }

        if (entidad != null && mChart != null) {
            inicializaDatosChart();
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

    @Override
    protected String getFechaAsString() {
        return fechas != null ? fechas.getFecha_vv() : null;
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

                saveTimeLastUpdated(getFechaActualizacion().getTime());

                loadFechasStorage();
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
                intentaInicializarGrafica();
                getActivity().invalidateOptionsMenu();
            } else {
                Utils.alertDialogShow(getActivity(), getString(R.string.mensaje_error_datos));
                progressDialog.dismiss();
            }
        }
    }

    protected void intentaInicializarGrafica() {
        if (entidad == null) {
            estado = EstadoMenuOferta.NINGUNO;
            return;
        }

        if (mChart != null) {
            inicializaDatosChart();
            estado = EstadoMenuOferta.GUARDAR;
        } else {
            estado = EstadoMenuOferta.GRAFICA;
        }
    }

    protected void inicializaDatosChart() {
        ArrayList<String> pParties =  entidad.getParties();
        long[] pValues = entidad.getValues();
        String pCenterText = "Valor de Vivienda";
        String pYvalLegend = "Porcentaje";
        int pEstado = entidad.getCve_ent();
        PieChartBuilder.buildPieChart(mChart, pParties, pValues, pCenterText,
                pYvalLegend, pEstado, getString(R.string.etiqueta_conavi));
        OnChartValueSelected listener = new OnChartValueSelected(mChart, getKey(), pEstado, pParties);
        mChart.setOnChartValueSelectedListener(listener);
    }

    public void muestraDialogo() {
        OfertaDialogFragment dialog = new OfertaDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(Constants.PARTIES, entidad.getParties());
        args.putLongArray(Constants.VALUES, entidad.getValues());
        args.putString(Constants.CENTER_TEXT, "Valor de Vivienda");
        args.putString(Constants.Y_VAL_LEGEND,"Porcentaje");
        args.putInt(Constants.ESTADO, entidad.getCve_ent());
        args.putString(Constants.DESCRIPCION, getKey());
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "OfertaDialog");
    }
}
