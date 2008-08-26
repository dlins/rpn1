/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.component.*;
import rpnumerics.StationaryPointCalc;
import rpnumerics.PhasePoint;
import wave.util.RealVector;
import rpn.RPnConfigReader;
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
        super(DESC_TEXT, RPnConfigReader.STATPOINT);
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
