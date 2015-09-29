package mx.gob.conavi.sniiv.charts;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

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
        mChart.setNoDataTextDescription("You need to provide data for the mChart.");
        mChart.setHighlightEnabled(true);

        XAxis xAxis = mChart.getXAxis();
        XAxisValueFormatter formatter = new XAxisValueFormatter() {
            @Override
            public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
                String[] months = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul",
                        "Ago", "Sep", "Oct", "Nov", "Dic"};

                return months[index];
            }
        };
        xAxis.setValueFormatter(formatter);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setStartAtZero(true);

        YAxisValueFormatter yFormatter = new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return ((int)value / 1000) + "";
            }
        };
        leftAxis.setValueFormatter(yFormatter);

        //leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        mChart.animateX(2500, Easing.EasingOption.EaseInCubic);
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.SQUARE);
    }

    private static void setData(LineChart mChart, int[] xValues, ArrayList<double[]> yValues, ArrayList<String> parties) {
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < xValues.length; i++) {
            xVals.add(xValues[i] + "");
        }


        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
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

            LineDataSet set1 = new LineDataSet(yVals, parties.get(i));
            set1.setLineWidth(2f);
            set1.setCircleSize(2.1f);
            set1.setDrawCircleHole(false);
            set1.setDrawValues(false);
            set1.setDrawCubic(true);

            set1.setColor(ColorTemplate.EVOLUCION_COLORS[i]);
            set1.setCircleColor(ColorTemplate.EVOLUCION_COLORS[i]);

            dataSets.add(set1);
        }

        mChart.getAxisLeft().setAxisMaxValue((float)maxValue);
        Log.i("maxValue: ", maxValue + "");

        LineData data = new LineData(xVals, dataSets);
        mChart.setData(data);
    }
}

