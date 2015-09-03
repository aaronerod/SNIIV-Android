package mx.gob.conavi.sniiv.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Constants;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.charts.PieChartBuilder;
import mx.gob.conavi.sniiv.listeners.OnChartValueSelected;
import mx.gob.conavi.sniiv.templates.ColorTemplate;

public class OfertaDialogFragment extends DialogFragment {

    private PieChart mChart;
    private String descripcion;
    private int pEstado;
    private ArrayList<String> pParties;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        pParties = getArguments().getStringArrayList(Constants.PARTIES);
        long[] pValues = getArguments().getLongArray(Constants.VALUES);
        String pCenterText = getArguments().getString(Constants.CENTER_TEXT);
        String pYvalLegend = getArguments().getString(Constants.Y_VAL_LEGEND);
        pEstado = getArguments().getInt(Constants.ESTADO);
        descripcion = getArguments().getString(Constants.DESCRIPCION);
        View view = inflater.inflate(R.layout.dialog_grafica_1, null);

        mChart = (PieChart) view.findViewById(R.id.chart);

        PieChartBuilder.buildPieChart(mChart, pParties, pValues, pCenterText, pYvalLegend,
                pEstado, getString(R.string.etiqueta_conavi));
        OnChartValueSelected listener = new OnChartValueSelected(mChart, descripcion, pEstado, pParties);
        mChart.setOnChartValueSelectedListener(listener);
        builder.setView(view)
                .setPositiveButton(getString(R.string.etiqueta_guardar), null)
                .setNegativeButton(getString(R.string.texto_cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OfertaDialogFragment.this.getDialog().cancel();
                    }
                });

        registerForContextMenu(view);
        return builder.create();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mChart.saveToGallery(descripcion + System.currentTimeMillis() + ".jpg", 100);
                    Toast.makeText(getActivity().getApplicationContext(),
                            R.string.mensaje_imagen_guardada, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

