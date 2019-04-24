package werkzeuge.traceabilitydisplaywerkzeug;

import colorspectrum.ColorUtils;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.UIUtil;
import materials.ProgramEntity;
import materials.TraceLinkProgramEntityAssociation;
import valueobjects.Language;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class TraceabilityListCellRenderer extends DefaultListCellRenderer {

    private TraceLinkJLabel _traceLinkJLabel;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        final TraceLinkProgramEntityAssociation traceLinkDependencyMaterial = (TraceLinkProgramEntityAssociation) value;
        ProgramEntity programEntity = traceLinkDependencyMaterial.getIndependentClass();
        ProgramEntity otherProgramEntity = traceLinkDependencyMaterial.getDependentClass();
        double traceLinkValue = traceLinkDependencyMaterial.getTracelinkValue();

        JPanel panel = new JBPanel();
        panel.setLayout(new BorderLayout());
        createString(panel, programEntity, otherProgramEntity, traceLinkValue);
        if (isSelected) {
            _traceLinkJLabel.setBackground(UIUtil.getListBackground(isSelected));
        }

        return panel;
    }

    /**
     * Return the String that gets displayed in the ListCell. The String of the JavaClassNodeMaterial should always be on the left side
     *
     * @param programEntity
     * @param otherclassNode
     * @param tracelinkValue
     * @return
     */
    private void createString(JPanel panel, final ProgramEntity programEntity, final ProgramEntity otherclassNode, double tracelinkValue) {
        _traceLinkJLabel = new TraceLinkJLabel(tracelinkValue);
        String text = "";
        if (programEntity.getLanguage() == Language.Java) {
            panel.add(new JBLabel(programEntity.getSimpleName()), BorderLayout.WEST);
            panel.add(new JBLabel(otherclassNode.getSimpleName()), BorderLayout.EAST);
            panel.add(_traceLinkJLabel, BorderLayout.CENTER);
            //panel.add(new Jb)
        }
        //text = "<html><center>" + classNode.getSimpleName() + " <"   +  String.format("%.2f", tracelinkValue) + "> " +otherclassNode.getSimpleName() + "</center></html>" ;

        else {
            panel.add(new JBLabel(otherclassNode.getSimpleName()), BorderLayout.WEST);
            panel.add(new JBLabel(programEntity.getSimpleName()), BorderLayout.EAST);
            panel.add(_traceLinkJLabel, BorderLayout.CENTER);
        }
    }

    private JLabel creatLabel(double traceLink) {
        return new TraceLinkJLabel(traceLink);
    }

    class TraceLinkJLabel extends JLabel {

        public TraceLinkJLabel(double tracevalue) {
            setText(Double.toString(tracevalue));
            setHorizontalTextPosition(SwingConstants.CENTER);
            setBackground(ColorUtils.test(tracevalue));
            setOpaque(false);
        }

        @Override
        public void updateUI() {
            super.updateUI();
            setBorder(new RoundedCornerBorder());
        }


        @Override
        protected void paintComponent(Graphics g) {
            if (!isOpaque() && getBorder() instanceof RoundedCornerBorder) {

                g.setColor(getBackground());
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(getBackground());
                g2.fill(((RoundedCornerBorder) getBorder()).getBorderShape(
                        0, 0, getWidth() - 1, getHeight() - 1));
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }
}

class RoundedCornerBorder extends AbstractBorder {
    private static final Color ALPHA_ZERO = new Color(0x0, true);

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape border = getBorderShape(x, y, width - 1, height - 1);
        g2.setPaint(new Color(0, 0, 0, 0));
        Area corner = new Area(new Rectangle2D.Double(x, y, width, height));
        corner.subtract(new Area(border));
        g2.fill(corner);
        g2.setPaint(Color.GRAY);
        g2.draw(border);
        g2.dispose();
    }

    public Shape getBorderShape(int x, int y, int w, int h) {
        int r = h; //h / 2;
        return new RoundRectangle2D.Double(x, y, w, h, r, r);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(4, 8, 4, 8);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(4, 8, 4, 8);
        return insets;
    }
}
