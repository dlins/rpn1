/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui.physics;

import rpn.ui.diagram.DiagramLabel;
import wave.multid.view.*;
import wave.multid.*;
import rpn.controller.PhaseSpacePanelController;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import rpn.configuration.Configuration;
import rpn.controller.ui.UIController;
import rpnumerics.RPNUMERICS;
import java.util.List;

import java.util.ArrayList;
import rpn.RPnPhaseSpacePanel;

public class RPnParamsPanel extends JPanel implements Printable {
    //
    // Constants
    //

    //*** alterei aqui  (Leandro)
    static public Color DEFAULT_BOUNDARY_COLOR = Color.gray;
    static public Color DEFAULT_BACKGROUND_COLOR = Color.black;
    static public Color DEFAULT_POINTMARK_COLOR = Color.white;
    static public Color DEFAULT_LASTINPUT_CURSOR_COLOR = Color.yellow;
    static public Color DEFAULT_LASTINPUT_HIGHLIGHT_CURSOR_COLOR = Color.white;

    //** declarei isso    (Leandro)
    //*** declarei esses métodos (Leandro)
    public static void blackBackground() {
        DEFAULT_BACKGROUND_COLOR = Color.black;
        DEFAULT_POINTMARK_COLOR = Color.white;
    }

    public static void whiteBackground() {
        DEFAULT_BACKGROUND_COLOR = Color.white;
        DEFAULT_POINTMARK_COLOR = Color.black;
    }
    //***

    public static boolean isCursorLine() {
        return cursorLine_;
    }

    // Members
    //
    // the cursor orientation
    private static boolean showCursorLine_ = true;

    private Scene scene_;
    private Point cursorPos_;
    private Point trackedPoint_;

    private List<Point> points_;

    private PhaseSpacePanelController ui_;

    private DiagramLabel[] monitorArray_;

    private ParamsDescriptor paramDescriptor_;

    private static boolean cursorLine_ = true;

    private ViewingTransform viewingTransform_;

    private double[] paramValues_;

    //
    // Constructors
    //
    public RPnParamsPanel(ParamsDescriptor paramDescriptor) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        points_ = new ArrayList<Point>();

        viewingTransform_ = paramDescriptor.getViewingTransform();

        paramDescriptor_ = paramDescriptor;
        // calculates viewing window dimensions
        int myW = new Double(paramDescriptor.getViewingTransform().viewPlane().
                getViewport().getWidth()).intValue();
        int myH = new Double(paramDescriptor.getViewingTransform().viewPlane().
                getViewport().getHeight()).intValue();

        cursorPos_ = new Point(0, 0);

        setBackground(DEFAULT_BOUNDARY_COLOR);
        setPreferredSize(new java.awt.Dimension(myW, myH));

        monitorArray_ = new DiagramLabel[paramDescriptor.getParamNames().length];

        for (int i = 0; i < monitorArray_.length; i++) {
            monitorArray_[i] = new DiagramLabel(paramDescriptor.getParamNames()[i]);

            add(monitorArray_[i].getSpeed());
        }

        paramValues_ = new double[2];

        addComponentListener(new RPnParamsPanel.PanelSizeController());
        addMouseListener(new MouseMotionHandler());
        addMouseMotionListener(new MouseMotionHandler());

    }

    //
    // Accessors/Mutators
    //
    // cursor orientation
    public void setCursorPos(Point pos) {
        cursorPos_ = pos;
    }

    public Point getCursorPos() {
        return cursorPos_;
    }

    public static boolean isShowCursor() {
        return showCursorLine_;
    }

    //
    // Methods
    //
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Stroke stroke = ((Graphics2D) g).getStroke();
        BasicStroke newStroke = new BasicStroke(1.1f);
        ((Graphics2D) g).setStroke(newStroke);
        Color prev = g.getColor();

        Font font = new Font("Verdana", Font.PLAIN, 13);
        g.setFont(font);
        FontMetrics metrics = new FontMetrics(font) {
        };

        /*
         * BOUNDARY WINDOW
         */
        g.setColor(DEFAULT_BACKGROUND_COLOR);
        Shape s = paramDescriptor_.getParamsBoundaryView();
        ((Graphics2D) g).fill(s);

        g.setColor(DEFAULT_POINTMARK_COLOR);

        /*
         * Points
         */
        for (Point point : points_) {
            g.fillOval(point.x, point.y, 2, 2);
        }

    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return 0;
    }

