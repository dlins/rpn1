/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpnumerics.RPNUMERICS;
import wave.util.RealVector;
import java.beans.PropertyChangeEvent;
import rpn.RPnCurvesListFrame;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnUIFrame;
import rpn.component.BifurcationCurveGeom;
import rpn.component.RpGeometry;
import rpn.controller.ui.*;
import rpn.parser.RPnDataModule;

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

        RpGeometry lastGeometry = RPnDataModule.PHASESPACE.getLastGeometry();

        RPnCurvesListFrame.removeLastEntry();

        UserInputTable userInputList = UIController.instance().globalInputTable();

        RealVector newValue = userInputList.values();

        lastGeometry.geomFactory().getUI().propertyChange(new PropertyChangeEvent(this, "enabled", null, newValue));

        RPnCurvesListFrame.addGeometry(lastGeometry);

        RPnDataModule.PHASESPACE.update();

        for (RPnPhaseSpaceFrame frame : RPnUIFrame.getPhaseSpaceFrames()) {
            frame.phaseSpacePanel().repaint();
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
