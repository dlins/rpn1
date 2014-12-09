/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.*;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;

public class ForwardOrbitPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Forward Orbit";

    //
    // Members
    //
    static private ForwardOrbitPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected ForwardOrbitPlotCommand() {
        super(DESC_TEXT, null,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        OrbitGeomFactory factory = new OrbitGeomFactory(RPNUMERICS.createOrbitCalc(oPoint));
        return factory.geom();
        //return null;
    }

    static public ForwardOrbitPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new ForwardOrbitPlotCommand();
        }
        return instance_;
    }
}
