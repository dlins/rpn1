/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.command.ReferencePointSelectionCommand;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.util.LinePlotted;
import rpnumerics.OrbitPoint;
import rpnumerics.RPnCurve;
import rpnumerics.RpException;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.view.ViewingAttr;
import wave.util.RealVector;

public class ReferencePointSelectorListener implements MouseListener, MouseMotionListener {

    private RealVector referencePoint_;
    private boolean setNewReferencePoint_;
    private RpGeometry closestGeometry_;

    private LinePlotted line_;
    private boolean plotLine_;

    public ReferencePointSelectorListener() {
        setNewReferencePoint_ = false;
    }
//
    @Override
    public void mouseMoved(MouseEvent me) {

        if (referencePoint_ != null) {
            if (setNewReferencePoint_) {

                List<Object> objectList = new ArrayList<Object>();

                objectList.add(referencePoint_);

                RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

                int x = me.getX();
                int y = me.getY();

                Coords2D dcCoords = new Coords2D(x, y);

                CoordsArray coordsWC = new CoordsArray(new Space("", rpnumerics.RPNUMERICS.domainDim()));

                panel.scene().getViewingTransform().dcInverseTransform(dcCoords, coordsWC);

                RealVector wcCoordsVector = new RealVector(coordsWC.getCoords());

                objectList.add(wcCoordsVector);
                objectList.add("new reference point");

                line_ = new LinePlotted(objectList, panel.scene().getViewingTransform(), new ViewingAttr(Color.white));

                if (plotLine_) {
                    panel.setLastGraphicsUtil(line_);
                }
                panel.repaint();
            }

        }

    }

    @Override
    public void mousePressed(MouseEvent me) {

        if (me.getButton() == MouseEvent.BUTTON3) {
            plotLine_ = true;
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

            int x = me.getX();
            int y = me.getY();

            Coords2D dcCoords = new Coords2D(x, y);

            CoordsArray coordsWC = new CoordsArray(new Space("", rpnumerics.RPNUMERICS.domainDim()));

            panel.scene().getViewingTransform().dcInverseTransform(dcCoords, coordsWC);

            RealVector wcCoordsVector = new RealVector(coordsWC.getCoords());

            if (setNewReferencePoint_ == false) {
                try {
                    RPnPhaseSpaceAbstraction activePhaseSpace = UIController.instance().getActivePhaseSpace();

                    closestGeometry_ = activePhaseSpace.findClosestGeometry(wcCoordsVector);

                    RPnCurve curve = (RPnCurve) closestGeometry_.geomFactory().geomSource();

                    referencePoint_ = curve.getReferencePoint();

                    setNewReferencePoint_ = true;

                } catch (RpException ex) {
                    Logger.getLogger(ReferencePointSelectorListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {

                RpCalcBasedGeomFactory geomFactory = (RpCalcBasedGeomFactory) closestGeometry_.geomFactory();

                int[] compIndexes = panel.scene().getViewingTransform().projectionMap().getCompIndexes();

                OrbitPoint newReferencePoint = new OrbitPoint(referencePoint_);

                for (int i = 0; i < compIndexes.length; i++) {
                    int index = compIndexes[i];

                    newReferencePoint.setElement(index, wcCoordsVector.getElement(i));

                }

                UIController.instance().globalInputTable().reset();

                geomFactory.rpCalc().setReferencePoint(newReferencePoint);

                panel.removeGraphicsUtil(line_);
                
                geomFactory.updateGeom();

                ReferencePointSelectionCommand.instance().execute();

                setNewReferencePoint_ = false;

            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        plotLine_ = false;

        if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {

            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) e.getSource();

            int x = e.getX();
            int y = e.getY();

            Coords2D dcCoords = new Coords2D(x, y);

            CoordsArray coordsWC = new CoordsArray(new Space("", rpnumerics.RPNUMERICS.domainDim()));

            panel.scene().getViewingTransform().dcInverseTransform(dcCoords, coordsWC);

            RealVector wcCoordsVector = new RealVector(coordsWC.getCoords());

            RpCalcBasedGeomFactory geomFactory = (RpCalcBasedGeomFactory) closestGeometry_.geomFactory();

            int[] compIndexes = panel.scene().getViewingTransform().projectionMap().getCompIndexes();

            OrbitPoint newReferencePoint = new OrbitPoint(referencePoint_);

            for (int i = 0; i < compIndexes.length; i++) {
                int index = compIndexes[i];

                newReferencePoint.setElement(index, wcCoordsVector.getElement(i));

            }

            UIController.instance().globalInputTable().reset();

            geomFactory.rpCalc().setReferencePoint(newReferencePoint);

          
            panel.removeGraphicsUtil(line_);
            
            geomFactory.updateGeom();

            ReferencePointSelectionCommand.instance().execute();


           

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
