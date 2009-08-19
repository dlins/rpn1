/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.RPnConfig;
import rpn.component.*;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;

import rpnumerics.RPNUMERICS;


public class BackwardOrbitPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Backward Orbit";

    //
    // Members
    //
    static private BackwardOrbitPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected BackwardOrbitPlotAgent() {
        super(DESC_TEXT,RPnConfig.ORBIT_BWD);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        OrbitGeomFactory factory = new OrbitGeomFactory(RPNUMERICS.createOrbitCalc(oPoint));
        return factory.geom();
    }

    static public BackwardOrbitPlotAgent instance() {
        if (instance_ == null)
            instance_ = new BackwardOrbitPlotAgent();
        return instance_;
    }
}
