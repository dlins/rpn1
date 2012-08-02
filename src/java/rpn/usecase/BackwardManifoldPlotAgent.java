/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import javax.swing.JToggleButton;
import rpn.RPnConfig;
import rpn.component.*;
import rpn.controller.phasespace.PoincareReadyImpl;
import rpn.parser.RPnDataModule;
import rpnumerics.ManifoldOrbitCalc;
import rpnumerics.Orbit;
import rpnumerics.RpException;
import rpnumerics.StationaryPoint;
import wave.util.RealVector;
import wave.util.SimplexPoincareSection;

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
        super(DESC_TEXT, RPnConfig.MANIFOLD_BWD, new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        try {

            PoincareSectionGeom poincareGeom = ((PoincareReadyImpl) RPnDataModule.PHASESPACE.state()).poincareGeom();

            RealVector lastPointAdded = input[input.length - 1];
            StationaryPoint statPoint = (StationaryPoint) rpn.parser.RPnDataModule.PHASESPACE.find(lastPointAdded).geomFactory().geomSource();
            System.out.println("statPoint.getCoords() para backward: " + statPoint.getCoords());

            SimplexPoincareSection poincareSection = (SimplexPoincareSection) poincareGeom.geomFactory().geomSource();
            ManifoldGeomFactory factory = new ManifoldGeomFactory(new ManifoldOrbitCalc(statPoint, poincareSection, Orbit.BACKWARD_DIR));
            return factory.geom();
        } catch (RpException ex) {
            System.err.println(ex.getMessage());
        }

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
