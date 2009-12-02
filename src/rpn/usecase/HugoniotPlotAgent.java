/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpn.component.*;
import rpnumerics.*;
import wave.util.RealVector;

public class HugoniotPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Hugoniot Curve";

    //
    // Members
    //
    static private HugoniotPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected HugoniotPlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

for (RealVector element: input){
    System.out.print("Dado de entrada: "+element+"\n");
}




        RPNUMERICS.getShockProfile().setXZero(new PhasePoint(input[0]));

        HugoniotCurveCalc hugoniotCurveCalc = RPNUMERICS.createHugoniotCalc();
        HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(hugoniotCurveCalc);
        return factory.geom();

    }

    static public HugoniotPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new HugoniotPlotAgent();
        }
        return instance_;
    }
}
