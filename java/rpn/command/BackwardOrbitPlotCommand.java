/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.command;

import javax.swing.JToggleButton;
import rpn.RPnConfig;
import rpn.component.*;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;

import rpnumerics.RPNUMERICS;


public class BackwardOrbitPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Backward Orbit";

    //
    // Members
    //
    static private BackwardOrbitPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected BackwardOrbitPlotCommand() {
        super(DESC_TEXT,RPnConfig.ORBIT_BWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        OrbitGeomFactory factory = new OrbitGeomFactory(RPNUMERICS.createOrbitCalc(oPoint));
        return factory.geom();
        //return null;
    }

    static public BackwardOrbitPlotCommand instance() {
        if (instance_ == null)
            instance_ = new BackwardOrbitPlotCommand();
        return instance_;
    }
}
