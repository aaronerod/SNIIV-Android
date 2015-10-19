package mx.gob.conavi.sniiv.fragments.demanda;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;

/**
 * Created by octavio.munguia on 14/10/2015.
 */
public class DatosDemandaMunicipalDialogFragment extends DialogFragment {
    public static final String TAG = "DatosDemandaMunicipalDialogFragment";
    @Bind(R.id.tableLayout) TableLayout tableLayout;
    @Bind(R.id.txtTitle) TextView txtTitle;
    private String titulo;
    private ArrayList<String> parties;
    private ArrayList<float[]> yValues;
    private String[] etiquetas;

    public static DatosDemandaMunicipalDialogFragment newInstance(String titulo,
               ArrayList<String> parties, ArrayList<float[]> yValues, String[] etiquetas) {
        DatosDemandaMunicipalDialogFragment dialogFragment = new DatosDemandaMunicipalDialogFragment();
        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putStringArrayList("parties", parties);
        args.putInt("numValues", yValues.size());
        args.putStringArray("etiquetas", etiquetas);
        for (int i = 0; i < yValues.size(); i++) {
            float[] values = yValues.get(i);
            args.putFloatArray("values" + i, values);
        }

        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.dialog_demanda_municipal_finan, container, false);
        ButterKnife.bind(this, root);
        titulo = getArguments().getString("titulo");
        parties = getArguments().getStringArrayList("parties");
        recortaNombres(parties);
        etiquetas = getArguments().getStringArray("etiquetas");

        yValues = new ArrayList<>();
        int numValues = getArguments().getInt("numValues");
        for (int i = 0; i < numValues; i++) {
            yValues.add(getArguments().getFloatArray("values" + i));
        }

        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        creaTableLayout();
    }

    protected void creaTableLayout() {
        txtTitle.setText(titulo);

        /*Log.v("Child count: ", tableLayout.getChildCount() + "");
        if (tableLayout.getChildCount() > 1) {
            tableLayout.removeViews(1, etiquetas.length);
        }*/

        TableRow headers = (TableRow) tableLayout.getChildAt(0);
        TextView txtMun1 = (TextView) headers.findViewById(R.id.txtMunicipio1);
        txtMun1.setText(parties.get(0));
        TextView txtMun2 = (TextView) headers.findViewById(R.id.txtMunicipio2);
        txtMun2.setText(parties.get(1));
        TextView txtMun3 = (TextView) headers.findViewById(R.id.txtMunicipio3);
        txtMun3.setText(parties.get(2));
        TextView txtOtros = (TextView) headers.findViewById(R.id.txtOtros);
        txtOtros.setText(parties.get(3));


        for (int i = 0; i< etiquetas.length; i++) {
            TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.table_row_demanda_municipal, null);
            TextView etiqueta = (TextView) row.findViewById(R.id.txtTitulo);
            etiqueta.setText(etiquetas[i]);

            txtMun1 = (TextView) row.findViewById(R.id.txtMunicipio1);
            txtMun1.setText(String.format("%.0f", yValues.get(0)[i]));

            txtMun2 = (TextView) row.findViewById(R.id.txtMunicipio2);
            txtMun2.setText(String.format("%.0f", yValues.get(1)[i]));

            txtMun3 = (TextView) row.findViewById(R.id.txtMunicipio3);
            txtMun3.setText(String.format("%.0f", yValues.get(2)[i]));

            txtOtros = (TextView) row.findViewById(R.id.txtMunicipioOtros);
            txtOtros.setText(String.format("%.0f", yValues.get(3)[i]));

            tableLayout.addView(row);
        }
    }

    private void recortaNombres(ArrayList<String> parties) {
        for(int i = 0; i < parties.size(); i++) {
            if (parties.get(i).length() > 8) {
                parties.set(i, parties.get(i).substring(0, 8));
            }
        }
    }
}
