/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.*;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.usecase.*;

public class ProfileReadyImpl extends ProfileSetupReadyImpl
	implements PROFILE_READY {

    //
    // Members
    //
    private ProfileGeom connectionGeom_;

    //
    // Constructors
    //
    public ProfileReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom xzeroGeom, PoincareSectionGeom poincareGeom,
        ManifoldGeom fwdOrbitGeom, ManifoldGeom bwdOrbitGeom, ProfileGeom connectionGeom, boolean manifold) {
            super(hugoniotGeom, xzeroGeom, poincareGeom, fwdOrbitGeom, bwdOrbitGeom, manifold);
            connectionGeom_ = connectionGeom;

        // ENABLED
        ChangeDirectionAgent.instance().setEnabled(true);
        ChangeSigmaAgent.instance().setEnabled(true);
        ChangeFluxParamsAgent.instance().setEnabled(true);
        ClearPhaseSpaceAgent.instance().setEnabled(true);
        FillPhaseSpaceAgent.instance().setEnabled(true);

        // DISABLED

        /*
         * AFTER CALCULATING THE CONNECTION WE SHOULD ONLY
         * CHANGE CONFIGURATION PARAMS
         */

        BackwardOrbitPlotAgent.instance().setEnabled(false);
        ForwardOrbitPlotAgent.instance().setEnabled(false);
        PoincareSectionPlotAgent.instance().setEnabled(false);
        StationaryPointPlotAgent.instance().setEnabled(false);
        BackwardManifoldPlotAgent.instance().setEnabled(false);
        ConnectionManifoldPlotAgent.instance().setEnabled(false);
        FindProfileAgent.instance().setEnabled(false);
    }

    //
    // Accessors/Mutators
    //
    public ProfileGeom connectionGeom() { return connectionGeom_; }

    //
    // Methods
    //
    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.delete(phaseSpace, geom);
        if (geom == connectionGeom())
            phaseSpace.changeState(
                new ProfileSetupReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), fwdManifoldGeom(), bwdManifoldGeom(), isPlotManifold()));
    }
}