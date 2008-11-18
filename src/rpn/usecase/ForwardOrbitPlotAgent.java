/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpn.component.*;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;

public class ForwardOrbitPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Forward Orbit";

    //
    // Members
    //
    static private ForwardOrbitPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected ForwardOrbitPlotAgent() {
        super(DESC_TEXT, rpn.RPnConfigReader.ORBIT_FWD);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);

//        OrbitGeomFactory factory = new BlowUpOrbitGeomFactory(new OrbitCalc(oPoint, OrbitGeom.FORWARD_DIR));

//        OrbitGeomFactory factory = new OrbitGeomFactory(new OrbitCalc(oPoint, OrbitGeom.FORWARD_DIR));

        OrbitGeomFactory factory = new OrbitGeomFactory(RPNUMERICS.createOrbitCalc(oPoint, OrbitGeom.FORWARD_DIR));
        return factory.geom();
    }

    static public ForwardOrbitPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new ForwardOrbitPlotAgent();
        }
        return instance_;
    }
}
