/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.event.MouseEvent;
import wave.multid.view.*;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.print.Printable;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.List;
import rpn.component.ClosestDistanceCalculator;
import rpn.component.DiagramGeom;
import rpn.component.RpDiagramFactory;
import rpn.parser.RPnDataModule;
import rpnumerics.Diagram;
import rpnumerics.DiagramLine;
import wave.multid.CoordsArray;
import wave.multid.graphs.ViewPlane;
import wave.multid.graphs.dcViewport;
import wave.multid.graphs.wcWindow;
import wave.multid.model.AbstractPathIterator;
import wave.multid.model.MultiPolyLine;
import wave.util.RealVector;

public class RPnRiemannPanel extends RPnPhaseSpacePanel implements Printable {
    //
    // Constants
    //

    //
    // Members
    //
    private Point cursorPos_;
    private Point trackedPoint_;
    private Line2D.Double trackLine_;
    private boolean addRectangle_ = false;
    private Polygon tempRectangle;
    private Scene scene_;

    //
    // Constructors
    //
    public RPnRiemannPanel(Scene scene) {
//        super(scene);
        scene_ = scene;
        cursorPos_ = new Point(0, 0);
        trackLine_ = new Line2D.Double(cursorPos_, cursorPos_);

        addMouseMotionListener(new MouseMotionHandler());
        addMouseListener(new MouseListenerHandler());
        addComponentListener(new PanelSizeController());

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

            cursorPos_ = me.getPoint();

            trackLine_ = new Line2D.Double(cursorPos_.x, 0, cursorPos_.x, getHeight());
            Iterator geomObjIterator = RPnDataModule.RIEMANNPHASESPACE.getGeomObjIterator();
            
            
            while (geomObjIterator.hasNext()) {
                DiagramGeom diagram = (DiagramGeom) geomObjIterator.next();
                
                
                Diagram diagramSource =  (Diagram) diagram.geomFactory().geomSource();
                List<DiagramLine> lines = diagramSource.getLines();
                
                for (DiagramLine diagramLine : lines) {

                    RealVector point = new RealVector(2);
                    point.setElement(0, me.getX());
                    point.setElement(1, me.getY());
                    ClosestDistanceCalculator closestCalculator = new ClosestDistanceCalculator(diagramLine.getSegments(), point);
                 
                    
                    System.out.println("PointOnLine" + closestCalculator.getClosestPoint());
                    
                    CoordsArray pointOnLine = new CoordsArray(closestCalculator.getClosestPoint());
                    
                    diagram.showSpeed(pointOnLine, new CoordsArray(point), scene_.getViewingTransform());
                    
                    
                }
                
                
                
            }
            
            
            
            
            
            
            

            repaint();

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

        g.setColor(Color.red);
        g.drawLine((int) trackLine_.x1, (int) trackLine_.y1, (int) trackLine_.x2, (int) trackLine_.y2);

    }

}
