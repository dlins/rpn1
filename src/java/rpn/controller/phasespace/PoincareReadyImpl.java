/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.phasespace;

import rpn.component.*;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.command.*;
import rpnumerics.ManifoldOrbit;
import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;
import wave.util.SimplexPoincareSection;

public class PoincareReadyImpl extends NumConfigReadyImpl
        implements POINCARE_READY {
    //
    // Members
    //

    private PoincareSectionGeom simplexGeom_;

    //
    // Constructors
    //
    public PoincareReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom xzeroGeom, PoincareSectionGeom simplexGeom, boolean manifold) {
        super(hugoniotGeom, xzeroGeom, manifold);
        simplexGeom_ = simplexGeom;
        // ENABLED
        BackwardManifoldPlotCommand.instance().setEnabled(true);
        ConnectionManifoldPlotCommand.instance().setEnabled(true);
        // DISABLED
        FindProfileCommand.instance().setEnabled(false);                          //só vai ficar habilitado em ProfileSetupReadyImpl


        RPNUMERICS.getViscousProfileData().setPoincare((SimplexPoincareSection) simplexGeom.geomFactory().geomSource());


    }

    //
    // Accessors/Mutators
    //
    public PoincareSectionGeom poincareGeom() {
        return simplexGeom_;
    }

    //
    // Methods
    //
    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.delete(phaseSpace, geom);

        if (geom instanceof PoincareSectionGeom) {
            phaseSpace.changeState(new NumConfigReadyImpl(hugoniotGeom(), xzeroGeom(), isPlotManifold()));
        }
    }

    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {

        if ((geom instanceof PoincareSectionGeom)) {
            phaseSpace.remove(simplexGeom_);

//            PoincareSectionGeom poincareGeom = (PoincareSectionGeom) geom.geomFactory();
//            RPNUMERICS.getShockProfile().setPoincare((SimplexPoincareSection) poincareGeom.geomFactory().geomSource());
        }


        super.plot(phaseSpace, geom);


    }

    public void select(RPnPhaseSpaceAbstraction phaseSpace, RealVector coords) {
        super.select(phaseSpace, coords);

        rpn.command.FindProfileCommand.instance().setEnabled(true);
    }
}
