/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.component.*;
import rpnumerics.*;
import wave.util.RealVector;
import rpn.RPnConfigReader;

public class ForwardManifoldPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Forward Manifold";

    //
    // Members
    //
    static private ForwardManifoldPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected ForwardManifoldPlotAgent() {
        super(DESC_TEXT, RPnConfigReader.MANIFOLD_FWD);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        RealVector lastPointAdded = input[input.length - 1];
        StationaryPoint statPoint = (StationaryPoint)rpn.parser.RPnDataModule.PHASESPACE.find(lastPointAdded).geomFactory().geomSource();
        ManifoldGeomFactory factory = new ManifoldGeomFactory(RPNUMERICS.createManifoldCalc(statPoint, new PhasePoint(lastPointAdded), OrbitGeom.FORWARD_DIR));
//            new ManifoldOrbitCalc(statPoint, new PhasePoint(lastPointAdded), OrbitGeom.FORWARD_DIR));
        return factory.geom();
    }

    //
    // Accessors/Mutators
    //
    static public ForwardManifoldPlotAgent instance() {
        if (instance_ == null)
            instance_ = new ForwardManifoldPlotAgent();
        return instance_;
    }
}
