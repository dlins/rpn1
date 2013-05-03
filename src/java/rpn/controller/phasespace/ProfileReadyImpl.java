/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.*;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.command.*;

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
        ChangeDirectionCommand.instance().setEnabled(true);
        ChangeSigmaCommand.instance().setEnabled(true);
        ChangeFluxParamsCommand.instance().setEnabled(true);
        ClearPhaseSpaceCommand.instance().setEnabled(true);
        FillPhaseSpaceCommand.instance().setEnabled(true);

        // DISABLED

        /*
         * AFTER CALCULATING THE CONNECTION WE SHOULD ONLY
         * CHANGE CONFIGURATION PARAMS
         */

        BackwardOrbitPlotCommand.instance().setEnabled(false);
        ForwardOrbitPlotCommand.instance().setEnabled(false);
        PoincareSectionPlotCommand.instance().setEnabled(false);
        StationaryPointPlotCommand.instance().setEnabled(false);
        BackwardManifoldPlotCommand.instance().setEnabled(false);
        ConnectionManifoldPlotCommand.instance().setEnabled(false);
        FindProfileCommand.instance().setEnabled(false);
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