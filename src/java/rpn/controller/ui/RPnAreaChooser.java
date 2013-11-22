/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.event.MouseEvent;
import rpn.RPnPhaseSpacePanel;
import rpn.component.util.AreaSelected;
import rpn.component.util.GraphicsUtil;

public class RPnAreaChooser extends RPn2DMouseController {

    @Override
    public void mouseMoved(MouseEvent me) {
  
    }

    @Override
    public void mousePressed(MouseEvent me) {
        
         RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

        for (GraphicsUtil object : panel.getGraphicsUtil()) {

            if (object instanceof AreaSelected) {

                AreaSelected area = (AreaSelected) object;

                if (area.getShape().contains(me.getPoint())){
                    
                    if (!area.getViewingAttr().isSelected()){
                        area.getViewingAttr().setSelected(true);                        
                    }
                    else{
                        area.getViewingAttr().setSelected(false);
                    }
                }
            }

        }
        panel.updateGraphicsUtil();

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
