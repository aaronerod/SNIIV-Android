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

        mChart.animateY(2500, Easing.EasingOption.EaseInSine);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);
    }

    private static void setData(BarChart mChart, ArrayList<String> xValues, ArrayList<float[]> yValues,
                                ArrayList<String> parties) {
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < xValues.size(); i++) {
            xVals.add(xValues.get(i));
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        double maxValue = 0;
        for (int i = 0; i < yValues.size(); i++) {
            yVals1.add(new BarEntry(yValues.get(i), i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setColors(ColorTemplate.EVOLUCION_COLORS);
        set1.setStackLabels(parties.toArray(new String[0]));
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        mChart.getAxisLeft().setAxisMaxValue((float) maxValue);

        BarData data = new BarData(xVals, dataSets);
        mChart.setData(data);
        mChart.invalidate();
        mChart.notifyDataSetChanged();
    }
}
