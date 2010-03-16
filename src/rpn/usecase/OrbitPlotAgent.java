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

public class OrbitPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Orbit";

    //
    // Members
    //
    static private OrbitPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected OrbitPlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.ORBIT_FWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        OrbitGeomFactory factory = new OrbitGeomFactory(RPNUMERICS.createOrbitCalc(oPoint));
        return factory.geom();
    }

    static public OrbitPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new OrbitPlotAgent();
        }
        return instance_;
    }
}
