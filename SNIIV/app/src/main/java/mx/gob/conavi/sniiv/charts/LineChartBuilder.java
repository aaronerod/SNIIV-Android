package mx.gob.conavi.sniiv.charts;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.templates.ColorTemplate;

/**
 * Created by octavio.munguia on 29/09/2015.
 */
public class LineChartBuilder {

    public static void buildPieChart(LineChart chart, ArrayList<String> parties, int[] xValues,
                                     ArrayList<double[]> yValues, String description){
        initChart(chart, description);
        setData(chart, xValues, yValues, parties);
    }

    private static void initChart(LineChart mChart, String description) {
        mChart.setDescription(description);
        mChart.setDescriptionTextSize(18f);
        mChart.setNoDataTextDescription("You need to provide data for the mChart.");
        mChart.setHighlightEnabled(true);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(MonthsXValueFormatter.getInstance());

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setStartAtZero(true);
        leftAxis.setValueFormatter(AmountYValueFormatter.getInstance(Utils.THOUSAND));

        mChart.getAxisRight().setEnabled(false);

        mChart.animateX(2500, Easing.EasingOption.EaseInSine);

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.SQUARE);
    }

    private static void setData(LineChart mChart, int[] xValues, ArrayList<double[]> yValues, ArrayList<String> parties) {
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < xValues.length; i++) {
            xVals.add(xValues[i] + "");
        }

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        double maxValue = 0;
        for (int i = 0; i < yValues.size(); i++) {
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int j = 0; j < yValues.get(i).length; j++) {
                double[] values = yValues.get(i);
                if (values[j] > maxValue) {
                    maxValue = values[j];
                }
                if ((int)values[j] > 0) {
                    yVals.add(new Entry((float) values[j], xValues[j]));
                }
            }

            LineDataSet set = new LineDataSet(yVals, parties.get(i));
            set.setLineWidth(2.5f);
            set.setCircleSize(2.1f);
            set.setDrawCircleHole(false);
            set.setDrawValues(false);
            set.setDrawCubic(true);

            set.setColor(ColorTemplate.EVOLUCION_COLORS[i]);
            set.setCircleColor(ColorTemplate.EVOLUCION_COLORS[i]);

            dataSets.add(set);
        }

        mChart.getAxisLeft().setAxisMaxValue((float) maxValue);

        LineData data = new LineData(xVals, dataSets);
        mChart.setData(data);
    }
}

