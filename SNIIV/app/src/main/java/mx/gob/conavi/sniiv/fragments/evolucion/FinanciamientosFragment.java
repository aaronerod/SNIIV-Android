package mx.gob.conavi.sniiv.fragments.evolucion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;

import java.util.EnumSet;

import butterknife.Bind;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.datos.Datos;
import mx.gob.conavi.sniiv.datos.DatosEvolucionFinanciamiento;
import mx.gob.conavi.sniiv.fragments.BaseFragment;
import mx.gob.conavi.sniiv.modelos.EstadoMenu;
import mx.gob.conavi.sniiv.modelos.EvolucionFinanciamiento;

/**
 * Created by octavio.munguia on 28/09/2015.
 */
public class FinanciamientosFragment extends BaseFragment<EvolucionFinanciamiento> {
    private static final String TAG = FinanciamientosFragment.class.getSimpleName();
    @Nullable  @Bind(R.id.chart) LineChart mChart;

    protected EnumSet<EstadoMenu> estado = EnumSet.of(EstadoMenu.NINGUNO);
    protected String titulo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        intentaInicializarGrafica();
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

    protected void intentaInicializarGrafica() {
        if (entidad == null) {
            estado = EnumSet.of(EstadoMenu.NINGUNO);
            return;
        }

        if (configuracion.equals("sw600dp")) {
            //inicializaDatosChart();
            estado = EnumSet.of(EstadoMenu.GUARDAR);
        } else {
            estado = EstadoMenu.AMBOS;
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
        return null;
    }

    @Override
    protected String getKey() {
        return EvolucionFinanciamiento.TABLE;
    }

    @Override
    protected String getFechaAsString() {
        return fechas != null ? fechas.getFecha_finan() : null;
    }

    // TODO: crear LineChartBuilder
    protected void inicializaDatosChart() {
        /* ArrayList<String> pParties =  entidad.getParties();
        long[] pValues = entidad.getValues();
        String pCenterText = "Avance Obra";
        int pEstado = entidad.getCve_ent();
        PieChartBuilder.buildPieChart(mChart, pParties, pValues, pCenterText,
                pEstado, getString(R.string.etiqueta_conavi));
        OnChartValueSelected listener = new OnChartValueSelected(mChart, getKey(), pEstado, pParties);
        mChart.setOnChartValueSelectedListener(listener); */
    }

    protected void inicializaDatos() {
        if (fechas != null) {
            String avance = String.format("%s (%s)", getString(R.string.title_avance_obra),
                    Utils.formatoMes(fechas.getFecha_vv()));
            titulo =  avance;
        } else {
            titulo = getString(R.string.title_avance_obra);
        }
    }

    protected Datos<EvolucionFinanciamiento> getDatos(EvolucionFinanciamiento[] datos) {
        return new DatosEvolucionFinanciamiento(getActivity(), datos);
    }
}
