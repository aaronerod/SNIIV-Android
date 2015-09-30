package mx.gob.conavi.sniiv.charts;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

/**
 * Created by octavio.munguia on 30/09/2015.
 */
public class AmountYValueFormatter implements YAxisValueFormatter {
    private static AmountYValueFormatter instance = null;
    private int amountToDivide = 1;

    protected AmountYValueFormatter() {}

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return ((int)value / amountToDivide) + "";
    }

    public static AmountYValueFormatter getInstance(int amountToDivide) {
        if(instance == null) {
            instance = new AmountYValueFormatter();
            instance.amountToDivide = amountToDivide;
        }

        return instance;
    }
}
