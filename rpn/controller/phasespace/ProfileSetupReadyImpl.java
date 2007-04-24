/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.*;
import rpn.RPnPhaseSpaceAbstraction;
import rpnumerics.ConnectionOrbit;
import rpn.usecase.FindProfileAgent;
import rpn.controller.*;

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
    public ProfileSetupReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom xzeroGeom, PoincareSectionGeom poincareGeom,
        ManifoldGeom fwdManifoldGeom, ManifoldGeom bwdManifoldGeom) {
            super(hugoniotGeom, xzeroGeom, poincareGeom);
            fwdManifoldGeom_ = fwdManifoldGeom;
            bwdManifoldGeom_ = bwdManifoldGeom;
            // ENABLED
            FindProfileAgent.instance().setEnabled(true);
    }

    //
    // Accessors/Mutators
    //
    public ManifoldGeom fwdManifoldGeom() { return fwdManifoldGeom_; }

    public ManifoldGeom bwdManifoldGeom() { return bwdManifoldGeom_; }

    //
    // Methods
    //
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.plot(phaseSpace, geom);
        if (geom.geomFactory().geomSource() instanceof ConnectionOrbit)
                phaseSpace.changeState(
                    new ProfileReadyImpl(hugoniotGeom(), xzeroGeom(), ((POINCARE_READY)phaseSpace.state()).poincareGeom(),
                    fwdManifoldGeom_, bwdManifoldGeom_, (ProfileGeom)geom));

    }

    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.delete(phaseSpace, geom);
        if (geom == fwdManifoldGeom())
            phaseSpace.changeState(new bwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), bwdManifoldGeom()));
        if (geom == bwdManifoldGeom())
            phaseSpace.changeState(new bwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), fwdManifoldGeom()));
    }
}
