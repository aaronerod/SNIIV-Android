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
import android.widget.Toast;


import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.animation.Easing;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.templates.ColorTemplate;

import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;


import mx.gob.conavi.sniiv.R;


public class OfertaDialogFragment extends DialogFragment implements
        OnChartValueSelectedListener
{

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

        pParties=getArguments().getStringArrayList("parties");
        pValues=getArguments().getLongArray("values");
        pCenterText=getArguments().getString("centerText");
        pYvalLegend=getArguments().getString("yValLegend");
        pEstado=getArguments().getInt("estado");

        view= inflater.inflate(R.layout.dialog_grafica_1, null);
        mChart = (PieChart) view.findViewById(R.id.chart);


        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        mChart.saveToGallery(pCenterText + System.currentTimeMillis()+".jpg", 100);
                        Toast toast2 =
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Imágen Guardada en Galeria", Toast.LENGTH_SHORT);

                        toast2.setGravity(Gravity.CENTER|Gravity.LEFT,0,0);

                        toast2.show();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
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
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Guardar");

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle()=="Guardar"){
            mChart.saveToPath("title" + System.currentTimeMillis(), "");
        }

        return true;
    }



    public void initChart(String centerText, int estado) {

        mChart.setUsePercentValues(true);
        mChart.setDescription("Comisión Nacional de Vivienda");
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
        mChart.setCenterText(centerText+"\n "+ Utils.listEdo[estado]);
        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);



    }




    private void setData(String yLegend, long[] values, ArrayList<String> parties) {

        float mult = 100;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();



        for(int a=0; a<values.length;a++)
            yVals1.add(new Entry((float) (values[a]),a));


        ArrayList<String> xVals = new ArrayList<String>();


        for(int a=0; a<parties.size();a++)
            xVals.add(parties.get(a));




        PieDataSet dataSet = new PieDataSet(yVals1, yLegend);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.CONAVI_COLORS)
            colors.add(c);

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

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }


}

