package mx.gob.conavi.sniiv.listeners;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import mx.gob.conavi.sniiv.Utils.Utils;

/**
 * Created by octavio.munguia on 03/09/2015.
 */
public class OnChartValueSelected implements OnChartValueSelectedListener {
    private PieChart mChart;
    private String descripcion;
    private int pEstado;
    private ArrayList<String> pParties;

    public OnChartValueSelected(PieChart mChart, String descripcion, int pEstado, ArrayList<String> pParties) {
        this.mChart = mChart;
        this.descripcion = descripcion;
        this.pEstado = pEstado;
        this.pParties = pParties;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        mChart.setCenterText(descripcion + "\n " + Utils.listEdo[pEstado] + "\n" + NumberFormat.getNumberInstance(Locale.US).format((int) e.getVal()) +" " + pParties.get(e.getXIndex()));
    }

    @Override
    public void onNothingSelected() {
        mChart.setCenterText(descripcion + "\n " + Utils.listEdo[pEstado]);
    }
}
