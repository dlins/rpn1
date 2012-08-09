/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JToggleButton;
import rpn.RPnConfig;
import rpn.component.*;
import rpn.component.HugoniotCurveGeom;
import rpn.controller.phasespace.NumConfigReadyImpl;
import rpn.controller.phasespace.PoincareReadyImpl;
import rpn.controller.phasespace.ProfileSetupReadyImpl;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.message.RPnActionMediator;
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


    @Override
    public void actionPerformed(ActionEvent event) {

        PoincareSectionGeom poincareGeom = ((PoincareReadyImpl) RPnDataModule.PHASESPACE.state()).poincareGeom();
        HugoniotCurveGeom hGeom = ((PoincareReadyImpl) RPnDataModule.PHASESPACE.state()).hugoniotGeom();
        XZeroGeom zeroGeom = ((PoincareReadyImpl) RPnDataModule.PHASESPACE.state()).xzeroGeom();

        ProfileSetupReadyImpl profileSetupState = new ProfileSetupReadyImpl(hGeom, zeroGeom, poincareGeom);
        RPnDataModule.PHASESPACE.changeState(profileSetupState);

        profileSetupState.plot(RPnDataModule.PHASESPACE, zeroGeom);

        StationaryPointCalc statUplusCalc = new StationaryPointCalc(RPNUMERICS.getShockProfile().getUplus(), (RealVector) zeroGeom.geomFactory().geomSource());
        StationaryPointGeomFactory uPlusFactory = new StationaryPointGeomFactory(statUplusCalc);
        
        profileSetupState.plot(RPnDataModule.PHASESPACE, uPlusFactory.geom());
        

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