//    @Override
//    public void update(Observable o, Object arg) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    private class MouseMotionHandler implements MouseInputListener {

        @Override
        public void mouseDragged(MouseEvent me) {

            cursorPos_ = me.getPoint();
            
            
            points_.add(new Point(me.getX(),me.getY()));

            Coords2D trackedDCCoords = new Coords2D(me.getX(), me.getY());
            CoordsArray trackedWCCoords = new CoordsArray(new Space("", 2));

            viewingTransform_.dcInverseTransform(trackedDCCoords, trackedWCCoords);

            Configuration physicsConfiguration = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());

            Configuration fluxConfiguration = physicsConfiguration.getConfiguration("fluxfunction");

            fluxConfiguration.setParamValue(paramDescriptor_.getParamNames()[0], String.valueOf(trackedWCCoords.getElement(0)));
            fluxConfiguration.setParamValue(paramDescriptor_.getParamNames()[1], String.valueOf(trackedWCCoords.getElement(1)));

            String phaseSpaceName = UIController.instance().getActivePhaseSpace().getName();

            for (int i = 0; i < monitorArray_.length; i++) {
                DiagramLabel rPnDiagramXMonitor = monitorArray_[i];
                rPnDiagramXMonitor.setX(trackedWCCoords.getElement(i));
            }

            RPNUMERICS.applyFluxParams();
            
//            fluxConfiguration.notifyObservers();

            rpn.command.ChangeFluxParamsCommand.instance().applyChange(new PropertyChangeEvent(rpn.command.ChangeFluxParamsCommand.instance(), phaseSpaceName,
                    fluxConfiguration,
                    fluxConfiguration));
            
            repaint();

        }

        @Override
        public void mouseMoved(MouseEvent me) {

            cursorPos_ = me.getPoint();

            Coords2D trackedDCCoords = new Coords2D(me.getX(), me.getY());
            CoordsArray trackedWCCoords = new CoordsArray(new Space("", 2));

            viewingTransform_.dcInverseTransform(trackedDCCoords, trackedWCCoords);

            for (int i = 0; i < monitorArray_.length; i++) {
                DiagramLabel rPnDiagramXMonitor = monitorArray_[i];
                rPnDiagramXMonitor.setX(trackedWCCoords.getElement(i));
            }

            repaint();

        }

        @Override
        public void mouseClicked(MouseEvent me) {
            cursorPos_ = me.getPoint();

            Point initialPoint = new Point(me.getX(), me.getY());

            points_.add(initialPoint);
            repaint();

        }

        @Override
        public void mousePressed(MouseEvent me) {

//            Coords2D trackedDCCoords = new Coords2D(me.getX(), me.getY());
//            CoordsArray trackedWCCoords = new CoordsArray(new Space("", 2));
//
//            viewingTransform_.viewPlaneTransform(trackedWCCoords, trackedDCCoords);dcInverseTransform(trackedDCCoords, trackedWCCoords);
        }

        @Override
        public void mouseReleased(MouseEvent e) {

            points_.clear();

            repaint();

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class PanelSizeController extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent event) {

            if (event.getComponent() instanceof RPnPhaseSpacePanel) {

//                RPnParamsPanel panel = (RPnParamsPanel) event.getComponent();
//
//                int wPanel = panel.getWidth();
//                int hPanel = panel.getHeight();
//
//                dcViewport newViewport = new dcViewport(wPanel, hPanel);
//                wcWindow currWindow = panel.scene().getViewingTransform().viewPlane().getWindow();
//
//                panel.scene().getViewingTransform().setViewPlane(new ViewPlane(newViewport, currWindow));
//                panel.scene().update();
//                panel.repaint();
////            }
//
//        }
            }

        }

    }
}
