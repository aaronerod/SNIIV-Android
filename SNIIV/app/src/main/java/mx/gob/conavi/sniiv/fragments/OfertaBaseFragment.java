package mx.gob.conavi.sniiv.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;

import butterknife.Bind;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.modelos.EstadoMenuOferta;

/**
 * Created by octavio.munguia on 01/09/2015.
 */
public abstract class OfertaBaseFragment extends BaseFragment {
    private static final String TAG = OfertaBaseFragment.class.getSimpleName();
    @Nullable @Bind(R.id.chart) PieChart mChart;
    @Nullable @Bind(R.id.tableLayout) TableLayout tableLayout;
    @Nullable @Bind(R.id.txtTitulo) TextView txtTitle;

    protected EstadoMenuOferta estado = EstadoMenuOferta.NINGUNO;
    protected String configuracion;
    protected String[] etiquetas;
    protected String[] valores;
    protected String titulo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configuracion = getString(R.string.selected_configuration);
        valueChangeListener = configuraValueChangeListener();
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        intentaInicializarGrafica();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_oferta, menu);

        MenuItem guardar = menu.findItem(R.id.action_guardar);
        MenuItem grafica = menu.findItem(R.id.action_grafica);

        switch (estado) {
            case NINGUNO:
                guardar.setVisible(false);
                grafica.setVisible(false);
                break;
            case GUARDAR:
                guardar.setVisible(true);
                grafica.setVisible(false);
                break;
            case GRAFICA:
                guardar.setVisible(false);
                grafica.setVisible(true);
                break;
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_grafica:
                muestraDialogo();
                break;
            case R.id.action_guardar:
                guardarChart();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected abstract void intentaInicializarGrafica();
    protected abstract void inicializaDatosChart();

    protected void guardarChart() {
        if (mChart != null) {
            mChart.saveToGallery(getKey() + System.currentTimeMillis() + ".jpg", 100);
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.mensaje_imagen_guardada, Toast.LENGTH_SHORT).show();
        }
    }

    protected void creaTableLayout() {
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
}
