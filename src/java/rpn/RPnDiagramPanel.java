/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

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
import rpn.component.ClosestDistanceCalculator;
import rpn.component.DiagramGeom;
import rpn.controller.ui.UIController;
import rpnumerics.Diagram;
import rpnumerics.RPNUMERICS;
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
    private List<Point> trackedPointList_;
    private Line2D.Double trackLine_;

    private Scene scene_;
    private final RPnDiagramXMonitor cursorMonitor_;

    private NumberFormat formatter_;
    
    private   Font font_;

    //
    // Constructors
    //
    
    
     public RPnDiagramPanel(RPnDiagramXMonitor cursorMonitor) {
         
        this(null,cursorMonitor);

    }
    
    
    public RPnDiagramPanel(Scene scene, RPnDiagramXMonitor cursorMonitor) {
        setLayout(new BorderLayout());
        scene_ = scene;
        cursorPos_ = new Point(0, 0);
        trackLine_ = new Line2D.Double(cursorPos_, cursorPos_);
        trackedPointList_ = new ArrayList<Point>();
        cursorMonitor_ = cursorMonitor;

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
////            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseMoved(MouseEvent me) {

            trackedPointList_.clear();
            cursorPos_ = me.getPoint();

            trackLine_ = new Line2D.Double(cursorPos_.x, 0, cursorPos_.x, getHeight());

            Iterator geomObjIterator = ((RPnPhaseSpaceAbstraction) scene_.getAbstractGeom()).getGeomObjIterator();

//            List<Double> textCoords = new ArrayList<Double>();

            ViewingTransform transform = scene_.getViewingTransform();
            double speed = 0;
            while (geomObjIterator.hasNext()) {
                DiagramGeom diagram = (DiagramGeom) geomObjIterator.next();

                Coords2D cursorPoint = new Coords2D(cursorPos_.getX(), cursorPos_.getY());

                CoordsArray cursorWC = new CoordsArray(new Space("", 2));

                transform.dcInverseTransform(cursorPoint, cursorWC);

                Diagram d = (Diagram) diagram.geomFactory().geomSource();

                for (int i = 0; i < d.getLines().size(); i++) {

                    List<RealSegment> segments = d.getLine(i).getSegments();

                    // TODO Usar valores dcWindow e viewPort para definir o ponto a partir do qual sera achado o mais proximo.
                    RealVector pointTest = new RealVector(2);

                    pointTest.setElement(0, cursorWC.getElement(0));
                    pointTest.setElement(1, 0);

                    ClosestDistanceCalculator calculator = new ClosestDistanceCalculator(segments, pointTest);

                    Coords2D trackedDCCoords = new Coords2D(0, 0);

                    CoordsArray trackedWCCoords = new CoordsArray(calculator.getClosestPoint());

//                    textCoords.add(trackedWCCoords.getElement(1));

                    speed = trackedWCCoords.getElement(0);

                    transform.viewPlaneTransform(trackedWCCoords, trackedDCCoords);

                    trackedPointList_.add(new Point((int) trackedDCCoords.getX(), (int) trackedDCCoords.getY()));

                }

                repaint();

//                showRiemannCoords();
                cursorMonitor_.setX(speed);

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

        for (Point trackedPoint : trackedPointList_) {
            g.drawRect(trackedPoint.x, trackedPoint.y, 2, 2);

            Graphics2D graph = (Graphics2D) g;

            StringBuilder builder = new StringBuilder();

            Coords2D cursorPoint = new Coords2D(trackedPoint.x, trackedPoint.y);

            CoordsArray cursorWC = new CoordsArray(new Space("", 2));

            transform.dcInverseTransform(cursorPoint, cursorWC);

            builder.append(formatter_.format(cursorWC.getElement(1)));

            GlyphVector v = font_.createGlyphVector(graph.getFontRenderContext(), builder.toString());

            graph.drawGlyphVector(v, (float) trackedPoint.x, (float) trackedPoint.y);

        }

        
        /**
         * Track line
         */
        
        g.setColor(Color.red);
        g.drawLine((int) trackLine_.x1, (int) trackLine_.y1, (int) trackLine_.x2, (int) trackLine_.y2);
    }

    public List<Point> getTrackedPointList() {
        return trackedPointList_;
    }

    private void showRiemannCoords() {

        RealVector pointOnPhaseSpace = new RealVector(RPNUMERICS.domainDim());

        Iterator geomObjIterator = ((RPnPhaseSpaceAbstraction) scene_.getAbstractGeom()).getGeomObjIterator();

        ViewingTransform transform = scene_.getViewingTransform();

        while (geomObjIterator.hasNext()) {
            DiagramGeom diagram = (DiagramGeom) geomObjIterator.next();

            Coords2D cursorPoint = new Coords2D(cursorPos_.getX(), cursorPos_.getY());

            CoordsArray cursorWC = new CoordsArray(new Space("", 2));

            transform.dcInverseTransform(cursorPoint, cursorWC);

            Diagram d = (Diagram) diagram.geomFactory().geomSource();

            for (int i = 0; i < d.getLines().size(); i++) {

                List<RealSegment> segments = d.getLine(i).getSegments();

                // TODO Usar valores dcWindow e viewPort para definir o ponto a partir do qual sera achado o mais proximo.
                RealVector pointTest = new RealVector(2);

                pointTest.setElement(0, cursorWC.getElement(0));
                pointTest.setElement(1, 0);

                ClosestDistanceCalculator calculator = new ClosestDistanceCalculator(segments, pointTest);

                pointOnPhaseSpace.setElement(i, calculator.getClosestPoint().getElement(1));

            }

        }

        //Show point mark
        Iterator<RPnPhaseSpacePanel> installedPanelsIterator = UIController.instance().getInstalledPanelsIterator();

        while (installedPanelsIterator.hasNext()) {

            RPnPhaseSpacePanel phaseSpacePanel = installedPanelsIterator.next();

            phaseSpacePanel.getCastedUI().pointMarkBuffer().clear();
            ViewingTransform viewingTransform = phaseSpacePanel.scene().getViewingTransform();

            Coords2D dcCoords = new Coords2D(0, 0);

            CoordsArray wcCoords = new CoordsArray(pointOnPhaseSpace);

            viewingTransform.viewPlaneTransform(wcCoords, dcCoords);

            Point point = new Point((int) dcCoords.getX(), (int) dcCoords.getY());

            phaseSpacePanel.getCastedUI().pointMarkBuffer().add(point);

            phaseSpacePanel.repaint();

        }

    }

}
