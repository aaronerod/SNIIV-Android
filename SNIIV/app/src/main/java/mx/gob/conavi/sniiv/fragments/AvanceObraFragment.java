package mx.gob.conavi.sniiv.fragments;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
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
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Constants;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.charts.PieChartBuilder;
import mx.gob.conavi.sniiv.datos.DatosAvanceObra;
import mx.gob.conavi.sniiv.modelos.AvanceObra;
import mx.gob.conavi.sniiv.parsing.ParseAvanceObra;
import mx.gob.conavi.sniiv.sqlite.AvanceObraRepository;
import mx.gob.conavi.sniiv.sqlite.FechasRepository;
import mx.gob.conavi.sniiv.templates.ColorTemplate;

public class AvanceObraFragment extends OfertaBaseFragment {
    public static final String TAG = "AvanceObraFragment";

    private DatosAvanceObra datos;
    private AvanceObra entidad;
    private AvanceObraRepository repository;
    private boolean errorRetrievingData = false;

    @Bind(R.id.txtTitleSubsidios) TextView txtTitleAvanceObra;
    @Bind(R.id.txtProceso50) TextView txtProceso50;
    @Bind(R.id.txtProceso99) TextView txtProceso99;
    @Bind(R.id.txtTerminadasRecientes) TextView txtTerminadasRecientes;
    @Bind(R.id.txtTerminadasAntiguas) TextView txtTerminadasAntiguas;
    @Bind(R.id.txtTotal) TextView txtTotal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new AvanceObraRepository(getActivity());
        valueChangeListener = configuraValueChangeListener();
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (entidad != null && mChart != null) {
            inicializaDatosChart();
            mostrarBoton = true;
        }
    }

    protected void loadFromStorage() {
        AvanceObra[] datosStorage = repository.loadFromStorage();
        if(datosStorage.length > 0) {
            datos = new DatosAvanceObra(getActivity(), datosStorage);
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
        View rootView = inflater.inflate(R.layout.fragment_avance_obra, container, false);
        ButterKnife.bind(this, rootView);

        pickerEstados = (NumberPicker) rootView.findViewById(R.id.pckEstados);
        configuraPickerView();

        return rootView;
    }

    protected void mostrarDatos() {
        if(entidad != null) {
            txtProceso50.setText(Utils.toString(entidad.getViv_proc_m50()));
            txtProceso99.setText(Utils.toString(entidad.getViv_proc_50_99()));
            txtTerminadasRecientes.setText(Utils.toString(entidad.getViv_term_rec()));
            txtTerminadasAntiguas.setText(Utils.toString(entidad.getViv_term_ant()));
            txtTotal.setText(Utils.toString(entidad.getTotal()));
        }

        if (fechas != null) {
            String avance = String.format("%s (%s)", getString(R.string.title_avance_obra),
                    Utils.formatoMes(fechas.getFecha_vv()));
            txtTitleAvanceObra.setText(avance);
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
        return AvanceObra.TABLE;
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
                ParseAvanceObra parse = new ParseAvanceObra();
                AvanceObra[] datosParse = parse.getDatos();
                repository.deleteAll();
                repository.saveAll(datosParse);

                datos = new DatosAvanceObra(getActivity(), datosParse);
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
            } else {
                Utils.alertDialogShow(getActivity(), getString(R.string.mensaje_error_datos));
                progressDialog.dismiss();
            }
        }
    }

    protected void inicializaDatosChart() {
        ArrayList<String> pParties =  entidad.getParties();
        long[] pValues = entidad.getValues();
        String pCenterText = "Avance Obra";
        String pYvalLegend = "Porcentaje";
        int pEstado = entidad.getCve_ent();
        PieChartBuilder.buildPieChart(mChart, pParties, pValues, pCenterText,
                pYvalLegend, pEstado, getString(R.string.etiqueta_conavi));
    }

    protected void muestraDialogo() {
        OfertaDialogFragment dialog = new OfertaDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(Constants.PARTIES, entidad.getParties());
        args.putLongArray(Constants.VALUES, entidad.getValues());
        args.putString(Constants.CENTER_TEXT, "Avance Obra");
        args.putString(Constants.Y_VAL_LEGEND,"Porcentaje");
        args.putInt(Constants.ESTADO, entidad.getCve_ent());
        args.putString(Constants.DESCRIPCION, getKey());
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "OfertaDialog");
    }
}
