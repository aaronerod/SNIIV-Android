package mx.gob.conavi.sniiv.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;

/**
 * Created by octavio.munguia on 18/09/2015.
 */
public class DatosSubsidiosDialogFragment extends DialogFragment {

    @Bind(R.id.txtNuevaAcc) TextView txtNuevaAcc;
    @Bind(R.id.txtNuevaMto) TextView txtNuevaMto;
    @Bind(R.id.txtUsadaAcc) TextView txtUsadaAcc;
    @Bind(R.id.txtUsadaMto) TextView txtUsadaMto;
    @Bind(R.id.txtAutoproduccionAcc) TextView txtAutoproduccionAcc;
    @Bind(R.id.txtAutoproduccionMto) TextView txtAutoproduccionMto;
    @Bind(R.id.txtMejoramientoAcc) TextView txtMejoramientoAcc;
    @Bind(R.id.txtMejoramientoMto) TextView txtMejoramientoMto;
    @Bind(R.id.txtLotesAcc) TextView txtLotesAcc;
    @Bind(R.id.txtLotesMto) TextView txtLotesMto;
    @Bind(R.id.txtOtrosAcc) TextView txtOtrosAcc;
    @Bind(R.id.txtOtrosMto) TextView txtOtrosMto;
    @Bind(R.id.txtTotalAcc) TextView txtTotalAcc;
    @Bind(R.id.txtTotalMto) TextView txtTotalMto;
    @Bind(R.id.txtTitleSubsidios) TextView txtTitleSubsidios;

    private String titulo;
    private long[] acciones;
    private double[] montos;

    public static DatosSubsidiosDialogFragment newInstance(String titulo, long[] acciones, double[] montos) {
        DatosSubsidiosDialogFragment frag = new DatosSubsidiosDialogFragment();
        Bundle args = new Bundle();

        args.putString("titulo", titulo);
        args.putLongArray("acciones", acciones);
        args.putDoubleArray("montos", montos);
        frag.setArguments(args);
        return frag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_subsidios, container, false);
        ButterKnife.bind(this, root);
        titulo = getArguments().getString("titulo");
        acciones = getArguments().getLongArray("acciones");
        montos = getArguments().getDoubleArray("montos");
        inicializaDatos();

        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    public void actualizaDatos(String titulo, long[] acciones, double[] montos) {
        getArguments().putString("titulo", titulo);
        getArguments().putLongArray("acciones", acciones);
        getArguments().putDoubleArray("montos", montos);
    }

    private void inicializaDatos() {
        txtTitleSubsidios.setText(titulo);

        if(acciones != null && montos != null) {
            txtNuevaAcc.setText(Utils.toStringDivide(acciones[0]));
            txtNuevaMto.setText(Utils.toStringDivide(montos[0],1000000));
            txtUsadaAcc.setText(Utils.toStringDivide(acciones[1]));
            txtUsadaMto.setText(Utils.toStringDivide(montos[1],1000000));
            txtAutoproduccionAcc.setText(Utils.toStringDivide(acciones[2]));
            txtAutoproduccionMto.setText(Utils.toStringDivide(montos[2],1000000));
            txtMejoramientoAcc.setText(Utils.toStringDivide(acciones[3]));
            txtMejoramientoMto.setText(Utils.toStringDivide(montos[3],1000000));

            txtLotesAcc.setText(Utils.toStringDivide(acciones[4]));
            txtLotesMto.setText(Utils.toStringDivide(montos[4],1000000));

            txtOtrosAcc.setText(Utils.toStringDivide(acciones[5]));
            txtOtrosMto.setText(Utils.toStringDivide(montos[5],1000000));

            txtTotalAcc.setText(Utils.toStringDivide(acciones[6]));
            txtTotalMto.setText(Utils.toStringDivide(montos[6],1000000));
        }
    }
}