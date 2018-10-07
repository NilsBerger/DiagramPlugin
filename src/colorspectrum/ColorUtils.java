package colorspectrum;

import Utils.RangeCheckUtils;

import java.awt.Color;

public class ColorUtils {

    public static Color interpolate(Color start, Color end, float p) {
        float minVal = 0.0f;
        float maxVal = 1.f;
        if(RangeCheckUtils.isValueInRange(p,minVal,maxVal))
        {
            throw new IllegalArgumentException("Value not between 0.0f and 1.0f");
        }
        float[] startHSB = Color.RGBtoHSB(start.getRed(), start.getGreen(), start.getBlue(), null);
        float[] endHSB = Color.RGBtoHSB(end.getRed(), end.getGreen(), end.getBlue(), null);

        float brightness = (startHSB[2] + endHSB[2]) / 2;
        float saturation = (startHSB[1] + endHSB[1]) / 2;

        float hueMax = 0;
        float hueMin = 0;
        if (startHSB[0] > endHSB[0]) {
            hueMax = startHSB[0];
            hueMin = endHSB[0];
        } else {
            hueMin = startHSB[0];
            hueMax = endHSB[0];
        }

        float hue = ((hueMax - hueMin) * p) + hueMin;

        return Color.getHSBColor(hue, saturation, brightness);
    }

    public static Color interpolate1(Color start, Color end, float p) {
        float a = 1.0f -p;
        return interpolate(start,end,a);
    }
    public static float convert4(float p )
    {

        double a = 1/Math.pow(100,4);
        double x = Math.pow(p,4);
        double b = 1;
        return (float) (-a * x) +1;
    }

    public static float convert2(float p )
    {

        double a = 1/Math.pow(100,2);
        double x = Math.pow(p,2);
        double b = 1;
        return (float) (-a * x) +1;
    }
    public static float convert6(float p )
    {

        double a = 1/Math.pow(100,6);
        double x = Math.pow(p,6);
        double b = 1;
        return (float) (-a * x) +1;
    }
    public static float convert(float p )
    {
        float a = p;
        float b = p/100.0f;
        float c = 1.0f - b;

        return c;
    }
}
