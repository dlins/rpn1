/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JToggleButton;
import rpn.RPnConfig;
import rpn.component.*;
import rpn.controller.phasespace.POINCARE_READY;
import rpn.controller.phasespace.PoincareReadyImpl;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.RealVector;
import wave.util.SimplexPoincareSection;

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
        super(DESC_TEXT, RPnConfig.MANIFOLD_FWD, new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        try {


            PoincareSectionGeom poincareGeom = ((PoincareReadyImpl) RPnDataModule.PHASESPACE.state()).poincareGeom();

            RealVector lastPointAdded = input[input.length - 1];
            StationaryPoint statPoint = (StationaryPoint) rpn.parser.RPnDataModule.PHASESPACE.find(lastPointAdded).geomFactory().geomSource();
            System.out.println("statPoint.getCoords() para forward: " + statPoint.getCoords());

            SimplexPoincareSection poincareSection = (SimplexPoincareSection) poincareGeom.geomFactory().geomSource();
            ManifoldGeomFactory factory = new ManifoldGeomFactory(new ManifoldOrbitCalc(statPoint, poincareSection, Orbit.FORWARD_DIR));
            return factory.geom();
        } catch (RpException ex) {
            System.err.println(ex.getMessage());

        }

        return null;
    }

    //
    // Accessors/Mutators
    //
    static public ForwardManifoldPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new ForwardManifoldPlotAgent();
        }
        return instance_;
    }
}
