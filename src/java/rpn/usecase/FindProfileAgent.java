/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import wave.util.RealVector;
import rpnumerics.*;
import rpn.message.*;
import rpn.component.*;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import rpn.controller.phasespace.*;
import rpn.controller.ui.*;

public class FindProfileAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Find Saddle to Saddle Profile";
    //
    // Members
    //
    static private FindProfileAgent instance_ = null;

    //
    // Constructors
    //
    protected FindProfileAgent() {
        super(DESC_TEXT, null,new JToggleButton());
    }

    public void actionPerformed(ActionEvent action) {
     findProfile();
     if (UIController.instance().getNetStatusHandler().isOnline())
     RPnActionMediator.instance().setState(DESC_TEXT);
    }

    public void execute() {
        //rpn.RPnUIFrame.instance().setTitle(" completing ...  " + DESC_TEXT);
        UIController.instance().setWaitCursor();
        super.execute();
        //rpn.RPnUIFrame.instance().setTitle("");
        UIController.instance().resetCursor();
    }

    public void unexecute() {
        // TODO for history porpouses will have to store sigma value too
    };

    public RpGeometry createRpGeometry(RealVector[] input) {
//        ConnectionOrbitCalc connCalc = RPNUMERICS.createConnectionOrbitCalc((ManifoldOrbit)
//            ((PROFILE_SETUP_READY)rpn.parser.RPnDataModule.PHASESPACE.state()).fwdManifoldGeom().geomFactory().geomSource(),
//            (ManifoldOrbit)((PROFILE_SETUP_READY)rpn.parser.RPnDataModule.PHASESPACE.state()).bwdManifoldGeom().geomFactory().geomSource());
//        ProfileGeomFactory factory = new ProfileGeomFactory(connCalc);
//        return factory.geom();

        return null;
    }

    public void findProfile(){
//  //        double oldSigma = ((GenericShockFlow)RPNUMERICS.flow()).getSigma();
////        double oldSigma = ((ShockFlow)RPNUMERICS.flow()).getSigma();
//
//        double oldSigma = RPNUMERICS.getShockProfile().getSigma();
//
//        /*
//         * THIS IS THE CONTROL FOR PROFILE CREATION
//         *
//         * IF ONE OF THE MANIFOLDS IS NULL WE SHOULD TRY TO
//         * FIND IT USING THE MIDPOINT BETWEEN U- AND THE
//         * SELECTED STATIONARY POINT
//         *
//         * IF BOTH ARE NULL SO WE FIND BOTH OF THEM
//         */
//
//        if (!(rpn.parser.RPnDataModule.PHASESPACE.state() instanceof ProfileSetupReadyImpl)) {
//            // U- MANIFOLD CHECK
//            if (rpn.parser.RPnDataModule.PHASESPACE.state() instanceof fwdProfileReadyImpl) {
//                PhasePoint midPoint = rpn.parser.RPnDataModule.PHASESPACE.findSelectionMidPoint();
//                // U+ MANIFOLD CALCULATION
//                StationaryPoint bwdstatPoint = (StationaryPoint)
//                    rpn.parser.RPnDataModule.PHASESPACE.getSelectedGeom().geomFactory().geomSource();
//                ManifoldGeomFactory bwdFactory = new ManifoldGeomFactory(RPNUMERICS.createManifoldCalc(bwdstatPoint, midPoint, OrbitGeom.BACKWARD_DIR));
////                new ManifoldGeomFactory(
////                    new ManifoldOrbitCalc(bwdstatPoint, new PhasePoint(midPoint), OrbitGeom.BACKWARD_DIR));
////
//
//
////                ManifoldGeomFactory bwdFactory = new ManifoldGeomFactory(
////                    new ManifoldOrbitCalc(bwdstatPoint, new PhasePoint(midPoint), OrbitGeom.BACKWARD_DIR));
//                rpn.parser.RPnDataModule.PHASESPACE.plot(bwdFactory.geom());
//                execute();
//            } else
//                // U+ MANIFOLD CHECK
//                if (rpn.parser.RPnDataModule.PHASESPACE.state() instanceof bwdProfileReadyImpl) {
//                    PhasePoint midPoint = rpn.parser.RPnDataModule.PHASESPACE.findSelectionMidPoint();
//                    // U+ MANIFOLD CALCULATION
//                    StationaryPoint fwdstatPoint = (StationaryPoint)((NUMCONFIG_READY)
//                        rpn.parser.RPnDataModule.PHASESPACE.state()).xzeroGeom().geomFactory().geomSource();
//
//                    ManifoldGeomFactory fwdFactory = new ManifoldGeomFactory(RPNUMERICS.createManifoldCalc(fwdstatPoint, midPoint, OrbitGeom.FORWARD_DIR));
////                        new ManifoldOrbitCalc(fwdstatPoint, new PhasePoint(midPoint), OrbitGeom.FORWARD_DIR));
////                    ManifoldGeomFactory fwdFactory = new
////                        ManifoldGeomFactory(
////                        new ManifoldOrbitCalc(fwdstatPoint, new PhasePoint(midPoint), OrbitGeom.FORWARD_DIR));
//                    rpn.parser.RPnDataModule.PHASESPACE.plot(fwdFactory.geom());
//                    execute();
//            } else {
//                // NO MANIFOLD AVAILABLE
//                PhasePoint midPoint = rpn.parser.RPnDataModule.PHASESPACE.findSelectionMidPoint();
//                // U- MANIFOLD CALCULATION
//                StationaryPoint fwdstatPoint = (StationaryPoint)((NUMCONFIG_READY)
//                    rpn.parser.RPnDataModule.PHASESPACE.state()).xzeroGeom().geomFactory().geomSource();
//
//                ManifoldGeomFactory fwdFactory = new ManifoldGeomFactory(RPNUMERICS.createManifoldCalc(fwdstatPoint, midPoint, OrbitGeom.FORWARD_DIR));
////                ManifoldGeomFactory fwdFactory = new ManifoldGeomFactory(
////                    new ManifoldOrbitCalc(fwdstatPoint, new PhasePoint(midPoint), OrbitGeom.FORWARD_DIR));
//                rpn.parser.RPnDataModule.PHASESPACE.plot(fwdFactory.geom());
//                // U+ MANIFOLD CALCULATION
//                StationaryPoint bwdstatPoint = (StationaryPoint)
//                    rpn.parser.RPnDataModule.PHASESPACE.getSelectedGeom().geomFactory().geomSource();
//
//                  ManifoldGeomFactory bwdFactory = new ManifoldGeomFactory(RPNUMERICS.createManifoldCalc(bwdstatPoint, midPoint, OrbitGeom.BACKWARD_DIR));
//
////                ManifoldGeomFactory bwdFactory = new ManifoldGeomFactory(
////                    new ManifoldOrbitCalc(bwdstatPoint, new PhasePoint(midPoint), OrbitGeom.BACKWARD_DIR));
//                rpn.parser.RPnDataModule.PHASESPACE.plot(bwdFactory.geom());
//                // PROFILE CALCULATION
//                execute();
//            }
//        } else
//            // PROFILE_SETUP_READY
//                execute();
//	//        double newSigma = ((GenericShockFlow)RPNUMERICS.flow()).getSigma();
//        double newSigma =  RPNUMERICS.getShockProfile().getSigma();
//
////        double newSigma = ((ShockFlow)RPNUMERICS.flow()).getSigma();
//        ChangeSigmaAgent.instance().applyChange(
//            new java.beans.PropertyChangeEvent(this, ChangeSigmaAgent.DESC_TEXT, new Double(oldSigma), new Double(newSigma)));
//        setEnabled(false);

    }








    static public FindProfileAgent instance() {
        if (instance_ == null)
            instance_ = new FindProfileAgent();
        return instance_;
    }
}
