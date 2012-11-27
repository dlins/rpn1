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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import rpn.component.util.GraphicsUtil;

public class RPnRiemannPanel extends RPnPhaseSpacePanel implements Printable {
    //
    // Constants
    //

    //
    // Members
    //
    private Point cursorPos_;
    private Point trackedPoint_;
    private boolean addRectangle_ = false;
    private Polygon tempRectangle;

    //
    // Constructors
    //
    public RPnRiemannPanel(Scene scene) {
        super(scene);
        addMouseMotionListener(new MouseMotionHandler());
        addMouseListener(new MouseListenerHandler());

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


            if (addRectangle_) {
                RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

                double x = cursorPos_.getX();
                double y = cursorPos_.getY();

                double newx = me.getX();
                double newy = me.getY();

                double w = Math.abs(newx - x);
                double h = Math.abs(newy - y);

                x = Math.min(x, newx);
                y = Math.min(y, newy);

//                tempRectangle = new Rectangle2D.Double(x, y, w, h);

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

//            RPnRiemannPanel source = (RPnRiemannPanel) me.getSource();
//            if (addRectangle_ == false) {
//
//                cursorPos_ = new Point(me.getX(), me.getY());
//                source.repaint();
//                addRectangle_ = true;
//            } else {
//                addRectangle_ = false;
//                source.getCastedUI().getSelectionAreas().add(tempRectangle);
//            }

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

        super.paintComponent(g);
        Stroke stroke = ((Graphics2D) g).getStroke();
        BasicStroke newStroke = new BasicStroke(1.1f);
        ((Graphics2D) g).setStroke(newStroke);
        Color prev = g.getColor();
        Graphics2D gra = (Graphics2D) g;

        /*
         * BOUNDARY WINDOW
         */

        g.setColor(DEFAULT_BACKGROUND_COLOR);
        Shape s = scene().getViewingTransform().viewPlane().getWindow().dcView(scene().getViewingTransform());
        ((Graphics2D) g).fill(s);


        /*
         * SCENE
         */

        if (scene() != null) {     //tentar fazer semelhante a isso para o desenho dos acessorios

            scene().draw((Graphics2D) g);
        }

        /*
         * POINT MARKS
         */

        g.setColor(DEFAULT_POINTMARK_COLOR);
        for (int i = 0; i < getCastedUI().pointMarkBuffer().size(); i++) {
            g.fillRect(((Point) getCastedUI().pointMarkBuffer().get(i)).x,
                    ((Point) getCastedUI().pointMarkBuffer().get(i)).y, 5, 5);
        }



        g.setColor(DEFAULT_POINTMARK_COLOR);

        /*
         * Tracked Point
         */
        if (trackedPoint_ != null) {
            g.fillRect(trackedPoint_.x, trackedPoint_.y, 5, 5);
        }


        /*
         * SELECTED AREAS
         */
        
        
        

        for (GraphicsUtil graphicsUtil : graphicsUtilList_) {

            graphicsUtil.draw(gra);

        }


        
//        /*
//         * TEMP SELECTED AREA
//         */
//        
//        if(addRectangle_ && tempRectangle!=null)
//            
//            g.drawPolygon(tempRectangle);
////        g.drawRect((int) tempRectangle.getX(), (int) tempRectangle.getY(), (int) tempRectangle.width, (int) tempRectangle.height);
//        
        
        

        /*
         * USER CURSOR ORIENTATION
         *
         * for printing and 3D projections we will not use cursor
         * orientation
         */


    }

    public void eraseSelectedArea() {


//
//
//        Graphics2D g = (Graphics2D) this.getGraphics();
//
//        g.setColor(Color.BLACK);
//
//        for (Rectangle2D.Double rectangle : getCastedUI().getSelectionAreas()) {
//            g.fill(rectangle);
//        }

    }
}
