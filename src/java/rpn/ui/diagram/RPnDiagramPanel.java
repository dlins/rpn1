/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui.diagram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import wave.multid.view.*;
import java.awt.Graphics2D;
import java.awt.print.Printable;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.GlyphVector;
import java.awt.geom.Line2D;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.ClosestDistanceCalculator;
import rpn.component.DiagramGeom;
import rpn.component.RpDiagramFactory;
import rpn.controller.ui.UIController;
import rpnumerics.Diagram;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.graphs.ViewPlane;
import wave.multid.graphs.dcViewport;
import wave.multid.graphs.wcWindow;
import wave.util.RealSegment;
import wave.util.RealVector;

public class RPnDiagramPanel extends RPnPhaseSpacePanel implements Printable {
    //
    // Constants
    //

    //
    // Members
    //
    private Point cursorPos_;
    private List<double[]> trackedPointList_;
    private Line2D.Double trackLine_;

    private Scene scene_;
    private final DiagramLabel cursorMonitor_;
    private final DiagramLabel yMonitor_;
    private final DiagramLabel relaterPoint_;
    
    
    private NumberFormat formatter_;

    private Font font_;
    
    private static Color DEFAULT_BACKGROUND_COLOR= Color.black;
    //
    // Constructors
    //
    public RPnDiagramPanel(DiagramLabel cursorMonitor,DiagramLabel yMonitor,DiagramLabel relaterPoint) {

        this(null, cursorMonitor,yMonitor,relaterPoint);

    }

    public RPnDiagramPanel(Scene scene, DiagramLabel cursorMonitor,DiagramLabel yMonitor,DiagramLabel relaterPoint) {
        setLayout(new BorderLayout());
        scene_ = scene;
        cursorPos_ = new Point(0, 0);
        trackLine_ = new Line2D.Double(cursorPos_, cursorPos_);
        trackedPointList_ = new ArrayList<double[]>();
      
        cursorMonitor_ = cursorMonitor;
        yMonitor_=yMonitor;
        relaterPoint_=relaterPoint;
        
       
        addMouseMotionListener(new MouseMotionHandler());
        addMouseListener(new MouseListenerHandler());
        addComponentListener(new PanelSizeController());

        formatter_ = NumberFormat.getInstance();
        formatter_.setMaximumFractionDigits(4);

        font_ = new Font("Arial", Font.PLAIN, 15);

    }

    @Override
    public Scene scene() {
        return scene_;
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    private class MouseMotionHandler implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent me) {

        }

