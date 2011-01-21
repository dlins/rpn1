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

public class SubInflectionPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "SubInflection Curve";

    //
    // Members
    //
    static private SubInflectionPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected SubInflectionPlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        SubInflectionCurveGeomFactory factory = new SubInflectionCurveGeomFactory(new SubInflectionCurveCalc());
        return factory.geom();

    }

    static public SubInflectionPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new SubInflectionPlotAgent();
        }
        return instance_;
    }

   
}
