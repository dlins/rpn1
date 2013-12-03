/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.RPnPhaseSpacePanel;
import wave.multid.DimMismatchEx;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.GeomObjView;
import wave.multid.view.PolyLine;

public class RPnAreaChooser extends RPn2DMouseController {

    public RPnAreaChooser() {
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();


        for (MultiGeometryImpl object : panel.getConvexSelection()) {

            GeomObjView view = null;
            try {
                view = object.createView(panel.scene().getViewingTransform());
            } catch (DimMismatchEx ex) {
                Logger.getLogger(RPnAreaChooser.class.getName()).log(Level.SEVERE, null, ex);
            }

            PolyLine area = (PolyLine) view;


            if (area.getShape().contains(me.getPoint())) {
               

                if (!area.getViewingAttr().isSelected()) {
                    area.getViewingAttr().setSelected(true);
                    panel.setPhysicalBoundarySelected(false);

                } else {
                    area.getViewingAttr().setSelected(false);
                    panel.setPhysicalBoundarySelected(true);


                }

            }

            

                    
        }

        panel.updateGraphicsUtil();
        panel.scene().update();

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
}
