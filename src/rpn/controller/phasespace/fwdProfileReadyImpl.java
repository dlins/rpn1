/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.*;
import rpn.RPnPhaseSpaceAbstraction;
import rpnumerics.ManifoldOrbit;
import wave.util.RealVector;


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
        ManifoldGeom manifoldGeom) {
            super(hugoniotGeom, xzeroGeom, poincareGeom);
            manifoldGeom_ = manifoldGeom;
    } 

    //
    // Accessors/Mutators
    //
    public ManifoldGeom manifoldGeom() { return manifoldGeom_; }

    //
    // Methods
    //
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.plot(phaseSpace, geom);
        // we can keep adding fwd or bwd...
        if (geom.geomFactory().geomSource() instanceof ManifoldOrbit)
                if (((ManifoldOrbit)geom.geomFactory().geomSource()).getTimeDirection() == OrbitGeom.BACKWARD_DIR)
                    phaseSpace.changeState(
                        new ProfileSetupReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), manifoldGeom_, (ManifoldGeom)geom));
                else
                    phaseSpace.changeState(
                        new fwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), (ManifoldGeom)geom));

    }

    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.delete(phaseSpace, geom);
        if (geom == manifoldGeom())
            phaseSpace.changeState(new PoincareReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom()));
    }
}