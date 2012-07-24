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
    public PoincareReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom xzeroGeom, PoincareSectionGeom simplexGeom, boolean manifold) {
        super(hugoniotGeom, xzeroGeom, manifold);
        simplexGeom_ = simplexGeom;

        System.out.println("Entrou no construtor de PoincareReadyImpl");

        // ENABLED
        BackwardManifoldPlotAgent.instance().setEnabled(true);
        ForwardManifoldPlotAgent.instance().setEnabled(true);
        // DISABLED
        FindProfileAgent.instance().setEnabled(false);                          //só vai ficar habilitado em ProfileSetupReadyImpl

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

        System.out.println("Entrou no delete de PoincareReadyImpl *******************************************");      // Como fazer pra entrar aqui?

        if (geom instanceof PoincareSectionGeom)
            phaseSpace.changeState(new NumConfigReadyImpl(hugoniotGeom(), xzeroGeom(), isPlotManifold()));
    }
    
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.plot(phaseSpace, geom);

        System.out.println("Entrou no plot de PoincareReadyImpl *******************************************");      // Como fazer pra entrar aqui? Está entrando só a partir da segunda vez...

        if (geom.geomFactory().geomSource() instanceof ManifoldOrbit)
            if (((ManifoldOrbit)geom.geomFactory().geomSource()).getTimeDirection() == Orbit.BACKWARD_DIR)
                phaseSpace.changeState(
                        new bwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), (ManifoldGeom)geom, isPlotManifold()));
            else
                phaseSpace.changeState(
                        new fwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), (ManifoldGeom)geom, isPlotManifold()));
        
    }
    
    public void select(RPnPhaseSpaceAbstraction phaseSpace, RealVector coords) {
        super.select(phaseSpace, coords);

        System.out.println("Entrou no select de PoincareReadyImpl *****************************************");      // Como fazer pra entrar aqui?

        rpn.usecase.FindProfileAgent.instance().setEnabled(true);
    }
}