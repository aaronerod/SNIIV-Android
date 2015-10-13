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
import mx.gob.conavi.sniiv.datos.DatosPCU;
import mx.gob.conavi.sniiv.listeners.OnChartValueSelected;
import mx.gob.conavi.sniiv.modelos.oferta.PCU;
import mx.gob.conavi.sniiv.parsing.ParsePCU;
import mx.gob.conavi.sniiv.sqlite.PCURepository;


public class PCUFragment extends OfertaBaseFragment<PCU> {
    public static final String TAG = "PCUFragment";

    private boolean errorRetrievingData = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new PCURepository(getActivity());
    }

    //region Implementaciones BaseFragment
    @Override
    protected AsyncTask<Void, Void, Void> getAsyncTask() {
        return new AsyncTaskRunner();
    }

    @Override
    protected String getKey() {
        return PCU.TABLE;
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
                ParsePCU parse = new ParsePCU();
                PCU[] datosParse = parse.getDatos();
                repository.deleteAll();
                repository.saveAll(datosParse);

                datos = new DatosPCU(getActivity(), datosParse);
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
        String pCenterText = "PCU";
        int pEstado = entidad.getCve_ent();
        PieChartBuilder.buildPieChart(mChart, pParties, pValues, pCenterText,
                pEstado, getString(R.string.etiqueta_conavi));
        OnChartValueSelected listener = new OnChartValueSelected(mChart, getKey(), pEstado, pParties);
        mChart.setOnChartValueSelectedListener(listener);
    }

    protected void inicializaDatos() {
        valores =  new String[]{Utils.toString(entidad.getU1()),
                Utils.toString(entidad.getU2()),
                Utils.toString(entidad.getU3()),
                Utils.toString(entidad.getNd()),
                Utils.toString(entidad.getTotal())};

        if (fechas != null) {
            String pcu = String.format("%s (%s)", getString(R.string.title_pcu),
                    fechas.getFecha_vv_ui());
            titulo =  pcu;
        } else {
            titulo = getString(R.string.title_pcu);
        }
    }

    @Override
    protected String[] getEtiquetas() {
        return new String[]{
                getString(R.string.title_u1), getString(R.string.title_u2),
                getString(R.string.title_u3), getString(R.string.title_nd),
                getString(R.string.title_total)};
    }

    @Override
    protected Datos<PCU> getDatos(PCU[] datos) {
        return new DatosPCU(getActivity(), datos);
    }
    //endregion
}
