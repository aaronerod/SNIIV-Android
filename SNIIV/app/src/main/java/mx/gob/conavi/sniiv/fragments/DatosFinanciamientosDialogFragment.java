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
    private long[] acciones;
    private double[] montos;

    public static DatosFinanciamientosDialogFragment newInstance(String titulo, long[] acciones, double[] montos) {
        DatosFinanciamientosDialogFragment frag = new DatosFinanciamientosDialogFragment();
        Bundle args = new Bundle();

        args.putString("titulo", titulo);
        args.putLongArray("acciones", acciones);
        args.putDoubleArray("montos", montos);
        frag.setArguments(args);
        return frag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_financiamientos, container, false);
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

    private void inicializaDatos() {
        txtTitleFinanciamientos.setText(titulo);

        if(acciones != null && montos != null) {
            txtNuevasCofinAcc.setText(Utils.toStringDivide(acciones[0]));
            txtNuevasCofinMto.setText(Utils.toStringDivide(montos[0],1000000));
            txtNuevasCredAcc.setText(Utils.toStringDivide(acciones[1]));
            txtNuevasCredMto.setText(Utils.toStringDivide(montos[1], 1000000));
            txtUsadasCofinAcc.setText(Utils.toStringDivide(acciones[2]));
            txtUsadasCofinMto.setText(Utils.toStringDivide(montos[2], 1000000));
            txtUsadasCredAcc.setText(Utils.toStringDivide(acciones[3]));
            txtUsadasCredMto.setText(Utils.toStringDivide(montos[3], 1000000));
            txtMejoramientoCofinAcc.setText(Utils.toStringDivide(acciones[4]));
            txtMejoramientoCofinMto.setText(Utils.toStringDivide(montos[4], 1000000));
            txtMejoramientoCrediAcc.setText(Utils.toStringDivide(acciones[5]));
            txtMejoramientoCrediMto.setText(Utils.toStringDivide(montos[5], 1000000));
            txtOtrosAcc.setText(Utils.toStringDivide(acciones[6]));
            txtOtrosMto.setText(Utils.toStringDivide(montos[6], 1000000));
            txtTotalAcc.setText(Utils.toStringDivide(acciones[7]));
            txtTotalMto.setText(Utils.toStringDivide(montos[7], 1000000));
        }
    }
}
