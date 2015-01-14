/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.*;
import rpnumerics.OrbitPoint;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class CompositePlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Composite Curve";   
    // Members
    //
    static private CompositePlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected CompositePlotCommand() {
        super(DESC_TEXT, null,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        CompositeGeomFactory factory = new CompositeGeomFactory(RPNUMERICS.createCompositeCalc(oPoint));
        return factory.geom();

    }

    static public CompositePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new CompositePlotCommand();
        }
        return instance_;
    }
}
