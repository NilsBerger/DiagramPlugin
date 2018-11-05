package colorspectrum;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;

public class ColorGradient extends JPanel{
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Dimension dim = getSize();
        g2.translate(dim.width / 2, dim.height / 2);

        float size = 10;
        float x = -10;
        float y = -10;

        Color color1 = Color.red;
        Color color2 = Color.green;

        y += size;
        color1 = Color.red;
        for (int j = 0; j < 100; j = j +10) {

            Rectangle2D shape = new Rectangle2D.Float(
                    x + size * (float)j, y, size*10, size);
            float jx = (float) j;
            float na = jx / 100;
            System.out.println(na);
            Color xcolor = ColorUtils.interpolate(Color.RED, Color.GREEN, 1.0f - na);
            g2.setPaint(xcolor);
            g2.fill(shape);
        }
        y = y + 10.0f;
        for (int j = 0; j < 10; j = ++j) {
            Rectangle2D shape = new Rectangle2D.Float(
                    x + size * (float)j, y, size*5, size);
            float jx = (float) j;
            float na = jx / 10;

            Color xcolor = ColorUtils.interpolate1(Color.RED, Color.GREEN, ColorUtils.convert2(j*10));
            g2.setPaint(xcolor);
            g2.fill(shape);
        }
        y = y + 10.0f;
        for (int j = 0; j < 100;j = j +5) {

            Rectangle2D shape = new Rectangle2D.Float(
                    x + size * (float)j, y, size*5, size);
            float jx = (float) j;
            float na = jx / 10;

            Color xcolor = ColorUtils.interpolate1(Color.RED, Color.GREEN, ColorUtils.convert4(j));
            g2.setPaint(xcolor);
            g2.fill(shape);
        }
        y = y + 10.0f;
        for (int j = 0; j < 10; j = ++j) {

            Rectangle2D shape = new Rectangle2D.Float(
                    x + size * (float)j , y, size, size);

            Color xcolor = ColorUtils.interpolate1(Color.RED, Color.GREEN, ColorUtils.convert6(j*10));
            g2.setPaint(xcolor);
            g2.fill(shape);
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Color Gradient Example");
        frame.getContentPane().add(new ColorGradient());
        frame.setSize(4500, 700);
        frame.show();
    }

} 