        @Override
        public void mouseMoved(MouseEvent me) {

            trackedPointList_.clear();
            cursorPos_ = me.getPoint();

            ArrayList<Double> yList = new ArrayList<Double>();

            trackLine_ = new Line2D.Double(cursorPos_.x, 0, cursorPos_.x, getHeight());

            Iterator geomObjIterator = ((RPnPhaseSpaceAbstraction) scene_.getAbstractGeom()).getGeomObjIterator();

            RealVector pointOnXAxis = new RealVector(2);

            double[] yArray = null;

            DiagramGeom diagram = null;

            double speed = 0;
            CoordsArray trackedWCCoords = null;
            ViewingTransform transform = scene_.getViewingTransform();

            while (geomObjIterator.hasNext()) {
                diagram = (DiagramGeom) geomObjIterator.next();

                Coords2D cursorPoint = new Coords2D(cursorPos_.getX(), cursorPos_.getY());

                CoordsArray cursorWC = new CoordsArray(new Space("", 2));

                transform.dcInverseTransform(cursorPoint, cursorWC);

                RpDiagramFactory factory = (RpDiagramFactory) diagram.geomFactory();

                Diagram d = (Diagram) factory.geomSource();

                for (int i = 0; i < d.getLines().size(); i++) {

                    List<RealSegment> segments = d.getLine(i).getSegments();

                    pointOnXAxis.setElement(0, cursorWC.getElement(0));

                    pointOnXAxis.setElement(1, 0);

                    ClosestDistanceCalculator calculator = new ClosestDistanceCalculator(segments, pointOnXAxis);

                    Coords2D trackedDCCoords = new Coords2D(0, 0);

                    Coords2D pointDCCoords = new Coords2D(0, 0);

                    trackedWCCoords = new CoordsArray(calculator.getClosestPoint());

                    speed = trackedWCCoords.getElement(0);

                    transform.viewPlaneTransform(trackedWCCoords, trackedDCCoords);

                    yList.add(i, trackedWCCoords.getElement(1));

                    transform.dcInverseTransform(pointDCCoords, new CoordsArray(pointOnXAxis));

                    double[] point = new double[2];

                    point[0] = trackedWCCoords.getElement(0);
                    point[1] = trackedWCCoords.getElement(1);

                    trackedPointList_.add(point);
                }

                yArray = new double[d.getLines().size()];

                for (int i = 0; i < yArray.length; i++) {
                    yArray[i] = yList.get(i);

                }

                repaint();

               

            }

            CoordsArray pointToPhaseSpace = diagram.getPointToPhaseSpace(speed, yArray);
            
            cursorMonitor_.setX(speed);
            
            yMonitor_.setText(yArray);
            
            relaterPoint_.setText(pointToPhaseSpace.getCoords());
            
            Iterator<RPnPhaseSpacePanel> installedPanelsIterator = UIController.instance().getInstalledPanelsIterator();

            while (installedPanelsIterator.hasNext()) {
                RPnPhaseSpacePanel rPnPhaseSpacePanel = installedPanelsIterator.next();

                Coords2D pointDCCoords = new Coords2D(0, 0);

                rPnPhaseSpacePanel.scene().getViewingTransform().viewPlaneTransform(pointToPhaseSpace, pointDCCoords);

                rPnPhaseSpacePanel.getCastedUI().pointMarkBuffer().clear();

                rPnPhaseSpacePanel.getCastedUI().pointMarkBuffer().add(new Point((int) pointDCCoords.getX(), (int) pointDCCoords.getY()));

                rPnPhaseSpacePanel.repaint();

            }

        }

    }

    private class PanelSizeController extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent event) {

            if (event.getComponent() instanceof RPnPhaseSpacePanel) {

                RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) event.getComponent();

                int wPanel = panel.getWidth();
                int hPanel = panel.getHeight();

                dcViewport newViewport = new dcViewport(wPanel, hPanel);
                wcWindow currWindow = panel.scene().getViewingTransform().viewPlane().getWindow();

                panel.scene().getViewingTransform().setViewPlane(new ViewPlane(newViewport, currWindow));
                panel.scene().update();
                panel.repaint();
            }

        }
    }

    private class MouseListenerHandler implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent me) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mousePressed(MouseEvent me) {

        }

        @Override
        public void mouseReleased(MouseEvent me) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseEntered(MouseEvent me) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseExited(MouseEvent me) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        /*
         * BOUNDARY WINDOW
         */
        g.setColor(DEFAULT_BACKGROUND_COLOR);
        Shape s = scene().getViewingTransform().viewPlane().getWindow().dcView(scene().getViewingTransform());
        ((Graphics2D) g).fill(s);

        /*
         * SCENE
         */
        if (scene() != null) {

            scene().draw((Graphics2D) g);
        }

        /**
         * Y coords
         */
        ViewingTransform transform = scene().getViewingTransform();

        g.setColor(Color.yellow);

        for (double[] trackedPoint : trackedPointList_) {
            Coords2D cursorPoint = new Coords2D();

            CoordsArray cursorWC = new CoordsArray(trackedPoint);
            transform.viewPlaneTransform(cursorWC, cursorPoint);

            g.drawRect((int) cursorPoint.getX(), (int) cursorPoint.getY(), 2, 2);

            Graphics2D graph = (Graphics2D) g;

            StringBuilder builder = new StringBuilder();

            builder.append(formatter_.format(trackedPoint[1]));

            GlyphVector v = font_.createGlyphVector(graph.getFontRenderContext(), builder.toString());

            graph.drawGlyphVector(v, (float) cursorPoint.getX(), (float) cursorPoint.getY());

        }

        /**
         * Track line
         */
        g.setColor(Color.red);
        g.drawLine((int) trackLine_.x1, (int) trackLine_.y1, (int) trackLine_.x2, (int) trackLine_.y2);
    }

}
