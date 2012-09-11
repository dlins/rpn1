/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.map.Map;
import wave.multid.view.Scene;
import wave.multid.view.ViewingTransform;
import wave.util.RealMatrix2;

/**
 *
 * @author edsonlan
 */
public class RPnPhasePanelBoxPlotter implements MouseMotionListener, MouseListener {

    private Point cursorPos_;
    private Rectangle2D.Double tempRectangle;
    private boolean addRectangle_ = false;

    @Override
    public void mouseMoved(MouseEvent me) {

        if (addRectangle_) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

            Path2D.Double areaSelection = new Path2D.Double();

            ViewingTransform viewingTransform = panel.scene().getViewingTransform();




            double x = cursorPos_.getX();
            double y = cursorPos_.getY();

            //P1
            areaSelection.moveTo(x, y);


            double newx = me.getX();
            double newy = me.getY();

            double w = Math.abs(newx - x);
            double h = Math.abs(newy - y);


            x = Math.min(x, newx);
            y = Math.min(y, newy);

            //P2


            Map testeMap = viewingTransform.viewingMap();
////
////
////
////
////
////
////
////
            RealMatrix2 matrixTeste = testeMap.getTransfMatrix();
////
////
//            System.out.print(matrixTeste.getElement(0, 0) + " ");
//            System.out.print(matrixTeste.getElement(0, 1) + " ");
//            System.out.println(matrixTeste.getElement(0, 2));
//
//            System.out.print(matrixTeste.getElement(1, 0) + " ");
//            System.out.print(matrixTeste.getElement(1, 1) + " ");
//            System.out.println(matrixTeste.getElement(1, 2));
//
//
//
//            System.out.print(matrixTeste.getElement(2, 0) + " ");
//            System.out.print(matrixTeste.getElement(2, 1) + " ");
//            System.out.println(matrixTeste.getElement(2, 2));



            double m00 = matrixTeste.getElement(0, 0);
            double m01 = matrixTeste.getElement(0, 1);
            double m02 = matrixTeste.getElement(0, 2);



            double m10 = matrixTeste.getElement(1, 0);
            double m11 = matrixTeste.getElement(1, 1);
            double m12 = matrixTeste.getElement(1, 2);

            double testeTransformArray[] = {m00, m01, m02, m10, m11, m12};
            
            
            
            
            
            
            

//            
//            
//   


//            System.out.println( matrixProj);


//         
//
//


            int listSize = panel.getCastedUI().getSelectionAreas().size();

            tempRectangle = new Rectangle2D.Double(x, y, w, h);




//            double testeTransformArray[] = {1, Math.sqrt(3) / 2., 0.5, 0};

            AffineTransform testeTransform = new AffineTransform(testeTransformArray);



            PathIterator iterator = tempRectangle.getPathIterator(testeTransform);
            
            Path2D.Double path = new Path2D.Double();
            
            
            path.append(iterator, true);
            
            
            
            

            Polygon poly = new Polygon();


//            while (!iterator.isDone()) {
//
//                double[] teste = new double[2];
//
//
//                int segment = iterator.currentSegment(teste);
//
//                for (int i = 0; i < teste.length; i++) {
//                    double d = teste[i];
//
//                    System.out.println(d);
//
//                }
//
//                System.out.println("Valor de seg: " + segment);
////                if (segment!=4){
//                Coords2D areaVertice = testeTransformVertices(panel.scene(), teste);
//                poly.addPoint((int) areaVertice.getX(), (int) areaVertice.getY());
//
////                }
//
//
//
//                iterator.next();
//            }
            panel.getGraphics().setColor(Color.yellow);
            panel.getGraphics().drawPolygon(path);

//
//        
//
//
//
//            System.out.println("in: "+ inDC.getCoords()[0]+" "+ inDC.getCoords()[1]+ "out: "+  finalDC.getCoords()[0] +" "+finalDC.getCoords()[1]);
//            
//            System.out.println("inWC: "+ outWC.getCoords()[0]+" "+ outWC.getCoords()[1]+ "out: "+  outWC.getCoords()[0] +" "+outWC.getCoords()[1]);








//
//
//            System.out.println("x: " + x + " y: " + y);
//
//            System.out.println("x+w: " + x + w + " y: " + y);
//            System.out.println("x+w: " + x + w + " y+h: " + y + h);
//            System.out.println("x: " + x + " y+h: " + y + h);
//
//


            System.out.println("-------------------------");



            if (listSize > 0) {
                panel.getCastedUI().getSelectionAreas().set(listSize - 1, tempRectangle);
            } else {
                panel.getCastedUI().getSelectionAreas().add(tempRectangle);
            }


            panel.repaint();

        }


    }

    private Coords2D testeTransformVertices(Scene scene, double[] oldCoords) {

        ViewingTransform viewingTransform = scene.getViewingTransform();

        Coords2D inDC = new Coords2D(oldCoords);
//
        CoordsArray outWC = new CoordsArray(new Space("", 2));
//
        viewingTransform.dcInverseTransform(inDC, outWC);



//
        Coords2D finalDC = new Coords2D();
//
        viewingTransform.viewPlaneTransform(outWC, finalDC);

        return finalDC;




    }

    public void mousePressed(MouseEvent me) {

        RPnPhaseSpacePanel source = (RPnPhaseSpacePanel) me.getSource();
        if (addRectangle_ == false) {

            cursorPos_ = new Point(me.getX(), me.getY());
            source.repaint();
            addRectangle_ = true;
        } else {
            addRectangle_ = false;
            source.getCastedUI().getSelectionAreas().add(tempRectangle);

        }

    }

    public void mouseClicked(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseDragged(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Rectangle2D.Double getSelectedRectangle() {
        return tempRectangle;
    }
}
