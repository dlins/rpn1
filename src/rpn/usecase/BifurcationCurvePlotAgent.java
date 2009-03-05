/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.component.*;
import rpnumerics.*;
import wave.util.RealVector;

public class BifurcationCurvePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Bifurcation Curve";


    // Members
    //
    static private BifurcationCurvePlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //

    protected BifurcationCurvePlotAgent() {
	super(DESC_TEXT,rpn.RPnConfig.HUGONIOT);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {


        BifurcationCurveGeomFactory factory = new BifurcationCurveGeomFactory(RPNUMERICS.createBifurcationCalc());
        return factory.geom();

    }
    static public BifurcationCurvePlotAgent instance() {
        if (instance_ == null)
            instance_ = new BifurcationCurvePlotAgent();
        return instance_;
    }
}
