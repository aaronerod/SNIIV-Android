package mx.gob.conavi.sniiv.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.modelos.ConsultaFinanciamiento;

/**
 * Created by octavio.munguia on 15/09/2015.
 */
public class DatosFinanciamientosDialogFragment extends DialogFragment {
    @Bind(R.id.txtNuevasCofinAcc) TextView txtNuevasCofinAcc;
    @Bind(R.id.txtNuevasCofinMto) TextView txtNuevasCofinMto;
    @Bind(R.id.txtNuevasCredAcc) TextView txtNuevasCredAcc;
    @Bind(R.id.txtNuevasCredMto) TextView txtNuevasCredMto;

    @Bind(R.id.txtUsadasCofinAcc) TextView txtUsadasCofinAcc;
    @Bind(R.id.txtUsadasCofinMto) TextView txtUsadasCofinMto;
    @Bind(R.id.txtUsadasCredAcc) TextView txtUsadasCredAcc;
    @Bind(R.id.txtUsadasCredMto) TextView txtUsadasCredMto;

    @Bind(R.id.txtMejoramientoCofinAcc) TextView txtMejoramientoCofinAcc;
    @Bind(R.id.txtMejoramientoCofinMto) TextView txtMejoramientoCofinMto;
    @Bind(R.id.txtMejoramientoCrediAcc) TextView txtMejoramientoCrediAcc;
    @Bind(R.id.txtMejoramientoCrediMto) TextView txtMejoramientoCrediMto;

    @Bind(R.id.txtOtrosAcc) TextView txtOtrosAcc;
    @Bind(R.id.txtOtrosMto) TextView txtOtrosMto;

    @Bind(R.id.txtTotalAcc) TextView txtTotalAcc;
    @Bind(R.id.txtTotalMto) TextView txtTotalMto;

    @Bind(R.id.txtTitleSubsidios) TextView txtTitleFinanciamientos;

    private String titulo;
    private ConsultaFinanciamiento entidad;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_financiamientos, null);
        ButterKnife.bind(this, view);

        //etiquetas = getArguments().getStringArray("Etiquetas");
        //valores = getArguments().getStringArray("Valores");
        titulo = getArguments().getString("Titulo");

        inicializaDatos();

        builder.setView(view)
                .setNegativeButton(getString(R.string.texto_cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatosFinanciamientosDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private void inicializaDatos() {
        txtTitleFinanciamientos.setText(titulo);

        if(entidad != null) {
            txtNuevasCofinAcc.setText(Utils.toStringDivide(entidad.getViviendasNuevas().getCofinanciamiento().getAcciones()));
            txtNuevasCofinMto.setText(Utils.toStringDivide(entidad.getViviendasNuevas().getCofinanciamiento().getMonto(),1000000));
            txtNuevasCredAcc.setText(Utils.toStringDivide(entidad.getViviendasNuevas().getCreditoIndividual().getAcciones()));
            txtNuevasCredMto.setText(Utils.toStringDivide(entidad.getViviendasNuevas().getCreditoIndividual().getMonto(), 1000000));
            txtUsadasCofinAcc.setText(Utils.toStringDivide(entidad.getViviendasUsadas().getCofinanciamiento().getAcciones()));
            txtUsadasCofinMto.setText(Utils.toStringDivide(entidad.getViviendasUsadas().getCofinanciamiento().getMonto(), 1000000));
            txtUsadasCredAcc.setText(Utils.toStringDivide(entidad.getViviendasUsadas().getCreditoIndividual().getAcciones()));
            txtUsadasCredMto.setText(Utils.toStringDivide(entidad.getViviendasUsadas().getCreditoIndividual().getMonto(), 1000000));
            txtMejoramientoCofinAcc.setText(Utils.toStringDivide(entidad.getMejoramientos().getCofinanciamiento().getAcciones()));
            txtMejoramientoCofinMto.setText(Utils.toStringDivide(entidad.getMejoramientos().getCofinanciamiento().getMonto(), 1000000));
            txtMejoramientoCrediAcc.setText(Utils.toStringDivide(entidad.getMejoramientos().getCreditoIndividual().getAcciones()));
            txtMejoramientoCrediMto.setText(Utils.toStringDivide(entidad.getMejoramientos().getCreditoIndividual().getMonto(), 1000000));
            txtOtrosAcc.setText(Utils.toStringDivide(entidad.getOtrosProgramas().getCreditoIndividual().getAcciones()));
            txtOtrosMto.setText(Utils.toStringDivide(entidad.getOtrosProgramas().getCreditoIndividual().getMonto(), 1000000));
            txtTotalAcc.setText(Utils.toStringDivide(entidad.getTotal().getAcciones()));
            txtTotalMto.setText(Utils.toStringDivide(entidad.getTotal().getMonto(), 1000000));
        }
    }
}
