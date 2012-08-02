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
import rpn.controller.phasespace.NumConfigReadyImpl;
import rpn.controller.phasespace.PoincareReadyImpl;
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
        SimplexPoincareSection poincareSection = (SimplexPoincareSection) poincareGeom.geomFactory().geomSource();

        StationaryPoint statPointRef = (StationaryPoint) ((PoincareReadyImpl) RPnDataModule.PHASESPACE.state()).xzeroGeom().geomFactory().geomSource();
        PhasePoint[] firstPoint = (PhasePoint[]) statPointRef.orbitDirectionFWD();
        
        ManifoldGeomFactory factoryRef0 = new ManifoldGeomFactory(new ManifoldOrbitCalc(statPointRef, firstPoint[0], poincareSection, Orbit.FORWARD_DIR));
        ManifoldGeomFactory factoryRef1 = new ManifoldGeomFactory(new ManifoldOrbitCalc(statPointRef, firstPoint[1], poincareSection, Orbit.FORWARD_DIR));

        RPnDataModule.PHASESPACE.join(factoryRef0.geom());
        RPnDataModule.PHASESPACE.join(factoryRef1.geom());


        StationaryPointCalc statUplusCalc = new StationaryPointCalc(RPNUMERICS.getShockProfile().getUplus(), statPointRef);
        try {
            StationaryPoint statPointUplus = (StationaryPoint) (statUplusCalc.calc());
            PhasePoint[] firstPointUplus = (PhasePoint[]) statPointUplus.orbitDirectionBWD();
            ManifoldGeomFactory factoryUplus0 = new ManifoldGeomFactory(new ManifoldOrbitCalc(statPointUplus, firstPointUplus[0], poincareSection, Orbit.BACKWARD_DIR));
            ManifoldGeomFactory factoryUplus1 = new ManifoldGeomFactory(new ManifoldOrbitCalc(statPointUplus, firstPointUplus[1], poincareSection, Orbit.BACKWARD_DIR));
            RPnDataModule.PHASESPACE.join(factoryUplus0.geom());
            RPnDataModule.PHASESPACE.join(factoryUplus1.geom());

        } catch (RpException ex) {
            Logger.getLogger(ForwardManifoldPlotAgent.class.getName()).log(Level.SEVERE, null, ex);
        }




        UIController.instance().panelsUpdate();

        

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
