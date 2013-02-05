/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import rpnumerics.RPNUMERICS;
import wave.util.RealVector;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
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

            ArrayList<RealVector> inputList = new ArrayList<RealVector>();

            inputList.add(newValue);

            if (GeometryGraphND.onCurve == 1) {
                newValue = GeometryGraphND.pMarca;
            }
            PropertyChangeEvent change = new PropertyChangeEvent(this, "enabled", null, newValue);




            lastGeometry.geomFactory().getUI().propertyChange(change);


            UIController.instance().getActivePhaseSpace().update();
            UIController.instance().panelsUpdate();



            UI_ACTION_SELECTED actionSelected = (UI_ACTION_SELECTED) UIController.instance().getState();


            PropertyChangeEvent commandEvent = new PropertyChangeEvent(actionSelected.getAction(), "enabled", null, lastGeometry);


            RpCommand command = new RpCommand(commandEvent, inputList);
            UndoActionController.instance().removeLastCommand();
            logCommand(command);



        } catch (Exception e) {

            e.printStackTrace();

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
