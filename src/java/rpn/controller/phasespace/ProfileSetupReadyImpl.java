/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.component.*;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpn.usecase.FindProfileAgent;
import rpn.usecase.OrbitPlotAgent;
import rpnumerics.ManifoldOrbitCalc;
import rpnumerics.Orbit;
import rpnumerics.PhasePoint;
import rpnumerics.StationaryPoint;
import wave.util.SimplexPoincareSection;

public class ProfileSetupReadyImpl extends PoincareReadyImpl
    	implements PROFILE_SETUP_READY {

    //
    // Members
    //
    private ManifoldGeom fwdManifoldGeom_;
    private ManifoldGeom bwdManifoldGeom_;

    //
    // Constructors
    //


    public ProfileSetupReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom xzeroGeom, PoincareSectionGeom poincareGeom) {
            super(hugoniotGeom, xzeroGeom, poincareGeom, false);
            fwdManifoldGeom_ = null;
            bwdManifoldGeom_ = null;
            // ENABLED
            FindProfileAgent.instance().setEnabled(true);
            OrbitPlotAgent.instance().setEnabled(false);
    }



    public ProfileSetupReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom xzeroGeom, PoincareSectionGeom poincareGeom,
        ManifoldGeom fwdManifoldGeom, ManifoldGeom bwdManifoldGeom, boolean manifold) {
            super(hugoniotGeom, xzeroGeom, poincareGeom, manifold);
            fwdManifoldGeom_ = fwdManifoldGeom;
            bwdManifoldGeom_ = bwdManifoldGeom;
            // ENABLED
            FindProfileAgent.instance().setEnabled(true);
            OrbitPlotAgent.instance().setEnabled(false);
    }

    //
    // Accessors/Mutators
    //
    public ManifoldGeom fwdManifoldGeom() { return fwdManifoldGeom_; }

    public ManifoldGeom bwdManifoldGeom() { return bwdManifoldGeom_; }

    //
    // Methods
    //
    @Override
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {

        System.out.println("Esta no plot de ProfileSetupReadyImpl *************************************");
        
        SimplexPoincareSection poincareSection = (SimplexPoincareSection)(poincareGeom().geomFactory().geomSource());
        StationaryPoint statPoint = null;
        PhasePoint[] firstPoint =  null;
        int direction = 0;

        if (geom instanceof XZeroGeom) {
            //statPoint = (StationaryPoint) xzeroGeom().geomFactory().geomSource();
            statPoint = (StationaryPoint) geom.geomFactory().geomSource();
            firstPoint = (PhasePoint[]) statPoint.orbitDirectionFWD();
            direction = Orbit.FORWARD_DIR;

        }
        if (geom instanceof StationaryPointGeom  && !(geom instanceof XZeroGeom)) {
            statPoint = (StationaryPoint) geom.geomFactory().geomSource();
            firstPoint = (PhasePoint[]) statPoint.orbitDirectionBWD();
            direction = Orbit.BACKWARD_DIR;

        }


        ManifoldGeomFactory factoryRef0 = new ManifoldGeomFactory(new ManifoldOrbitCalc(statPoint, firstPoint[0], poincareSection, direction));
        ManifoldGeomFactory factoryRef1 = new ManifoldGeomFactory(new ManifoldOrbitCalc(statPoint, firstPoint[1], poincareSection, direction));

        RPnDataModule.PHASESPACE.join(factoryRef0.geom());
        RPnDataModule.PHASESPACE.join(factoryRef1.geom());

        UIController.instance().panelsUpdate();

    }

    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.delete(phaseSpace, geom);
        if (geom == fwdManifoldGeom())
            phaseSpace.changeState(new bwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), bwdManifoldGeom(), isPlotManifold()));
        if (geom == bwdManifoldGeom())
            phaseSpace.changeState(new bwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), fwdManifoldGeom(), isPlotManifold()));
    }
}
