/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import rpn.configuration.RPnConfig;
import rpn.component.*;
import rpn.component.HugoniotCurveGeom;
import rpn.controller.phasespace.PoincareReadyImpl;
import rpn.controller.phasespace.ProfileSetupReadyImpl;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.RealVector;
import wave.util.SimplexPoincareSection;

public class ConnectionManifoldPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Connection Manifolds";
    //
    // Members
    //
    static private ConnectionManifoldPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected ConnectionManifoldPlotCommand() {
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

        System.out.println(RPNUMERICS.getViscousProfileData().getUplus());

        StationaryPointCalc statUplusCalc = new StationaryPointCalc(RPNUMERICS.getViscousProfileData().getUplus(), (RealVector) zeroGeom.geomFactory().geomSource());
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
    static public ConnectionManifoldPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new ConnectionManifoldPlotCommand();
        }
        return instance_;
    }
}
