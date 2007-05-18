/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.component.*;
import rpnumerics.OrbitCalc;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;

import rpn.RPnConfigReader;


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
        super(DESC_TEXT,RPnConfigReader.ORBIT_BWD);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        OrbitGeomFactory factory = new OrbitGeomFactory(new OrbitCalc(oPoint, OrbitGeom.BACKWARD_DIR));
        return factory.geom();
    }

    static public BackwardOrbitPlotAgent instance() {
        if (instance_ == null)
            instance_ = new BackwardOrbitPlotAgent();
        return instance_;
    }
}
