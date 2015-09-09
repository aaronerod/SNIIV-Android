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
import android.widget.TableLayout;
import android.widget.TableRow;
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
import mx.gob.conavi.sniiv.listeners.OnChartValueSelected;
import mx.gob.conavi.sniiv.modelos.AvanceObra;
import mx.gob.conavi.sniiv.modelos.EstadoMenuOferta;
import mx.gob.conavi.sniiv.parsing.ParseAvanceObra;
import mx.gob.conavi.sniiv.sqlite.AvanceObraRepository;
import mx.gob.conavi.sniiv.sqlite.FechasRepository;
import mx.gob.conavi.sniiv.templates.ColorTemplate;

public class AvanceObraFragment extends OfertaBaseFragment {
    public static final String TAG = "AvanceObraFragment";

    @Nullable @Bind(R.id.tableLayout) TableLayout tableLayout;
    @Nullable @Bind(R.id.txtTitulo) TextView txtTitle;
    private String[] etiquetas;
    private String[] valores;
    private String titulo;

    private DatosAvanceObra datos;
    private AvanceObra entidad;
    private AvanceObraRepository repository;
    private boolean errorRetrievingData = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new AvanceObraRepository(getActivity());
        valueChangeListener = configuraValueChangeListener();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_avance_obra, container, false);
        ButterKnife.bind(this, rootView);

        pickerEstados = (NumberPicker) rootView.findViewById(R.id.pckEstados);
        configuraPickerView();

        etiquetas = new String[]{
                getString(R.string.title_50), getString(R.string.title_99),
                getString(R.string.title_recientes), getString(R.string.title_antiguas),
                getString(R.string.title_total)};

        return rootView;
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

    protected void mostrarDatos() {
        if (entidad == null) {
            return;
        }

        inicializaDatos();

        if (getString(R.string.selected_configuration).equals("sw600dp") && tableLayout.getChildCount() == 0) {
            creaTableLayout();
        }

        if (mChart != null) {
            inicializaDatosChart();
        }
    }

    private void inicializaDatos() {
        valores =  new String[]{Utils.toString(entidad.getViv_proc_m50()),
                Utils.toString(entidad.getViv_proc_50_99()),
                Utils.toString(entidad.getViv_term_rec()),
                Utils.toString(entidad.getViv_term_ant()),
                Utils.toString(entidad.getTotal())};

        if (fechas != null) {
            String avance = String.format("%s (%s)", getString(R.string.title_avance_obra),
                    Utils.formatoMes(fechas.getFecha_vv()));
            titulo =  avance;
        } else {
            titulo = getString(R.string.title_avance_obra);
        }
    }

    private void creaTableLayout() {
        for (int i = 0; i< etiquetas.length; i++) {
            TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.table_row, null);
            TextView etiqueta = (TextView) row.findViewById(R.id.txtEtiqueta);
            TextView valor = (TextView) row.findViewById(R.id.txtValor);
            etiqueta.setText(etiquetas[i]);
            valor.setText(valores[i]);

            tableLayout.addView(row);
        }

        txtTitle.setText(titulo);
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

        if (getString(R.string.selected_configuration).equals("sw600dp")) {
            inicializaDatosChart();
            estado = EstadoMenuOferta.GUARDAR;
        } else {
            estado = EstadoMenuOferta.GRAFICA;
        }
    }

    protected void inicializaDatosChart() {
        ArrayList<String> pParties =  entidad.getParties();
        long[] pValues = entidad.getValues();
        String pCenterText = "Avance Obra";
        String pYvalLegend = getString(R.string.etiqueta_porcentaje);
        int pEstado = entidad.getCve_ent();
        PieChartBuilder.buildPieChart(mChart, pParties, pValues, pCenterText,
                pYvalLegend, pEstado, getString(R.string.etiqueta_conavi));
        OnChartValueSelected listener = new OnChartValueSelected(mChart, getKey(), pEstado, pParties);
        mChart.setOnChartValueSelectedListener(listener);
    }

    protected void muestraDialogo() {
        DatosOfertaDialogFragment dialog = new DatosOfertaDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray("Etiquetas", etiquetas);
        args.putStringArray("Valores", valores);
        args.putString("Titulo", titulo);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "DatosOfertaDialog");
    }
}
