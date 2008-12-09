/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpn.controller.phasespace.NUMCONFIG;
import rpn.parser.RPnDataModule;
import rpnumerics.RPNUMERICS;
import rpnumerics.PhasePoint;
import rpnumerics.HugoniotCurve;
import wave.util.RealVector;
import java.beans.PropertyChangeEvent;
import rpn.controller.ui.*;

public class ChangeSigmaAgent extends RpModelConfigChangeAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Change Flow Speed";
    //
    // Members
    //
    private static ChangeSigmaAgent instance_ = null;

    //
    // Constructors
    //
    protected ChangeSigmaAgent() {
        super(DESC_TEXT);
    }

    public void execute() {

//        Double oldValue = new Double(((ShockFlow) RPNUMERICS.flow()).getSigma());

        Double oldValue = new Double(RPNUMERICS.getShockProfile().getSigma());

        RealVector[] userInputList = UIController.instance().userInputList();
        RealVector lastPointAdded = userInputList[userInputList.length - 1];
        double newSigma = 0d;
        if (rpnumerics.RPNUMERICS.domainDim() == 2) {
            // finds the best point closest from Hugoniot curve
            HugoniotCurve hCurve = (HugoniotCurve) ((NUMCONFIG) RPnDataModule.PHASESPACE.state()).hugoniotGeom().geomFactory().geomSource();
            newSigma = hCurve.findSigma(new PhasePoint(lastPointAdded));

            RPNUMERICS.getShockProfile().setSigma(newSigma);
//            ((ShockFlow)RPNUMERICS.flow()).setSigma(newSigma);
        } else {
        RPNUMERICS.getShockProfile().setSigma(newSigma);

//            ((ShockFlow) RPNUMERICS.flow()).setSigma(lastPointAdded);

        }
        System.out.println("OLD SIGMA = " + oldValue);
//        Double newValue = new Double(((ShockFlow) RPNUMERICS.flow()).getSigma());
        Double newValue = new Double(RPNUMERICS.getShockProfile().getSigma());

        System.out.println("NEW SIGMA = " + newValue);
        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldValue, newValue));
    }

    public void unexecute() {
        Double oldValue = (Double) log().getNewValue();
        System.out.println("OLD SIGMA = " + oldValue);
        Double newValue = (Double) log().getOldValue();
        RPNUMERICS.getShockProfile().setSigma(newValue);
//        ((ShockFlow) RPNUMERICS.flow()).setSigma(newValue.doubleValue());
        System.out.println("NEW SIGMA = " + newValue);
        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldValue, newValue));
    }

    static public ChangeSigmaAgent instance() {
        if (instance_ == null) {
            instance_ = new ChangeSigmaAgent();
        }
        return instance_;
    }
}
