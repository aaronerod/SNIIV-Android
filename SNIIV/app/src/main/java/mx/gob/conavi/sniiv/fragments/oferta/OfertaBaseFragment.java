package mx.gob.conavi.sniiv.fragments.oferta;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;

import java.util.EnumSet;

import butterknife.Bind;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.fragments.BaseFragment;
import mx.gob.conavi.sniiv.modelos.EstadoMenu;

/**
 * Created by octavio.munguia on 01/09/2015.
 */
public abstract class OfertaBaseFragment<T> extends BaseFragment<T> {
    private static final String TAG = OfertaBaseFragment.class.getSimpleName();
    @Nullable @Bind(R.id.chart) PieChart mChart;
    @Nullable @Bind(R.id.tableLayout) TableLayout tableLayout;
    @Nullable @Bind(R.id.txtTitulo) TextView txtTitle;

    protected EnumSet<EstadoMenu> estado = EnumSet.of(EstadoMenu.NINGUNO);
    protected String[] etiquetas;
    protected String[] valores;
    protected String titulo;

    //region Métodos del Ciclo de Vida
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
        etiquetas = getEtiquetas();

        if (mChart != null) {
            mChart.setNoDataText(getString(R.string.mensaje_datos_no_disponibles));
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_oferta, menu);

        MenuItem guardar = menu.findItem(R.id.action_guardar);
        MenuItem grafica = menu.findItem(R.id.action_datos);

        if (estado.contains(EstadoMenu.NINGUNO)) {
            guardar.setVisible(false);
            grafica.setVisible(false);
        }

        if (estado.contains(EstadoMenu.GUARDAR)){
            guardar.setVisible(true);
        }

        if (estado.contains(EstadoMenu.DATOS)) {
            grafica.setVisible(true);
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
    //endregion

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_oferta;
    }

    protected void mostrarDatos() {
        if (entidad == null) {
            return;
        }

        inicializaDatos();

        if (configuracion.equals("sw600dp")) {
            creaTableLayout();
        }

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
            inicializaDatosChart();
            estado = EnumSet.of(EstadoMenu.GUARDAR);
        } else {
            estado = EstadoMenu.AMBOS;
        }
    }

    protected void creaTableLayout() {
        tableLayout.removeAllViews();
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

    protected void muestraDialogo() {
        DatosOfertaDialogFragment dialog = new DatosOfertaDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray("Etiquetas", etiquetas);
        args.putStringArray("Valores", valores);
        args.putString("Titulo", titulo);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "DatosOfertaDialog");
    }

    protected void guardarChart() {
        if (mChart != null) {
            mChart.saveToGallery(getKey() + System.currentTimeMillis() + ".jpg", 100);
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.mensaje_imagen_guardada, Toast.LENGTH_SHORT).show();
        }
    }

    protected abstract void inicializaDatosChart();
    protected void inicializaDatos() {}
    protected String[] getEtiquetas() {return new String[]{};}
}
