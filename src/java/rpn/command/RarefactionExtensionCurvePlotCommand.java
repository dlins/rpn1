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

public class RarefactionExtensionCurvePlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Rarefaction Extension";    //
    // Members
    //
    static private RarefactionExtensionCurvePlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected RarefactionExtensionCurvePlotCommand() {
        super(DESC_TEXT,null, new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        RarefactionExtensionGeomFactory factory = new RarefactionExtensionGeomFactory(RPNUMERICS.createRarefactionExtensionCalc(oPoint));
        return factory.geom();
    }


    static public RarefactionExtensionCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new RarefactionExtensionCurvePlotCommand();
        }
        return instance_;
    }
}
