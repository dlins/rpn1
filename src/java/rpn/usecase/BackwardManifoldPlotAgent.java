/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import javax.swing.JToggleButton;
import rpn.RPnConfig;
import rpn.component.*;
import rpnumerics.PhasePoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.StationaryPoint;
import wave.util.RealVector;

public class BackwardManifoldPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Backward Manifold";

    //
    // Members
    //
    static private BackwardManifoldPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected BackwardManifoldPlotAgent() {
        super(DESC_TEXT, RPnConfig.MANIFOLD_BWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        RealVector lastPointAdded = input[input.length - 1];
        StationaryPoint statPoint = (StationaryPoint) rpn.parser.RPnDataModule.PHASESPACE.find(lastPointAdded).geomFactory().geomSource();

        //*** comentado no codigo original
//        ManifoldGeomFactory factory = new ManifoldGeomFactory(RPNUMERICS.createManifoldCalc(statPoint, new PhasePoint(lastPointAdded), OrbitGeom.BACKWARD_DIR));
//            new ManifoldOrbitCalc(statPoint, new PhasePoint(lastPointAdded), OrbitGeom.BACKWARD_DIR));
//        return factory.geom();
        //*********

        return null;
    }

    //
    // Accessors/Mutators
    //
    static public BackwardManifoldPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new BackwardManifoldPlotAgent();
        }
        return instance_;
    }
}
