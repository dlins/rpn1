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
import rpn.component.util.CLASSIFIERAGENT_CONFIG;
import rpn.component.util.ControlClick;
import rpn.component.util.GeometryGraphND;
import rpn.component.util.GeometryUtil;
import rpn.controller.ui.*;
import rpn.parser.RPnDataModule;
import rpnumerics.Orbit;
import rpnumerics.RPnCurve;
import rpnumerics.SegmentedCurve;
import wave.util.RealSegment;

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
//<<<<<<< HEAD

//        RpGeometry lastGeometry = RPnDataModule.PHASESPACE.getLastGeometry();
//
//        RPnCurvesListFrame.removeLastEntry();
//
//        UserInputTable userInputList = UIController.instance().globalInputTable();
//
//        RealVector newValue = userInputList.values();
//
//        if (ControlClick.onCurve == 1) newValue = GeometryGraphND.pMarca;
//
//        lastGeometry.geomFactory().getUI().propertyChange(new PropertyChangeEvent(this, "enabled", null, newValue));
//
//        RPnCurvesListFrame.addGeometry(lastGeometry);
//
//        RPnDataModule.PHASESPACE.update();
//
//        for (RPnPhaseSpaceFrame frame : RPnUIFrame.getPhaseSpaceFrames()) {
//            frame.phaseSpacePanel().repaint();
//        }
//=======
        try {
            RpGeometry lastGeometry = phaseSpace_.getLastGeometry();
            UserInputTable userInputList = UIController.instance().globalInputTable();
            RealVector newValue = userInputList.values();
            if (ControlClick.onCurve == 1) newValue = GeometryGraphND.pMarca;
            lastGeometry.geomFactory().getUI().propertyChange(new PropertyChangeEvent(this, "enabled", null, newValue));
            phaseSpace_.update();
            UIController.instance().panelsUpdate();
        } catch (Exception e) {

            return;
//>>>>>>> f35dc7fbfcff605ad0a07a4c31682e08101d761b
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
