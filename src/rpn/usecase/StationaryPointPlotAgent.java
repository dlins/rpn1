/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.component.*;
import rpnumerics.PhasePoint;
import wave.util.RealVector;
import rpn.RPnConfigReader;
import rpnumerics.RPNUMERICS;
import rpnumerics.StationaryPointCalc;

public class StationaryPointPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Stationary Point";

    //
    // Members
    //
    static private StationaryPointPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected StationaryPointPlotAgent() {
        super(DESC_TEXT, RPnConfigReader.STATPOINT);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        
        
        StationaryPointCalc sPointCalc = RPNUMERICS.createStationaryPointCalc(new PhasePoint(input[input.length-1]));
        
        return new StationaryPointGeomFactory(sPointCalc).geom();
//
//        return new StationaryPointGeomFactory(
//            new rpnumerics.StationaryPointCalc(new PhasePoint(input[input.length - 1]))).geom();
    }

    static public StationaryPointPlotAgent instance() {
        if (instance_ == null)
            instance_ = new StationaryPointPlotAgent();
        return instance_;
    }
}
