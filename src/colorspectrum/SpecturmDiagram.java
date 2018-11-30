package colorspectrum;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Nils-Pc on 03.09.2018.
 */
public class SpecturmDiagram extends JComponent{

    public static Dimension PLATZBUTTON_GROESSE = new Dimension(10, 10);

    private StatusButton[][] _buttons;

    public SpecturmDiagram()
    {
        setLayout(new GridBagLayout());

    }

    public void setAnzahlPlatze(int anzahlSitzeProReihe, ColorCalculator[] colorCalculator)
    {
        removeAll();
        int anzahlReihen = colorCalculator.length;

                _buttons = new StatusButton[anzahlReihen][anzahlSitzeProReihe];
        for (int reihe = 0; reihe < anzahlReihen; reihe++)
        {
            JLabel label = new JLabel(colorCalculator[reihe].getName() + ":");
            imGitterEinfuegen(label, 0, reihe);
            for (int sitz = 0; sitz < anzahlSitzeProReihe; sitz++)
            {
                StatusButton button = new StatusButton(colorCalculator[reihe].calculateColor(sitz));
                button.setMinimumSize(PLATZBUTTON_GROESSE);
                button.setPreferredSize(PLATZBUTTON_GROESSE);
                imGitterEinfuegen(button, sitz + 1, reihe);
                _buttons[reihe][sitz] = button;
            }

//            for(int sitz = 0; sitz <= anzahlSitzeProReihe; ++sitz)
//            {
//                if((sitz % 10) == 0)
//                {
//                    VerticalLabel prozentLabel = new VerticalLabel(sitz + "%");
//                    prozentLabel.setRotation(VerticalLabel.ROTATE_LEFT);
//                    prozentLabel.setFont(new Font("Serif", Font.PLAIN, 10));
//                    imGitterEinfuegen(prozentLabel,sitz+1,colorCalculator.length+1);
//                }
//            }

        }
        revalidate();
        repaint();

    }
    private void imGitterEinfuegen(Component component, int gridx, int gridy)
    {
        add(component, new GridBagConstraints(gridx, gridy, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                2, 2, 2, 2), 0, 0));
    }

    public static void main(String[] args)
    {   SpecturmDiagram specturmDiagram = new SpecturmDiagram();
        ColorCalculator[] calculators = {new NormalColorCalculator(), new NormalZweiColoCalculator(),new NormalVierColorCalculator(), new NormalSechsColorCalculator()};
        specturmDiagram.setAnzahlPlatze(100,calculators);
        JFrame frame = new JFrame("Beispiel der Farbverteilung");
        frame.getContentPane().setBackground(Color.WHITE);
        frame.getContentPane().add(specturmDiagram);
        frame.setSize(4500, 700);
        frame.setVisible(true);

    }
}
