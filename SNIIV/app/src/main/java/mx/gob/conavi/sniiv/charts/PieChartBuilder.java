package mx.gob.conavi.sniiv.charts;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.templates.ColorTemplate;

/**
 * Created by octavio.munguia on 01/09/2015.
 */
public class PieChartBuilder {

    public static final double MIN_PERCENTAGE = 2.0;

    public static void buildPieChart(PieChart chart, ArrayList<String> parties, long[] values,
                                     String centerText, int estado, String description){
        initChart(chart, centerText, estado, description);
        setData(chart, values, parties);
    }

    private static void initChart(PieChart chart, String centerText, int estado, String description) {
        chart.setUsePercentValues(true);
        chart.setDescription(description);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColorTransparent(true);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        chart.setRotationEnabled(false);
        chart.setCenterText(centerText + "\n " + Utils.listEdo[estado]);
        chart.setLongClickable(true);
        chart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(2f);
    }

    private static void setData(PieChart chart, long[] values, ArrayList<String> parties) {
        ArrayList<Entry> yVals1 = new ArrayList<>();

        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }

        // Solo añade los valores mayores a 2%
        ArrayList<String> xVals = new ArrayList<>();
        for(int a = 0; a < values.length; a++) {
            double calculatedPercent = (values[a] / sum * 100);
            if (calculatedPercent > MIN_PERCENTAGE) {
                yVals1.add(new Entry((float) (values[a]), a));
                xVals.add(parties.get(a));
            }
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "");
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
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        chart.highlightValues(null);

        chart.notifyDataSetChanged();
        chart.invalidate();
    }
}
