/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import javax.swing.JToggleButton;
import rpn.component.*;
import rpnumerics.OrbitPoint;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class CompositePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Composite Curve";   
    // Members
    //
    static private CompositePlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected CompositePlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        CompositeGeomFactory factory = new CompositeGeomFactory(RPNUMERICS.createCompositeCalc(oPoint));
        return factory.geom();

    }

    static public CompositePlotAgent instance() {
        if (instance_ == null) {
            instance_ = new CompositePlotAgent();
        }
        return instance_;
    }
}
