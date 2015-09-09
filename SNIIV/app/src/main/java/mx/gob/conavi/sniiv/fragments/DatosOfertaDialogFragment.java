package mx.gob.conavi.sniiv.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;

/**
 * Created by octavio.munguia on 09/09/2015.
 */
public class DatosOfertaDialogFragment extends DialogFragment{
    @Bind(R.id.tableLayout) TableLayout tableLayout;
    @Bind(R.id.txtTitulo) TextView txtTitle;
    private String[] etiquetas;
    private String[] valores;
    private String titulo;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_datos, null);
        ButterKnife.bind(this, view);

        etiquetas = getArguments().getStringArray("Etiquetas");
        valores = getArguments().getStringArray("Valores");
        titulo = getArguments().getString("Titulo");

        inicializaDatos();

        builder.setView(view)
                .setNegativeButton(getString(R.string.texto_cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatosOfertaDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private void inicializaDatos() {
        txtTitle.setText(titulo);

        for (int i = 0; i< etiquetas.length; i++) {
            TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.table_row, null);
            TextView etiqueta = (TextView) row.findViewById(R.id.txtEtiqueta);
            TextView valor = (TextView) row.findViewById(R.id.txtValor);
            etiqueta.setText(etiquetas[i]);
            valor.setText(valores[i]);

            tableLayout.addView(row);
        }
    }
}
