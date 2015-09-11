package mx.gob.conavi.sniiv.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.datos.Datos;
import mx.gob.conavi.sniiv.datos.DatosAvanceObra;
import mx.gob.conavi.sniiv.datos.DatosReporteGeneral;
import mx.gob.conavi.sniiv.modelos.AvanceObra;
import mx.gob.conavi.sniiv.modelos.Fechas;
import mx.gob.conavi.sniiv.modelos.ReporteGeneral;
import mx.gob.conavi.sniiv.parsing.ParseReporteGeneral;
import mx.gob.conavi.sniiv.sqlite.ReporteGeneralRepository;

/**
 * Created by admin on 04/08/15.
 */
public class ReporteGeneralFragment extends BaseFragment<ReporteGeneral> {
    public static final String TAG = "ReporteGeneralFragment";
    private boolean errorRetrievingData = false;

    @Bind(R.id.txtAccionesFinan) TextView txtAccFinan;
    @Bind(R.id.txtMontoFinan) TextView txtMtoFinan;
    @Bind(R.id.txtAccionesSub) TextView txtAccSub;
    @Bind(R.id.txtMontoSub) TextView txtMtoSub;
    @Bind(R.id.txtVigentes) TextView txtVigentes;
    @Bind(R.id.txtRegistradas) TextView txtRegistradas;
    @Bind(R.id.txtTitleFinanciamientos) TextView txtTitleFinanciamientos;
    @Bind(R.id.txtTitleSubsidios) TextView txtTitleSubsidios;
    @Bind(R.id.txtTitleOfertaVivienda) TextView txtTitleOferta;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ReporteGeneralRepository(getActivity());
    }

    @Override
    protected String getKey() {
        return ReporteGeneral.TABLE;
    }

    @Override
    protected String getFechaAsString(){
        return fechas != null ? fechas.getFecha_subs() : null;
    }

    @Override
    protected AsyncTask<Void, Void, Void> getAsyncTask() {
        return new AsyncTaskRunner();
    }

    public void mostrarDatos() {
        if(entidad != null) {
            txtAccFinan.setText(Utils.toStringDivide(entidad.getAcc_finan()));
            txtMtoFinan.setText(Utils.toStringDivide(entidad.getMto_finan(), 1000000));
            txtAccSub.setText(Utils.toStringDivide(entidad.getAcc_subs()));
            txtMtoSub.setText(Utils.toStringDivide(entidad.getMto_subs(), 1000000));
            txtVigentes.setText(Utils.toStringDivide(entidad.getVv()));
            txtRegistradas.setText(Utils.toStringDivide(entidad.getVr()));
        }

        if(fechas != null) {
            String financiamientos = String.format("%s (%s)", getString(R.string.title_financiamiento),
                   Utils.formatoDiaMes(fechas.getFecha_finan()));
            String subsidios = String.format("%s (%s)", getString(R.string.title_subsidios),
                    Utils.formatoDiaMes(fechas.getFecha_subs()));
            String oferta = String.format("%s (%s)", getString(R.string.title_oferta_vivienda),
                    Utils.formatoMes(fechas.getFecha_vv()));
            txtTitleFinanciamientos.setText(financiamientos);
            txtTitleSubsidios.setText(subsidios);
            txtTitleOferta.setText(oferta);
        }
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    null, getString(R.string.mensaje_espera));
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ParseReporteGeneral parse = new ParseReporteGeneral();
                ReporteGeneral[] reportes = parse.getDatos();
                repository.deleteAll();
                repository.saveAll(reportes);

                datos = new DatosReporteGeneral(getActivity(), reportes);
                entidad = datos.consultaNacional();

                saveTimeLastUpdated(getFechaActualizacion().getTime());

                loadFechasStorage();
            } catch (Exception e) {
                Log.v(TAG, "Error obteniendo datos " + e.getMessage());
                errorRetrievingData = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            if (!errorRetrievingData) {
                habilitaPantalla();
            } else {
                Utils.alertDialogShow(getActivity(), getString(R.string.mensaje_error_datos));
                progressDialog.dismiss();
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reportegeneral;
    }

    @Override
    protected Datos<ReporteGeneral> getDatos(ReporteGeneral[] datos) {
        return new DatosReporteGeneral(getActivity(), datos);
    }
}
