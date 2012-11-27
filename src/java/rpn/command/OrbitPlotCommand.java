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

public class OrbitPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Orbit";
    //
    // Members
    //
    static private OrbitPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected OrbitPlotCommand() {
        super(DESC_TEXT, rpn.RPnConfig.ORBIT_FWD, new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        OrbitGeomFactory factory = new OrbitGeomFactory(RPNUMERICS.createOrbitCalc(oPoint));
        return factory.geom();
    }

    static public OrbitPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new OrbitPlotCommand();
        }
        return instance_;
    }
}
