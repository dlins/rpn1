/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import rpn.RPnPhaseSpacePanel;

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

            double x = cursorPos_.getX();
            double y = cursorPos_.getY();

            double newx = me.getX();
            double newy = me.getY();

            double w = Math.abs(newx - x);
            double h = Math.abs(newy - y);

            x = Math.min(x, newx);
            y = Math.min(y, newy);

            int listSize = panel.getCastedUI().getSelectionAreas().size();

            tempRectangle = new Rectangle2D.Double(x, y, w, h);

            if (listSize > 0) {
                panel.getCastedUI().getSelectionAreas().set(listSize - 1, tempRectangle);
            } else {
                panel.getCastedUI().getSelectionAreas().add(tempRectangle);
            }


            panel.repaint();

        }


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
