/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.component.*;
import rpnumerics.*;
import wave.util.RealVector;

public class BifurcationPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Bifurcation Curve";


    // Members
    //
    static private BifurcationPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //

    protected BifurcationPlotAgent() {
	super(DESC_TEXT,rpn.RPnConfig.HUGONIOT);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {


        BifurcationCurveGeomFactory factory = new BifurcationCurveGeomFactory(RPNUMERICS.createBifurcationCalc());
        return factory.geom();

    }
    static public BifurcationPlotAgent instance() {
        if (instance_ == null)
            instance_ = new BifurcationPlotAgent();
        return instance_;
    }
}
