package mx.gob.conavi.sniiv.charts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.templates.ColorTemplate;

/**
 * Created by octavio.munguia on 29/09/2015.
 */
public class LineChartBuilder {
    private static float descriptionTextSize;
    private static float lineWidth;
    private static float circleSize;
    private static float legendTextSize;
    private static final float TABLE_SIZE_FACTOR = 1.7f;

    public static void buildLineChart(Context context, LineChart chart, LineChartConfig config) {
        initSettings(config.getConfiguracion());
        initChart(context, chart, config.getDescription(), config.isShowAcciones());
        setData(chart, config.getxValues(), config.getyValues(), config.getParties());
    }

    private  static void initSettings(String configuration) {
        descriptionTextSize = 12f;
        lineWidth = 2.4f;
        circleSize = 2.0f;
        legendTextSize = 13.3f;

        if ("sw600dp".equals(configuration)) {
            float factor = TABLE_SIZE_FACTOR;
            descriptionTextSize *= factor;
            lineWidth *= factor;
            circleSize *= factor;
            legendTextSize *= factor;
        }
    }

    private static void initChart(Context context, LineChart mChart, String description, boolean showAcciones) {
        mChart.setDescription(description);
        mChart.setDescriptionTextSize(descriptionTextSize);
        mChart.setHighlightEnabled(true);

        MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);
        mChart.setMarkerView(mv);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(MonthsXValueFormatter.getInstance());

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setStartAtZero(true);
        int amountDivide = showAcciones ? Utils.THOUSAND : Utils.MILLION;
        leftAxis.setValueFormatter(AmountYValueFormatter.getInstance(amountDivide));

        mChart.getAxisRight().setEnabled(false);

        mChart.animateX(2500, Easing.EasingOption.EaseInSine);

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.SQUARE);
        l.setTextSize(legendTextSize);
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
            set.setLineWidth(lineWidth);
            set.setCircleSize(circleSize);
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
        mChart.invalidate();
        mChart.notifyDataSetChanged();
    }
}

