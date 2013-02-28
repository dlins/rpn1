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

public class fwdProfileReadyImpl extends PoincareReadyImpl
        implements FIRSTDIR_MANIFOLD_READY {

    //
    // Members
    //
    private ManifoldGeom manifoldGeom_;

    //
    // Constructors
    //
    public fwdProfileReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom xzeroGeom, PoincareSectionGeom poincareGeom,
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
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.plot(phaseSpace, geom);
        // we can keep adding fwd or bwd...
        if (geom.geomFactory().geomSource() instanceof ManifoldOrbit) {
            if (((ManifoldOrbit) geom.geomFactory().geomSource()).getTimeDirection() == Orbit.BACKWARD_DIR) {
                phaseSpace.changeState(
                        new ProfileSetupReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), manifoldGeom_, (ManifoldGeom) geom, isPlotManifold()));
            } else {
                phaseSpace.changeState(
                        new fwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), (ManifoldGeom) geom, false));
//                new fwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), (ManifoldGeom) geom, isPlotManifold());

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
