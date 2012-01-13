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

public class RarefactionExtensionCurvePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Rarefaction Extension";    //
    // Members
    //
    static private RarefactionExtensionCurvePlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected RarefactionExtensionCurvePlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        RarefactionExtensionGeomFactory factory = new RarefactionExtensionGeomFactory(RPNUMERICS.createRarefactionExtensionCalc(oPoint));
        return factory.geom();
    }

    static public RarefactionExtensionCurvePlotAgent instance() {
        if (instance_ == null) {
            instance_ = new RarefactionExtensionCurvePlotAgent();
        }
        return instance_;
    }
}
