package mx.gob.conavi.sniiv.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.charts.PieChartBuilder;
import mx.gob.conavi.sniiv.datos.Datos;
import mx.gob.conavi.sniiv.datos.DatosTipoVivienda;
import mx.gob.conavi.sniiv.listeners.OnChartValueSelected;
import mx.gob.conavi.sniiv.modelos.TipoVivienda;
import mx.gob.conavi.sniiv.parsing.ParseTipoVivienda;
import mx.gob.conavi.sniiv.sqlite.TipoViviendaRepository;

/**
 * Created by admin on 04/08/15.
 */
public class TipoViviendaFragment extends OfertaBaseFragment<TipoVivienda> {
    public static final String TAG = "TipoViviendaFragment";
    private boolean errorRetrievingData = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new TipoViviendaRepository(getActivity());
    }

    //region Implementaciones BaseFragment
    @Override
    protected AsyncTask<Void, Void, Void> getAsyncTask() {
        return new AsyncTaskRunner();
    }

    @Override
    protected String getKey() {
        return TipoVivienda.TABLE;
    }

    @Override
    protected String getFechaAsString() {
        return fechas != null ? fechas.getFecha_vv() : null;
    }
    //endregion

    protected class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    null, getString(R.string.mensaje_espera));
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ParseTipoVivienda parse = new ParseTipoVivienda();
                TipoVivienda[] datosParse = parse.getDatos();
                repository.deleteAll();
                repository.saveAll(datosParse);

                datos = new DatosTipoVivienda(getActivity(), datosParse);
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

    //region Implementaciones OfertaBaseFragment
    protected void inicializaDatosChart() {
        ArrayList<String> pParties =  entidad.getParties();
        long[] pValues = entidad.getValues();
        String pCenterText = "Tipo de Vivienda";
        int pEstado = entidad.getCve_ent();
        PieChartBuilder.buildPieChart(mChart, pParties, pValues, pCenterText,
                pEstado, getString(R.string.etiqueta_conavi));
        OnChartValueSelected listener = new OnChartValueSelected(mChart, getKey(), pEstado, pParties);
        mChart.setOnChartValueSelectedListener(listener);
    }

    protected void inicializaDatos() {
        valores =  new String[]{Utils.toString(entidad.getHorizontal()),
                Utils.toString(entidad.getVertical()),
                Utils.toString(entidad.getTotal())};

        if (fechas != null) {
            String tipo = String.format("%s (%s)", getString(R.string.title_tipo_vivienda),
                    Utils.formatoMes(fechas.getFecha_vv()));
            titulo =  tipo;
        } else {
            titulo = getString(R.string.title_avance_obra);
        }
    }

    @Override
    protected String[] getEtiquetas() {
        return new String[]{
                getString(R.string.title_horizontal), getString(R.string.title_vertical),
                getString(R.string.title_total)};
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_oferta;
    }

    @Override
    protected Datos<TipoVivienda> getDatos(TipoVivienda[] datos) {
        return new DatosTipoVivienda(getActivity(), datos);
    }
    //endregion
}
