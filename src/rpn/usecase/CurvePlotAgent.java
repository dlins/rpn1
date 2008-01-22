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

public class CurvePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Curve";

    //
    // Members
    //
    static private CurvePlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected CurvePlotAgent() {
        super(DESC_TEXT, rpn.RPnConfigReader.ORBIT_FWD);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        System.out.println("Chamando curve !");
        return null;
    }

    static public CurvePlotAgent instance() {
        if (instance_ == null)
            instance_ = new CurvePlotAgent();
        return instance_;
    }
}
