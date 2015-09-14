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
import mx.gob.conavi.sniiv.datos.DatosValorVivienda;
import mx.gob.conavi.sniiv.listeners.OnChartValueSelected;
import mx.gob.conavi.sniiv.modelos.ValorVivienda;
import mx.gob.conavi.sniiv.parsing.ParseValorVivienda;
import mx.gob.conavi.sniiv.sqlite.ValorViviendaRepository;

/**
 * Created by admin on 04/08/15.
 */
public class ValorViviendaFragment extends OfertaBaseFragment<ValorVivienda> {
    public static final String TAG = "ValorViviendaFragment";
    private boolean errorRetrievingData = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ValorViviendaRepository(getActivity());
    }

    //region Implementaciones BaseFragment
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

    //region Implementaciones OfertaBaseFragment
    protected void inicializaDatosChart() {
        ArrayList<String> pParties =  entidad.getParties();
        long[] pValues = entidad.getValues();
        String pCenterText = "Valor de Vivienda";
        int pEstado = entidad.getCve_ent();
        PieChartBuilder.buildPieChart(mChart, pParties, pValues, pCenterText,
                pEstado, getString(R.string.etiqueta_conavi));
        OnChartValueSelected listener = new OnChartValueSelected(mChart, getKey(), pEstado, pParties);
        mChart.setOnChartValueSelectedListener(listener);
    }

    protected void inicializaDatos() {
        valores =  new String[]{Utils.toString(entidad.getEconomica()),
                Utils.toString(entidad.getPopular()),
                Utils.toString(entidad.getTradicional()),
                Utils.toString(entidad.getMedia_residencial()),
                Utils.toString(entidad.getTotal())};

        if (fechas != null) {
            String valor = String.format("%s (%s)", getString(R.string.title_valor_vivienda),
                    Utils.formatoMes(fechas.getFecha_vv()));
            titulo =  valor;
        } else {
            titulo = getString(R.string.title_avance_obra);
        }
    }

    @Override
    protected String[] getEtiquetas() {
        return new String[]{
                getString(R.string.title_economica), getString(R.string.title_popular),
                getString(R.string.title_tradicional), getString(R.string.title_media_residencial),
                getString(R.string.title_total)};
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_oferta;
    }

    @Override
    protected Datos<ValorVivienda> getDatos(ValorVivienda[] datos) {
        return new DatosValorVivienda(getActivity(), datos);
    }
    //endregion
}
