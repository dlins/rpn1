/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.phasespace;

import rpn.component.*;
import rpn.RPnPhaseSpaceAbstraction;
import rpnumerics.ManifoldOrbit;
import rpnumerics.Orbit;

public class bwdProfileReadyImpl extends PoincareReadyImpl
        implements FIRSTDIR_MANIFOLD_READY {

    //
    // Members
    //
    private ManifoldGeom manifoldGeom_;

    //
    // Constructors
    //
    public bwdProfileReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom xzeroGeom, PoincareSectionGeom poincareGeom,
            ManifoldGeom manifoldGeom, boolean manifold) {
        super(hugoniotGeom, xzeroGeom, poincareGeom, manifold);
        manifoldGeom_ = manifoldGeom;
    }

    //
    // Accessors/Mutators
    //
    public ManifoldGeom manifoldGeom() {
        return manifoldGeom_;
    }

    //
    // Methods
    //
    public void plot(RPnPhaseSpaceAbstraction phaseSpace,
            RpGeometry geom) {

        super.plot(phaseSpace, geom);

        // we can keep adding fwd or bwd...
        if (geom.geomFactory().geomSource() instanceof ManifoldOrbit) {
            if (((ManifoldOrbit) geom.geomFactory().geomSource()).getTimeDirection() == Orbit.FORWARD_DIR) {
                phaseSpace.changeState(
                        new ProfileSetupReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), (ManifoldGeom) geom, manifoldGeom_, isPlotManifold()));
            } else {
                phaseSpace.changeState(
                        //                        new bwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), (ManifoldGeom)geom, isPlotManifold()));
                        new bwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), (ManifoldGeom) geom, false));
            }
        }


    }

    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {

        super.delete(phaseSpace, geom);
        if (geom == manifoldGeom()) //            phaseSpace.changeState(new PoincareReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), isPlotManifold()));
        {
            phaseSpace.changeState(new PoincareReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), false));
        }
    }
}
