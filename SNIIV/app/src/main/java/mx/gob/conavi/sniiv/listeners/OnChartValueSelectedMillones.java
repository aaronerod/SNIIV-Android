package mx.gob.conavi.sniiv.listeners;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;

/**
 * Created by octavio.munguia on 21/09/2015.
 */
public class OnChartValueSelectedMillones extends OnChartValueSelected {
    public OnChartValueSelectedMillones(PieChart mChart, String descripcion, int pEstado, ArrayList<String> pParties) {
        super(mChart, descripcion, pEstado, pParties);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        mChart.setCenterText(descripcion + "\n " + Utils.listEdo[pEstado] + "\n"
                + Utils.toStringDivide(e.getVal(), 1000000) + " MDP" +
                " \n" + pParties.get(e.getXIndex()));
    }
}