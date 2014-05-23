/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeometry;
import rpn.component.util.GraphicsUtil;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.util.RealVector;

public class AnnotationSelector extends RPn2DMouseController {

    private GraphicsUtil selectedAnnotation_;
    private RpGeometry selectedGeometry_;

    public AnnotationSelector() {

    }

    @Override
    public void mouseMoved(MouseEvent me) {

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

        int x = me.getX();
        int y = me.getY();
        
        double XMouse = (double )x;
        double YMouse = (double )y;

        Coords2D dcCoords = new Coords2D(x, y);

        CoordsArray coordsWC = new CoordsArray(new Space("", rpnumerics.RPNUMERICS.domainDim()));

        panel.scene().getViewingTransform().dcInverseTransform(dcCoords, coordsWC);

        RealVector wcCoordsVector = new RealVector(coordsWC.getCoords());
        RpGeometry findClosestGeometry = UIController.instance().getActivePhaseSpace().findClosestGeometry(wcCoordsVector);
        Iterator<GraphicsUtil> annotationIterator = findClosestGeometry.getAnnotationIterator();

        while (annotationIterator.hasNext()) {
            GraphicsUtil graphicsUtil = annotationIterator.next();
            
            if (graphicsUtil.getShape().getBounds().contains(XMouse, YMouse)){
                graphicsUtil.getViewingAttr().setColor(Color.red); 
                selectedGeometry_=findClosestGeometry;
                selectedAnnotation_=graphicsUtil;
            }
            else {
                graphicsUtil.getViewingAttr().setColor(Color.white);      
            }

        }

        UIController.instance().panelsUpdate();

    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (selectedAnnotation_!=null)
        selectedGeometry_.removeAnnotation(selectedAnnotation_);

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
    }

    @Override
    public void mouseExited(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseDragged(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
