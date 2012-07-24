/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import javax.swing.JToggleButton;
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
        super(DESC_TEXT, rpn.RPnConfig.ORBIT_FWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        OrbitGeomFactory factory = new OrbitGeomFactory(RPNUMERICS.createOrbitCalc(oPoint));
        return factory.geom();
        //return null;
    }

    static public ForwardOrbitPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new ForwardOrbitPlotAgent();
        }
        return instance_;
    }
}
