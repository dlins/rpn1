/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.*;
import rpnumerics.OrbitCalc;
import rpnumerics.OrbitPoint;
import rpnumerics.StationaryPoint;
import rpnumerics.StationaryPointCalc;
import rpnumerics.PhasePoint;
import wave.util.RealVector;
import javax.swing.ImageIcon;
import rpn.controller.PhaseSpacePanel2DController;
import rpn.RPnConfigReader;

public class XZeroPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Stationary Point";

    //
    // Members
    //
    static private XZeroPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected XZeroPlotAgent() {
        super(DESC_TEXT, RPnConfigReader.STATPOINT);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        return new XZeroGeomFactory(new StationaryPointCalc(new PhasePoint(input[input.length - 1]))).geom();
    }

    static public XZeroPlotAgent instance() {
        if (instance_ == null)
            instance_ = new XZeroPlotAgent();
        return instance_;
    }
}
