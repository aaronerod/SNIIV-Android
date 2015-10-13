package mx.gob.conavi.sniiv.charts;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

import mx.gob.conavi.sniiv.Utils.Utils;

public class MyValueFormatter implements ValueFormatter {
    private static MyValueFormatter instance = null;
    private int amountToDivide = 1;

    protected MyValueFormatter() {}

    public static MyValueFormatter getInstance(int amountToDivide) {
        if(instance == null) {
            instance = new MyValueFormatter();
        }

        instance.amountToDivide = amountToDivide;
        return instance;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return Utils.toStringDivide(Math.ceil(value), amountToDivide);
    }
}
