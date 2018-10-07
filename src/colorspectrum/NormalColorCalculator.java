package colorspectrum;
import java.awt.*;

/**
 * Created by Nils-Pc on 03.09.2018.
 */
public class NormalColorCalculator implements ColorCalculator {
    @Override
    public Color calculateColor(int prozent) {
        return  ColorUtils.interpolate1(Color.RED, Color.GREEN, ColorUtils.convert(prozent));
    }

    @Override
    public String getName() {
        return "1. Grad Gradient";
    }
}
