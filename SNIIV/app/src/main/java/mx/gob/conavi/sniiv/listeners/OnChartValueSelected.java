package mx.gob.conavi.sniiv.listeners;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;

/**
 * Created by octavio.munguia on 03/09/2015.
 */
public class OnChartValueSelected implements OnChartValueSelectedListener {
    protected PieChart mChart;
    protected String descripcion;
    protected int pEstado;
    protected ArrayList<String> pParties;

    public OnChartValueSelected(PieChart mChart, String descripcion, int pEstado, ArrayList<String> pParties) {
        this.mChart = mChart;
        this.descripcion = descripcion;
        this.pEstado = pEstado;
        this.pParties = pParties;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        mChart.setCenterText(descripcion + "\n " + Utils.listEdo[pEstado] + "\n" +  Utils.toString(e.getVal()) + " " + pParties.get(e.getXIndex()));
    }

    @Override
    public void onNothingSelected() {
        mChart.setCenterText(descripcion + "\n " + Utils.listEdo[pEstado]);
    }
}
