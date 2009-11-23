/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import rpn.usecase.BifurcationRefineAgent;
import rpn.usecase.ChangeDirectionAgent;
import rpnumerics.RPNUMERICS;
import wave.multid.Coords2D;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class AREASELECTION_CONFIG extends UI_ACTION_SELECTED {

    private int pointsSelected_;
    private ViewingTransform viewingTransf_;

    public AREASELECTION_CONFIG() {

        super(ChangeDirectionAgent.instance());
        pointsSelected_ = 0;

    }

    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {


        if (pointsSelected_ == 0) {
            RPNUMERICS.SELCTIONACTIVE = false;
            ui.getFocusPanel().getCastedUI().getSelectionAreas().clear();
            RPNUMERICS.AREA_TOP = new RealVector(userInput);
            pointsSelected_++;

        } else {
            RPNUMERICS.AREA_DOWN = new RealVector(userInput);

            viewingTransf_ = ui.getFocusPanel().scene().getViewingTransform();

            Point topLeft = toDCcoords(RPNUMERICS.AREA_TOP);
            Point downRight = toDCcoords(RPNUMERICS.AREA_DOWN);

            double width = downRight.x - topLeft.x;
            double heigh = downRight.y - topLeft.y;

            Rectangle2D.Double rectangle = new Rectangle2D.Double(topLeft.x, topLeft.y, width, heigh);

            ui.getFocusPanel().getCastedUI().getSelectionAreas().add(rectangle);
            ui.getFocusPanel().repaint();

            reset();

            RPNUMERICS.SELCTIONACTIVE = true;
            BifurcationRefineAgent.instance().getContainer().setEnabled(true);

        }

    }

    public void reset() {
        UIController.instance().getFocusPanel().getCastedUI().pointMarkBuffer().clear();
        pointsSelected_ = 0;
        BifurcationRefineAgent.instance().getContainer().setEnabled(false);

    }

    private Point toDCcoords(RealVector input) {

        Coords2D wcCoords = new Coords2D(input.getElement(0), input.getElement(1));

        Coords2D dcCoords = new Coords2D();

        viewingTransf_.viewPlaneTransform(wcCoords, dcCoords);

        Point point = new Point((int) dcCoords.getX(), (int) dcCoords.getY());

        return point;


    }
}
