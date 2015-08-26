package mx.gob.conavi.sniiv.fragments;



import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.templates.ColorTemplate;


public class OfertaDialogFragment extends DialogFragment implements
        OnChartValueSelectedListener {

    private PieChart mChart;
    private  View view;
    private ArrayList<String> pParties;
    private long[] pValues;
    private String pCenterText;
    private String pYvalLegend;
    private int pEstado;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        pParties = getArguments().getStringArrayList("parties");
        pValues = getArguments().getLongArray("values");
        pCenterText = getArguments().getString("centerText");
        pYvalLegend = getArguments().getString("yValLegend");
        pEstado = getArguments().getInt("estado");
        view= inflater.inflate(R.layout.dialog_grafica_1, null);

        mChart = (PieChart) view.findViewById(R.id.chart);


        builder.setView(view)
                .setPositiveButton(getString(R.string.etiqueta_guardar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(getString(R.string.texto_cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OfertaDialogFragment.this.getDialog().cancel();
                    }
                });

        initChart(pCenterText, pEstado);
        setData(pYvalLegend, pValues, pParties);
        registerForContextMenu(view);

        return builder.create();
    }


    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    mChart.saveToGallery(pCenterText + System.currentTimeMillis() + ".jpg", 100);
                    Toast toast2 =
                            Toast.makeText(getActivity().getApplicationContext(),
                                    R.string.mensaje_imagen_guardada, Toast.LENGTH_SHORT);


                    toast2.show();
                    Boolean wantToCloseDialog = false;
                    //Do stuff, possibly set wantToCloseDialog to true then...
                    if (wantToCloseDialog)
                        d.dismiss();
                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, getString(R.string.etiqueta_guardar));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(getString(R.string.etiqueta_guardar))){
            mChart.saveToPath("title" + System.currentTimeMillis(), "");
        }

        return true;
    }

    public void initChart(String centerText, int estado) {
        mChart.setUsePercentValues(true);
        mChart.setDescription(getString(R.string.etiqueta_conavi));
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(false);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setCenterText(centerText + "\n " + Utils.listEdo[estado]);
        mChart.setLongClickable(true);
        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void setData(String yLegend, long[] values, ArrayList<String> parties) {
        ArrayList<Entry> yVals1 = new ArrayList<>();

        for(int a = 0; a<values.length; a++) {
            yVals1.add(new Entry((float) (values[a]), a));
        }

        ArrayList<String> xVals = new ArrayList<>();

        for(int a = 0; a<parties.size(); a++) {
            xVals.add(parties.get(a));
        }

        PieDataSet dataSet = new PieDataSet(yVals1, yLegend);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.CONAVI_COLORS) {
            colors.add(c);
        }

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        // not implemented
    }

    @Override
    public void onNothingSelected() {
        // not implemented
    }
}

