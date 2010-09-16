/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import javax.swing.JToggleButton;
import rpn.RPnConfig;
import rpn.component.*;
import rpnumerics.StationaryPointCalc;
import rpnumerics.PhasePoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;
import rpnumerics.ShockFlow;

public class XZeroPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Stationary Point";

    //
    // Members
    //
    static private XZeroPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected XZeroPlotAgent() {
        super(DESC_TEXT, RPnConfig.STATPOINT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        return  new XZeroGeomFactory(new StationaryPointCalc(new PhasePoint(input[input.length - 1]),(ShockFlow)RPNUMERICS.createShockFlow())).geom();
    }

    static public XZeroPlotAgent instance() {
        if (instance_ == null)
            instance_ = new XZeroPlotAgent();
        return instance_;
    }
}
