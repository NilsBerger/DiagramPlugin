package werkzeuge;

import valueobjects.Marking;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class StatusIcons{

    public static Icon createIcon(final Color color){

        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                final Graphics2D graphics2D = (Graphics2D) g;

                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setStroke(new BasicStroke(2));

                graphics2D.translate(x, y);
                graphics2D.setColor(color);

                final Shape circle = new Ellipse2D.Float(9,9,14,14);
                graphics2D.draw(circle);
            }

            @Override
            public int getIconWidth() {
                return 32;
            }

            @Override
            public int getIconHeight() {
                return 32;
            }
        };
    }


    public static Icon getChangedIcon(){

        return createIcon(Color.RED);
    }
    public static Icon getNextIcon(){
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                final Graphics2D graphics2D = (Graphics2D) g;
                Font font = new Font( "SansSerif", Font.PLAIN, 12 );

                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setStroke(new BasicStroke(2));

                graphics2D.translate(x, y);
                graphics2D.setColor(Color.GRAY);

                final Shape circle = new Ellipse2D.Float(9,9,14,14);
                String questionmark = "?";

                graphics2D.setFont(font);
                graphics2D.draw(circle);
                graphics2D.drawString(questionmark,14,20);
            }

            @Override
            public int getIconWidth() {
                return 32;
            }

            @Override
            public int getIconHeight() {
                return 32;
            }
        };
    }
    public static Icon getPropagatesIcon(){

        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                final Graphics2D graphics2D = (Graphics2D) g;

                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setStroke(new BasicStroke(2));

                graphics2D.translate(x, y);
                graphics2D.setColor(Color.green);

                final Shape circle = new Ellipse2D.Float(9,9,14,14);
                graphics2D.draw(circle);

                final GeneralPath arrowTopRight = new GeneralPath();
                arrowTopRight.moveTo(22f,11f);
                arrowTopRight.lineTo(29f, 4f);
                arrowTopRight.lineTo(22f, 4f);
                arrowTopRight.moveTo(29f,4f);
                arrowTopRight.lineTo(29f, 11f);
                graphics2D.draw(arrowTopRight);

                final GeneralPath arrowBottomRight = new GeneralPath();
                arrowBottomRight.moveTo(22f,19);
                arrowBottomRight.lineTo(29f, 28f);
                arrowBottomRight.lineTo(22f, 28f);
                arrowBottomRight.moveTo(29f,28);
                arrowBottomRight.lineTo(29f, 19f);
                graphics2D.draw(arrowBottomRight);

                final GeneralPath arrowTopLeft = new GeneralPath();
                arrowTopLeft.moveTo(0f, 4f);
                arrowTopLeft.lineTo(8f,11f);
                arrowTopLeft.lineTo(0f, 11f);
                arrowTopLeft.moveTo(8f,11f);
                arrowTopLeft.lineTo(8f, 4f);
                graphics2D.draw(arrowTopLeft);

                final GeneralPath arrowBottomLeft = new GeneralPath();
                arrowBottomLeft.moveTo(8f,19);
                arrowBottomLeft.lineTo(0f, 28f);
                arrowBottomLeft.moveTo(8f,19);
                arrowBottomLeft.lineTo(8f, 28f);
                arrowBottomLeft.moveTo(8f,19);
                arrowBottomLeft.lineTo(0, 19f);
                graphics2D.draw(arrowBottomLeft);
            }

            @Override
            public int getIconWidth() {
                return 32;
            }

            @Override
            public int getIconHeight() {
                return 32;
            }
        };
    }
    public static Icon getBlankIcon(){
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                final Graphics2D graphics2D = (Graphics2D) g;
                Font font = new Font( "SansSerif", Font.PLAIN, 12 );

                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setStroke(new BasicStroke(2));

                graphics2D.translate(x, y);
                graphics2D.setColor(Color.WHITE);

                final Shape circle = new Ellipse2D.Float(9,9,14,14);
                String questionmark = "?";

                graphics2D.setFont(font);
                graphics2D.draw(circle);
                graphics2D.drawString(questionmark,14,20);
            }

            @Override
            public int getIconWidth() {
                return 32;
            }

            @Override
            public int getIconHeight() {
                return 32;
            }
        };
    }
    public static Icon getInspectedIcon(){

        return createIcon(Color.YELLOW);
    }

    public static Icon getIconForMarking(final Marking marking)
    {

        if(marking == Marking.CHANGED)
        {
            return getChangedIcon();
        }
        if(marking == Marking.NEXT)
        {
            return getNextIcon();
        }
        if(marking == Marking.INSPECTED)
        {
            return getInspectedIcon();
        }
        if(marking == Marking.PROPAGATES)
        {
            return getPropagatesIcon();
        }
        else
        {
            return getBlankIcon();
        }
    }

}
