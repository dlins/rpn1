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

public class CoincidencePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Coincidence Curve";

    //
    // Members
    //
    static private CoincidencePlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected CoincidencePlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        CoincidenceCurveGeomFactory factory = new CoincidenceCurveGeomFactory(new CoincidenceCurveCalc());
        return factory.geom();

    }

    static public CoincidencePlotAgent instance() {
        if (instance_ == null) {
            instance_ = new CoincidencePlotAgent();
        }
        return instance_;
    }

   
}
