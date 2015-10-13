package mx.gob.conavi.sniiv.charts;

import android.content.Context;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.templates.ColorTemplate;

/**
 * Created by octavio.munguia on 08/10/2015.
 */
public class StackedBarChartBuilder {
    private static float descriptionTextSize;
    private static float legendTextSize;
    private static final float TABLE_SIZE_FACTOR = 1.7f;

    public static void buildLineChart(Context context, BarChart chart, BarChartConfig config) {
        initSettings(config.getConfiguracion());
        initChart(context, chart, config.getDescription(), config.isShowAcciones());
        setData(chart, config.getxValues(), config.getyValues(), config.getParties());
    }

    private  static void initSettings(String configuration) {
        descriptionTextSize = 12f;
        legendTextSize = 13.3f;

        if ("sw600dp".equals(configuration)) {
            float factor = TABLE_SIZE_FACTOR;
            descriptionTextSize *= factor;
            legendTextSize *= factor;
        }
    }

    private static void initChart(Context context, BarChart mChart, String description, boolean showAcciones) {
        mChart.setDescription(description);
        mChart.setDescriptionTextSize(descriptionTextSize);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);
        mChart.setMarkerView(mv);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setStartAtZero(true);
        int amountDivide = showAcciones ? Utils.THOUSAND : Utils.MILLION;
        leftAxis.setValueFormatter(AmountYValueFormatter.getInstance(amountDivide));

        mChart.getAxisRight().setEnabled(false);

        mChart.animateY(1500, Easing.EasingOption.EaseInSine);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);
    }

    private static void setData(BarChart mChart, ArrayList<String> xValues, ArrayList<float[]> yValues,
                                ArrayList<String> parties) {
        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        float max = 0;
        for (int i = 0; i < yValues.size(); i++) {
            float[] arr = yValues.get(i);
            float currentArrayMax = maxValue(arr);
            if (currentArrayMax > max) {
                max = currentArrayMax;
            }

            yVals1.add(new BarEntry(arr, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setColors(getColors(xValues.size()));
        set1.setStackLabels(xValues.toArray(new String[0]));
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        max = (float)(max * 1.1); // max + 10%
        mChart.getAxisLeft().setAxisMaxValue(max);
        BarData data = new BarData(parties, dataSets);
        data.setValueFormatter(new MyValueFormatter());
        mChart.setData(data);
        mChart.invalidate();
        mChart.notifyDataSetChanged();
    }

    private static float maxValue(float[] arr) {
        float maxValue = 0;
        for (float f : arr) {
            maxValue += f;
        }

        return maxValue;
    }

    private static int[] getColors(int size) {
        int stacksize = size;
        int[] colors = new int[stacksize];

        for (int i = 0; i < stacksize; i++) {
            colors[i] = ColorTemplate.VORDIPLOM_COLORS[i];
        }

        return colors;
    }
}
