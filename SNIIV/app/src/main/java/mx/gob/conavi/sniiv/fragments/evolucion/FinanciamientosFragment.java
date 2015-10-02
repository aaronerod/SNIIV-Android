package mx.gob.conavi.sniiv.fragments.evolucion;

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
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.EnumSet;

import butterknife.Bind;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.charts.LineChartBuilder;
import mx.gob.conavi.sniiv.charts.LineChartConfig;
import mx.gob.conavi.sniiv.charts.MyMarkerView;
import mx.gob.conavi.sniiv.datos.Datos;
import mx.gob.conavi.sniiv.datos.DatosEvolucionFinanciamiento;
import mx.gob.conavi.sniiv.fragments.BaseFragment;
import mx.gob.conavi.sniiv.modelos.EstadoMenu;
import mx.gob.conavi.sniiv.modelos.EvolucionFinanciamiento;
import mx.gob.conavi.sniiv.parsing.ParseEvolucionFinanciamiento;
import mx.gob.conavi.sniiv.sqlite.EvolucionFinanciamientoRepository;

/**
 * Created by octavio.munguia on 28/09/2015.
 */
public class FinanciamientosFragment extends BaseFragment<EvolucionFinanciamiento> {
    private static final String TAG = FinanciamientosFragment.class.getSimpleName();
    @Nullable  @Bind(R.id.chart) LineChart mChart;

    protected String titulo;
    private boolean errorRetrievingData = false;
    private boolean showAcciones = true;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        repository = new EvolucionFinanciamientoRepository(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        errorShowed = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (mChart != null) {
            mChart.setNoDataText("Datos no disponibles");
        }

        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_evolucion, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_toggle:
                showAcciones = !showAcciones;
                setOptionTitle();
                inicializaDatosChart();
                break;
            case R.id.action_guardar:
                guardarChart();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_evolucion;
    }

    @Override
    protected void mostrarDatos() {
        if (entidad == null) {
            return;
        }

        inicializaDatos();

        if (mChart != null) {
            inicializaDatosChart();
        }
    }

    protected void guardarChart() {
        if (mChart != null) {
            mChart.saveToGallery(getKey() + System.currentTimeMillis() + ".jpg", 100);
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.mensaje_imagen_guardada, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected AsyncTask<Void, Void, Void> getAsyncTask() {
        return new AsyncTaskRunner();
    }

    @Override
    protected String getKey() {
        return EvolucionFinanciamiento.TABLE;
    }

    @Override
    protected String getFechaAsString() {
        return fechas != null ? fechas.getFecha_finan() : null;
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
                ParseEvolucionFinanciamiento parse = new ParseEvolucionFinanciamiento();
                EvolucionFinanciamiento[] datosParse = parse.getDatos();
                repository.deleteAll();
                ((EvolucionFinanciamientoRepository)repository).saveAll(parse.getJsonObject());

                datos = new DatosEvolucionFinanciamiento(getActivity(), datosParse);
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
                habilitaPantalla();;
                getActivity().invalidateOptionsMenu();
            } else {
                Utils.alertDialogShow(getActivity(), getString(R.string.mensaje_error_datos));
                progressDialog.dismiss();
            }
        }
    }

    protected void inicializaDatosChart() {
        int[] xValues = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        LineChartConfig config = new LineChartConfig();
        config.setParties(entidad.getParties());
        config.setyValues(getValues());
        config.setDescription(titulo);
        config.setxValues(xValues);
        config.setShowAcciones(showAcciones);
        config.setConfiguracion(configuracion);
        LineChartBuilder.buildLineChart(getActivity(), mChart,config);
    }

    protected void inicializaDatos() {
        if (fechas != null) {
            String finan = String.format("%s\n(%s)", "Financiamientos Otorgados",
                    Utils.formatoDiaMes(fechas.getFecha_finan()));
            titulo =  finan;
        } else {
            titulo = "Financiamientos Otorgados";
        }
    }

    protected Datos<EvolucionFinanciamiento> getDatos(EvolucionFinanciamiento[] datos) {
        return new DatosEvolucionFinanciamiento(getActivity(), datos);
    }

    private ArrayList<double[]> getValues() {
        if (showAcciones) {
            return entidad.getYValuesAcciones();
        } else {
            return entidad.getYValuesMontos();
        }
    }

    private void setOptionTitle() {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (showAcciones){
            item.setTitle("MONTOS");
        } else {
            item.setTitle("ACCIONES");
        }
    }
}