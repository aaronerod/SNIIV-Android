package mx.gob.conavi.sniiv.fragments;

import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

import butterknife.Bind;
import mx.gob.conavi.sniiv.R;

/**
 * Created by octavio.munguia on 01/09/2015.
 */
public abstract class OfertaBaseFragment extends BaseFragment {
    @Nullable @Bind(R.id.chart) PieChart mChart;
    protected boolean mostrarBoton = false;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_oferta, menu);

        menu.findItem(R.id.action_guardar).setVisible(mostrarBoton);
        menu.findItem(R.id.action_grafica).setVisible(!mostrarBoton);

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

    protected abstract void inicializaDatosChart();
    protected abstract void muestraDialogo();

    protected void guardarChart() {
        if (mChart != null) {
            mChart.saveToGallery(getKey() + System.currentTimeMillis() + ".jpg", 100);
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.mensaje_imagen_guardada, Toast.LENGTH_SHORT).show();
        }
    }
}
