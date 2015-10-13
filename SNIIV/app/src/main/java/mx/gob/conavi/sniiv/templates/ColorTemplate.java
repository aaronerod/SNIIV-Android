package mx.gob.conavi.sniiv.templates;


import android.content.res.Resources;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;


public class ColorTemplate {

    /**
     * an "invalid" color that indicates that no color is set
     */
    public static final int COLOR_NONE = -1;

    /**
     * this "color" is used for the Legend creation and indicates that the next
     * form should be skipped
     */
    public static final int COLOR_SKIP = -2;

    /**
     * THE COLOR THEMES ARE PREDEFINED (predefined color integer arrays), FEEL
     * FREE TO CREATE YOUR OWN WITH AS MANY DIFFERENT COLORS AS YOU WANT
     */
    public static final int[] LIBERTY_COLORS = {
            Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
            Color.rgb(118, 174, 175), Color.rgb(42, 109, 130)
    };
    public static final int[] JOYFUL_COLORS = {
            Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
            Color.rgb(106, 167, 134), Color.rgb(53, 194, 209)
    };
    public static final int[] PASTEL_COLORS = {
            Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134), Color.rgb(179, 48, 80)
    };
    public static final int[] COLORFUL_COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)
    };
    public static final int[] VORDIPLOM_COLORS = {
            Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255), Color.rgb(255, 140, 157),

            Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134), Color.rgb(179, 48, 80)
    };

    public static final int[] CONAVI_COLORS = {
            Color.rgb(154,205,50),
            Color.rgb(85,107,47),
            Color.rgb(107,142,35),
            Color.rgb(0,100,0),
            Color.rgb(34,139,34),
            Color.rgb(50,205,50),
            Color.rgb(152,251,152),
            Color.rgb(60,179,113)
    };

    public static final int[] EVOLUCION_COLORS = {
            Color.rgb(142,144,144),
            Color.rgb(155,27,47),
            Color.rgb(0,128,59)
    };

    /**
     * Returns the Android ICS holo blue light color.
     *
     * @return
     */
    public static int getHoloBlue() {
        return Color.rgb(51, 181, 229);
    }

    /**
     * turn an array of resource-colors (contains resource-id integers) into an
     * array list of actual color integers
     *
     * @param r
     * @param colors an integer array of resource id's of colors
     * @return
     */
    public static List<Integer> createColors(Resources r, int[] colors) {

        List<Integer> result = new ArrayList<Integer>();

        for (int i : colors) {
            result.add(r.getColor(i));
        }

        return result;
    }

    /**
     * Turns an array of colors (integer color values) into an ArrayList of
     * colors.
     *
     * @param colors
     * @return
     */
    public static List<Integer> createColors(int[] colors) {

        List<Integer> result = new ArrayList<Integer>();

        for (int i : colors) {
            result.add(i);
        }

        return result;
    }
}
