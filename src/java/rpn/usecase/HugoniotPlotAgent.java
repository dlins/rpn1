/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import javax.swing.JToggleButton;
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
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        //HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(new HugoniotCurveCalcND(input[0]));
        HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(RPNUMERICS.createHugoniotCalc(input[0]));
        return factory.geom();

    }

    static public HugoniotPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new HugoniotPlotAgent();
        }
        return instance_;
    }

   
}
