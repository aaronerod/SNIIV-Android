package mx.gob.conavi.sniiv.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;

import java.util.EnumSet;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.modelos.EstadoMenu;
import mx.gob.conavi.sniiv.sqlite.Repository;
import mx.gob.conavi.sniiv.sqlite.SubsidioRepository;

/**
 * Created by octavio.munguia on 21/09/2015.
 */
// TODO: refactorizar FinanciamientoFragment
public abstract class DemandaBaseFragment extends BaseFragment {
    protected Repository repository;
    protected EnumSet<EstadoMenu> estado = EnumSet.of(EstadoMenu.NINGUNO);
    protected int idEntidad = 0;
    protected String titulo;
    protected boolean errorRetrievingData = false;

    @Nullable
    @Bind(R.id.chart) PieChart mChart;

    @Override
    public void onResume() {
        super.onResume();

        intentaInicializarGrafica();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subsidios, container, false);
        ButterKnife.bind(this, rootView);

        if (mChart != null) {
            mChart.setNoDataText("Datos no disponibles");
        }

        pickerEstados = (NumberPicker) rootView.findViewById(R.id.pckEstados);
        configuraPickerView();
        return rootView;
    }

    protected abstract void createFragment();

    protected void intentaInicializarGrafica() {
        if (entidad == null) {
            estado = EnumSet.of(EstadoMenu.NINGUNO);
            return;
        }

        if (configuracion.equals("sw600dp")) {
            inicializaDatosChart();
            estado = EnumSet.of(EstadoMenu.GUARDAR);
        } else {
            estado = EstadoMenu.AMBOS;
        }
    }

    protected abstract void inicializaDatosChart();

    protected abstract void inicializaDatos();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_oferta, menu);

        MenuItem guardar = menu.findItem(R.id.action_guardar);
        MenuItem datos = menu.findItem(R.id.action_datos);

        if (estado.contains(EstadoMenu.NINGUNO)) {
            guardar.setVisible(false);
            datos.setVisible(false);
        }

        if (estado.contains(EstadoMenu.GUARDAR)){
            guardar.setVisible(true);
        }

        if (estado.contains(EstadoMenu.DATOS)) {
            datos.setVisible(true);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_datos:
                muestraDialogo();
                break;
            case R.id.action_guardar:
                guardarChart();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void guardarChart() {
        if (mChart != null) {
            mChart.saveToGallery(getKey() + System.currentTimeMillis() + ".jpg", 100);
            Toast.makeText(getActivity(),
                    R.string.mensaje_imagen_guardada, Toast.LENGTH_SHORT).show();
        }
    }

    protected abstract void muestraDialogo();
}
