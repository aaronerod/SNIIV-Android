package mx.gob.conavi.sniiv.fragments.oferta;

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
import mx.gob.conavi.sniiv.datos.DatosAvanceObra;
import mx.gob.conavi.sniiv.listeners.OnChartValueSelected;
import mx.gob.conavi.sniiv.modelos.oferta.AvanceObra;
import mx.gob.conavi.sniiv.parsing.ParseAvanceObra;
import mx.gob.conavi.sniiv.sqlite.AvanceObraRepository;

public class AvanceObraFragment extends OfertaBaseFragment<AvanceObra> {
    public static final String TAG = "AvanceObraFragment";
    private boolean errorRetrievingData = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new AvanceObraRepository(getActivity());
    }

    //region Implementaciones BaseFragment
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

    //region Implementaciones OfertaBaseFragment
    protected void inicializaDatosChart() {
        ArrayList<String> pParties =  entidad.getParties();
        long[] pValues = entidad.getValues();
        String pCenterText = "Avance Obra";
        int pEstado = entidad.getCve_ent();
        PieChartBuilder.buildPieChart(mChart, pParties, pValues, pCenterText,
                pEstado, getString(R.string.etiqueta_conavi));
        OnChartValueSelected listener = new OnChartValueSelected(mChart, getKey(), pEstado, pParties);
        mChart.setOnChartValueSelectedListener(listener);
    }

    protected void inicializaDatos() {
        valores =  new String[]{Utils.toString(entidad.getViv_proc_m50()),
                Utils.toString(entidad.getViv_proc_50_99()),
                Utils.toString(entidad.getViv_term_rec()),
                Utils.toString(entidad.getViv_term_ant()),
                Utils.toString(entidad.getTotal())};

        if (fechas != null) {
            String avance = String.format("%s (%s)", getString(R.string.title_avance_obra),
                    fechas.getFecha_vv_ui());
            titulo =  avance;
        } else {
            titulo = getString(R.string.title_avance_obra);
        }
    }

    @Override
    protected String[] getEtiquetas() {
        return new String[]{
                getString(R.string.title_50), getString(R.string.title_99),
                getString(R.string.title_recientes), getString(R.string.title_antiguas),
                getString(R.string.title_total)};
    }

    @Override
    protected Datos<AvanceObra> getDatos(AvanceObra[] datos) {
        return new DatosAvanceObra(getActivity(), datos);
    }
    //endregion
}
