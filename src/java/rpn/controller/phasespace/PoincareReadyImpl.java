/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.*;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.usecase.*;
import rpnumerics.ManifoldOrbit;
import rpnumerics.Orbit;
import wave.util.RealVector;

public class PoincareReadyImpl extends NumConfigReadyImpl
        implements POINCARE_READY {
    //
    // Members
    //
    private PoincareSectionGeom simplexGeom_;
    
    //
    // Constructors
    //
    public PoincareReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom xzeroGeom, PoincareSectionGeom simplexGeom) {
        super(hugoniotGeom, xzeroGeom);
        simplexGeom_ = simplexGeom;
        // ENABLED
        BackwardManifoldPlotAgent.instance().setEnabled(true);
        ForwardManifoldPlotAgent.instance().setEnabled(true);
        // DISABLED
        FindProfileAgent.instance().setEnabled(false);
    }
    
    //
    // Accessors/Mutators
    //
    public PoincareSectionGeom poincareGeom() { return simplexGeom_; }
    
    //
    // Methods
    //
    
    
    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.delete(phaseSpace, geom);
        if (geom instanceof PoincareSectionGeom)
            phaseSpace.changeState(new NumConfigReadyImpl(hugoniotGeom(), xzeroGeom()));
    }
    
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.plot(phaseSpace, geom);
        if (geom.geomFactory().geomSource() instanceof ManifoldOrbit)
            if (((ManifoldOrbit)geom.geomFactory().geomSource()).getTimeDirection() == Orbit.BACKWARD_DIR)
                phaseSpace.changeState(
                        new bwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), (ManifoldGeom)geom));
            else
                phaseSpace.changeState(
                        new fwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), (ManifoldGeom)geom));
        
    }
    
    public void select(RPnPhaseSpaceAbstraction phaseSpace, RealVector coords) {
        super.select(phaseSpace, coords);
        rpn.usecase.FindProfileAgent.instance().setEnabled(true);
    }
}