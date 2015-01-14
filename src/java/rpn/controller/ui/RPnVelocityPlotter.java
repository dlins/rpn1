/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import rpn.command.GenericExtensionCurveCommand;
import rpn.command.RpCommand;
import rpn.component.RpGeometry;
import rpn.component.util.GraphicsUtil;
import rpn.message.RPnNetworkStatus;
import rpn.parser.RPnDataModule;
import rpnumerics.RPnCurve;
import rpnumerics.RPNUMERICS;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.util.RealVector;

public class RPnVelocityPlotter extends RPn2DMouseController {

    private RealVector cursorPos_;

    private boolean addLine_ = false;
    public static List<RealVector> listaEquil = new ArrayList();
    private static RPnVelocityPlotter instance_;
    private RpGeometry geometry_;

    public void mouseMoved(MouseEvent me) {

        if (addLine_) {

            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

            int dim = RPNUMERICS.domainDim();
            Coords2D coordsDC = new Coords2D(me.getX(), me.getY());
            CoordsArray coordsWC = new CoordsArray(new Space("", dim));
            panel.scene().getViewingTransform().dcInverseTransform(coordsDC, coordsWC);
            geometry_.removeLastAnnotation();
            geometry_.showSpeed(new CoordsArray(cursorPos_), coordsWC, panel.scene().getViewingTransform());

            panel.repaint();

        }

        UIController.instance().globalInputTable().reset();
    }

    public void mousePressed(MouseEvent me) {
        listaEquil.clear();

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

        if (addLine_ == false) {

            int dim = RPNUMERICS.domainDim();
            Coords2D coordsDC = new Coords2D(me.getX(), me.getY());
            CoordsArray coordsWC = new CoordsArray(new Space("", dim));
            panel.scene().getViewingTransform().dcInverseTransform(coordsDC, coordsWC);

            RealVector newValue = new RealVector(dim);
            for (int i = 0; i < dim; i++) {
                newValue.setElement(i, coordsWC.getElement(i));
            }
            geometry_ = RPnDataModule.PHASESPACE.findClosestGeometry(newValue);

            RPnCurve curve = (RPnCurve) (geometry_.geomFactory().geomSource());
            RealVector closestPoint = curve.findClosestPoint(newValue);

            cursorPos_ = closestPoint;
            geometry_.showSpeed(new CoordsArray(cursorPos_), new CoordsArray(cursorPos_), panel.scene().getViewingTransform());

            addLine_ = true;

        } else {
            Iterator<GraphicsUtil> annotationIterator = geometry_.getAnnotationIterator();

            GraphicsUtil lastAnnotation = null;
            while (annotationIterator.hasNext()) {
                lastAnnotation = annotationIterator.next();

            }
            if (lastAnnotation != null) {
                RPnCurve curve = (RPnCurve) geometry_.geomFactory().geomSource();
                RpCommand command = new RpCommand(lastAnnotation.toXML(), "velocity", curve.getId());
                GenericExtensionCurveCommand.instance().logCommand(command);
            }

            if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {
                RPnNetworkStatus.instance().sendCommand(rpn.controller.ui.UndoActionController.instance().getLastCommand().toXML());
            }

            addLine_ = false;
        }

        UIController.instance().globalInputTable().reset();
    }

    public void mouseDragged(MouseEvent me) {

    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public static RPnVelocityPlotter instance() {
        if (instance_ == null) {
            instance_ = new RPnVelocityPlotter();
        }
        return instance_;
    }

}
