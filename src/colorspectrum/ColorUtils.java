package colorspectrum;

import Utils.RangeCheckUtils;

import java.awt.Color;

public class ColorUtils {

    public static Color interpolate(Color start, Color end, double p) {
        double minVal = 0.0d;
        double maxVal = 1.d;
        if(RangeCheckUtils.isValueInRange(p,minVal,maxVal))
        {
            //throw new IllegalArgumentException("Value: "+ p +"  not between 0.0d and 1.0d");
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

        double hueDouble = ((hueMax - hueMin) * p) + hueMin;

        float hue = (float) hueDouble;

        return Color.getHSBColor(hue, saturation, brightness);
    }

    public static Color interpolate1(Color start, Color end, double p) {
        double a = 1.0d -p;
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

    public static float convert8(float p )
    {
        double a = 1/Math.pow(50,2);
        double x = Math.pow(p,2);
        double b = 1;
        return (float) (-a * x) + 1;
    }
    public static double convert(double p )
    {
        double a = p;
        double b = p/100.0f;
        double c = 1.0d - b;

        return c;
    }
    public static double convertNORMAL(double p )
    {
        double a = p;
        double b = p/50.0f;
        double c = 1.0d - b;

        return c;
    }
    public static Color test(double p)
    {
        return interpolate1(Color.RED, Color.GREEN, convertNORMAL(p));
    }
}
