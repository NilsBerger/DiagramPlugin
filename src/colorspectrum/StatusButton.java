package colorspectrum;
import javax.swing.*;

/**
 * Created by Nils-Pc on 03.09.2018.
 */
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;


/**
 * Ein Button, mit dem ein Sitzplatz in der Benutzeroberfläche dargestellt wird.
 * Der Sitzplatz kann ausgewählt und als frei oder als verkauft gekennzeichnet
 * werden.
 *
 * @author SE2-Team
 * @version SoSe 2016
 */
class StatusButton extends JButton
{
    private static final long serialVersionUID = 1498799618824175365L;

    private Color status ;

    public StatusButton(Color status)
    {
       this.status = status;
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        /*
         * Der Button zeichnet sich selbst, weil das Einfaerben basierend auf
         * seinem Zustand mit dem normalen JButton nicht plattformuebergreifend
         * fuer alle Look&Feels funktioniert.
         */

        // Diese Methode soll den Zustand von g nicht veraendern, deshalb wird
        // hier eine Kopie erstellt, mit der dann gearbeitet wird.
        Graphics graphics = g.create();

        try
        {
            // Hintergrund einfaerben
            Color color = status;
            graphics.setColor(color);
            Rectangle viewRect = new Rectangle();
            viewRect.width = getWidth();
            viewRect.height = getHeight();
            graphics.fillRect(viewRect.x, viewRect.y, viewRect.width,
                    viewRect.height);

            // Beschriftung des Buttons zeichnen
            graphics.setColor(Color.BLACK);
        }
        finally
        {
            graphics.dispose();
        }
    }
}
