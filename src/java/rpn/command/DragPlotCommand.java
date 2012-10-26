/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import rpnumerics.RPNUMERICS;
import wave.util.RealVector;
import java.beans.PropertyChangeEvent;
import rpn.component.RpGeometry;
import rpn.component.util.GeometryGraphND;
import rpn.controller.ui.*;

public class DragPlotCommand extends RpModelConfigChangeCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Drag Plot";
    //
    // Members
    //
    private static DragPlotCommand instance_ = null;

    //
    // Constructors
    //
    protected DragPlotCommand() {
        super(DESC_TEXT);

    }

    public void execute() {
        try {
            RpGeometry lastGeometry = UIController.instance().getActivePhaseSpace().getLastGeometry();
            UserInputTable userInputList = UIController.instance().globalInputTable();
            RealVector newValue = userInputList.values();

            if (GeometryGraphND.onCurve == 1) {
                newValue = GeometryGraphND.pMarca;
            }
            lastGeometry.geomFactory().getUI().propertyChange(new PropertyChangeEvent(this, "enabled", null, newValue));
            UIController.instance().getActivePhaseSpace().update();
            UIController.instance().panelsUpdate();
        } catch (Exception e) {

            return;

        }


    }

    public void unexecute() {
        Double oldValue = (Double) log().getNewValue();
        System.out.println("OLD XZero = " + oldValue);
        Double newValue = (Double) log().getOldValue();
        RPNUMERICS.getViscousProfileData().setSigma(newValue);
        System.out.println("NEW Xzero = " + newValue);
        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldValue, newValue));
    }

    static public DragPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new DragPlotCommand();
        }
        return instance_;
    }
}
