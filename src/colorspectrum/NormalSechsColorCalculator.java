package colorspectrum;
import java.awt.*;

/**
 * Created by Nils-Pc on 03.09.2018.
 */
public class NormalSechsColorCalculator implements ColorCalculator {
    @Override
    public Color calculateColor(int prozent) {
        return ColorUtils.interpolate1(Color.RED, Color.GREEN, ColorUtils.convertNORMAL(prozent));
    }

    @Override
    public String getName() {
        return "6. Grad Gradient";
    }
}
