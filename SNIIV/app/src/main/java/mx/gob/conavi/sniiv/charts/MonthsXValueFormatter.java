package mx.gob.conavi.sniiv.charts;

import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by octavio.munguia on 30/09/2015.
 */
public class MonthsXValueFormatter implements XAxisValueFormatter {
    private static MonthsXValueFormatter instance = null;

    private static final String[] months = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul",
            "Ago", "Sep", "Oct", "Nov", "Dic"};

    protected MonthsXValueFormatter() {}


    @Override
    public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
        return months[index];
    }

    public static MonthsXValueFormatter getInstance() {
        if(instance == null) {
            instance = new MonthsXValueFormatter();
        }

        return instance;
    }
}
