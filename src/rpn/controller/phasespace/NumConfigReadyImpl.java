/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.RpGeometry;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.XZeroGeom;
import rpn.component.HugoniotCurveGeom;
import rpn.component.PoincareSectionGeom;
import rpn.usecase.*;


public class NumConfigReadyImpl extends NumConfigImpl
	implements NUMCONFIG_READY {

    //
    // Members
    //
    private XZeroGeom xzeroGeom_;

    //
    // Constructors
    //
    public NumConfigReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom geom) {
        super(hugoniotGeom);
        xzeroGeom_ = geom;
        // ENABLED
        BackwardOrbitPlotAgent.instance().setEnabled(true);
        ForwardOrbitPlotAgent.instance().setEnabled(true);
        PoincareSectionPlotAgent.instance().setEnabled(true);
        StationaryPointPlotAgent.instance().setEnabled(true);
        ChangeXZeroAgent.instance().setEnabled(true);
        ChangeSigmaAgent.instance().setEnabled(true);
        ChangeFluxParamsAgent.instance().setEnabled(true);
        ClearPhaseSpaceAgent.instance().setEnabled(true);
        FillPhaseSpaceAgent.instance().setEnabled(true);
        // DISABLED
        BackwardManifoldPlotAgent.instance().setEnabled(false);
        ForwardManifoldPlotAgent.instance().setEnabled(false);
        FindProfileAgent.instance().setEnabled(false);
    }

    //
    // Accessors/Mutators
    //

    //
    // Methods
    //
    

    public XZeroGeom xzeroGeom() { return xzeroGeom_; }

    @Override
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) { 
        super.plot(phaseSpace, geom);
        if (geom instanceof PoincareSectionGeom)
            phaseSpace.changeState(new PoincareReadyImpl(hugoniotGeom(), xzeroGeom(), (PoincareSectionGeom)geom));
    }
}
