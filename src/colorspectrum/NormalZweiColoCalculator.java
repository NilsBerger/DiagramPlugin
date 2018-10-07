package colorspectrum;
import java.awt.*;

/**
 * Created by Nils-Pc on 03.09.2018.
 */
public class NormalZweiColoCalculator implements ColorCalculator {
    @Override
    public Color calculateColor(int prozent) {
        return  ColorUtils.interpolate1(Color.RED, Color.GREEN, ColorUtils.convert2(prozent));
    }

    @Override
    public String getName() {
        return "2. Grad Gradient";
    }
}
