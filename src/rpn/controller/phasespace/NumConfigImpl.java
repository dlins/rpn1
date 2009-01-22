/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.phasespace;

import rpn.component.*;
import rpn.RPnPhaseSpaceAbstraction;
import wave.util.RealVector;


public class NumConfigImpl implements NUMCONFIG {
    //
    // Members
    //
    private HugoniotCurveGeom hugoniotGeom_;

    //
    // Constructors
    //
    public NumConfigImpl() {
    }

    public NumConfigImpl(HugoniotCurveGeom hugoniotGeom) {
        hugoniotGeom_ = hugoniotGeom;
    }

    //
    // Accessors/Mutators
    //
    public HugoniotCurveGeom hugoniotGeom() { return hugoniotGeom_; }

    //
    // Methods
    //
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        phaseSpace.join(geom);
        // TODO maybe we could have xzero part of hugoniot geom ?
        if (geom instanceof XZeroGeom)
            phaseSpace.changeState(new NumConfigReadyImpl(hugoniotGeom_, (XZeroGeom)geom));
        if (geom instanceof HugoniotCurveGeom)
            hugoniotGeom_ = (HugoniotCurveGeom)geom;
    }

    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        // this will enable garbage collection
        geom.geomFactory().getUI().uninstall(geom.geomFactory());
        phaseSpace.remove(geom);
    }

    public void select(RPnPhaseSpaceAbstraction phaseSpace, RealVector coords) {
        RpGeometry found = phaseSpace.find(coords);
       	if (found != null){
	    if (found.viewingAttr().isSelected()) {
		found.viewingAttr().setSelected(false);
		phaseSpace.setSelectedGeom(null);
	    }
	    else {
		found.viewingAttr().setSelected(true);
		if (phaseSpace.getSelectedGeom() != null)
		    phaseSpace.getSelectedGeom().viewingAttr().setSelected(false);
		phaseSpace.setSelectedGeom(found);
	    }
	    //        phaseSpace.update(found);
	    phaseSpace.update();
	}
    }
    
}
