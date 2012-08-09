/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.*;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpn.usecase.FindProfileAgent;
import rpn.usecase.OrbitPlotAgent;
import rpnumerics.ManifoldOrbit;
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

        ManifoldGeom[] manifoldGeom = null;

        System.out.println("Esta no plot de ProfileSetupReadyImpl *************************************");
        
        StationaryPoint statPoint = null;
        int direction = 0;

        if (geom instanceof XZeroGeom) {

            statPoint = (StationaryPoint) geom.geomFactory().geomSource();
            direction = Orbit.FORWARD_DIR;
            manifoldGeom = buildManifold(statPoint, direction);
            fwdManifoldGeom_ = manifoldGeom[0];

        }
        if (geom instanceof StationaryPointGeom  && !(geom instanceof XZeroGeom)) {
            statPoint = (StationaryPoint) geom.geomFactory().geomSource();
            direction = Orbit.BACKWARD_DIR;
            manifoldGeom = buildManifold(statPoint, direction);
            bwdManifoldGeom_ = manifoldGeom[0];

        }


        RPnDataModule.PHASESPACE.join(manifoldGeom[0]);
        RPnDataModule.PHASESPACE.join(manifoldGeom[1]);

        UIController.instance().panelsUpdate();

    }


    private ManifoldGeom[] buildManifold (StationaryPoint statPoint, int direction) {

        SimplexPoincareSection poincareSection = (SimplexPoincareSection)(poincareGeom().geomFactory().geomSource());

        PhasePoint[] firstPoint =  null;
        if (direction == Orbit.FORWARD_DIR) firstPoint = statPoint.orbitDirectionFWD();
        if (direction == Orbit.BACKWARD_DIR) firstPoint = statPoint.orbitDirectionBWD();


        ManifoldGeomFactory factoryRef0 = new ManifoldGeomFactory(new ManifoldOrbitCalc(statPoint, firstPoint[0], poincareSection, direction));
        ManifoldGeomFactory factoryRef1 = new ManifoldGeomFactory(new ManifoldOrbitCalc(statPoint, firstPoint[1], poincareSection, direction));

        ManifoldGeom geom0 = (ManifoldGeom) factoryRef0.geom();
        ManifoldGeom geom1 = (ManifoldGeom) factoryRef1.geom();

        Orbit orbit0 = ((ManifoldOrbit)geom0.geomFactory().geomSource()).getOrbit();

        ManifoldGeom[] manifoldGeom = new ManifoldGeom[2];

        if (orbit0.isInterPoincare()) {
            manifoldGeom[0] = geom0;
            manifoldGeom[1] = geom1;
        }
        else {
            manifoldGeom[0] = geom1;
            manifoldGeom[1] = geom0;
        }


        return manifoldGeom;
    }


    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.delete(phaseSpace, geom);
        if (geom == fwdManifoldGeom())
            phaseSpace.changeState(new bwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), bwdManifoldGeom(), isPlotManifold()));
        if (geom == bwdManifoldGeom())
            phaseSpace.changeState(new bwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), fwdManifoldGeom(), isPlotManifold()));
    }
}
