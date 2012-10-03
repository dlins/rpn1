/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

import java.util.ArrayList;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import rpn.component.util.AreaSelected;
import rpn.component.util.GraphicsUtil;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.view.ViewingAttr;

public class RPnSelectionPlotter extends RPn2DMouseController {

    private Point cursorPos_;
    private boolean addRectangle_ = false;

    public void mouseMoved(MouseEvent me) {

        if (addRectangle_) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

            Path2D.Double selectionPath = plotWCArea(cursorPos_, me, panel);

            List<Object> wcObjList = new ArrayList();

            wcObjList.add(selectionPath);
            ViewingAttr viewingAttr = new ViewingAttr(Color.red);
            GraphicsUtil graphicsUtil = new AreaSelected(wcObjList, panel.scene().getViewingTransform(), viewingAttr);
            panel.setLastGraphicsUtil(graphicsUtil);

            panel.repaint();

        }
    }

    public void mousePressed(MouseEvent me) {

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
        if (addRectangle_ == false) {
            cursorPos_ = new Point(me.getX(), me.getY());

            double[] mePosArray = {me.getX(), me.getY()};
            CoordsArray cursorPosWC = new CoordsArray(new Space(" ", 2));
            Coords2D mePosDC = new Coords2D(mePosArray);
            CoordsArray mePosWC = new CoordsArray(new Space(" ", 2));
            panel.scene().getViewingTransform().dcInverseTransform(mePosDC, mePosWC);
            Path2D.Double selectionPath = new Path2D.Double();
            selectionPath.moveTo(cursorPosWC.getElement(0), cursorPosWC.getElement(1));
            List<Object> wcObjectsList = new ArrayList();
            wcObjectsList.add(selectionPath);
            ViewingAttr viewingAttr = new ViewingAttr(Color.red);
            GraphicsUtil emptyGraphicsUtil = new AreaSelected(wcObjectsList, panel.scene().getViewingTransform(), viewingAttr);


            panel.addGraphicUtil(emptyGraphicsUtil);


            addRectangle_ = true;
        } else {
            addRectangle_ = false;
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
}
