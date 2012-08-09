/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpnumerics.RPNUMERICS;
import wave.util.RealVector;
import java.beans.PropertyChangeEvent;
import rpn.component.RpGeometry;
import rpn.component.util.GeometryGraphND;
import rpn.controller.ui.*;

public class DragPlotAgent extends RpModelConfigChangeAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Drag Plot";
    //
    // Members
    //
    private static DragPlotAgent instance_ = null;

    //
    // Constructors
    //
    protected DragPlotAgent() {
        super(DESC_TEXT);

    }

    public void execute() {
        try {
            RpGeometry lastGeometry = phaseSpace_.getLastGeometry();
            UserInputTable userInputList = UIController.instance().globalInputTable();
            RealVector newValue = userInputList.values();

            if (GeometryGraphND.onCurve == 1) {
                newValue = GeometryGraphND.pMarca;
            }
            lastGeometry.geomFactory().getUI().propertyChange(new PropertyChangeEvent(this, "enabled", null, newValue));
            phaseSpace_.update();
            UIController.instance().panelsUpdate();
        } catch (Exception e) {

            return;

        }


    }

    public void unexecute() {
        Double oldValue = (Double) log().getNewValue();
        System.out.println("OLD XZero = " + oldValue);
        Double newValue = (Double) log().getOldValue();
        RPNUMERICS.getShockProfile().setSigma(newValue);
        System.out.println("NEW Xzero = " + newValue);
        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldValue, newValue));
    }

    static public DragPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new DragPlotAgent();
        }
        return instance_;
    }
}
