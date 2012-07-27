/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.phasespace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.component.*;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.ManifoldOrbitCalc;
import rpnumerics.Orbit;
import rpnumerics.PhasePoint;
import rpnumerics.StationaryPoint;
import wave.util.RealVector;

public class InvariantsReadyImpl extends NumConfigReadyImpl {
    //
    // Members
    //

    //
    // Constructors
    //
    public InvariantsReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom xZero) {
        super(hugoniotGeom, xZero, true);
        //super(hugoniotGeom, xZero, false);

        Iterator it = RPnDataModule.PHASESPACE.getGeomObjIterator();
        List<RpGeometry> listManifolds = new ArrayList<RpGeometry>();
        
        while (it.hasNext()) {
            RpGeometry geometry = (RpGeometry) it.next();

            if (geometry instanceof ManifoldGeom){
                listManifolds.add(geometry);
            }

        }

        for (RpGeometry rpgeometry : listManifolds) {
            RPnDataModule.PHASESPACE.remove(rpgeometry);
        }

    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
  
    @Override
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        

        
        System.out.println("ESTAMOS NO PLOT DO INVARIANTS_READY_IMPL");

        // TODO maybe we could have xzero part of hugoniot geom ?
        if (geom instanceof StationaryPointGeom  ||  geom instanceof XZeroGeom) {

            //if (isPlotManifold()) phaseSpace.join(geom);      //*** está dando problema ...


            StationaryPoint point = (StationaryPoint) geom.geomFactory().geomSource();

            System.out.println("No plot de invariantes: " +point.getElement(0) + " " + point.getElement(1) + " Sela: " + point.isSaddle());


            plotManifolds(point);

            

        }
        else
        phaseSpace.join(geom);


    }

    @Override
    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        // this will enable garbage collection
        geom.geomFactory().getUI().uninstall(geom.geomFactory());
        phaseSpace.remove(geom);
    }

      private void plotManifolds(StationaryPoint statPoint) {

        System.out.println("Classe InvariantPlotAgent ::: Entrou em joinManifolds(StationaryPoint statPoint) ... ");

        try {
            RealVector[] array = statPoint.initialManifoldPoint();

            double lambda1 = statPoint.getEigenValR()[0];
            double lambda2 = statPoint.getEigenValR()[1];

            ManifoldOrbitCalc calc0 = null;
            ManifoldOrbitCalc calc1 = null;
            ManifoldGeomFactory factory0 = null;
            ManifoldGeomFactory factory1 = null;


            if (lambda1 > 0.) {
                calc0 = new ManifoldOrbitCalc(statPoint, new PhasePoint(array[0]), Orbit.FORWARD_DIR);
                factory0 = new ManifoldGeomFactory(calc0);
                RPnDataModule.PHASESPACE.join(factory0.geom());

                calc1 = new ManifoldOrbitCalc(statPoint, new PhasePoint(array[1]), Orbit.FORWARD_DIR);
                factory1 = new ManifoldGeomFactory(calc1);
                RPnDataModule.PHASESPACE.join(factory1.geom());

            } else if (lambda1 < 0.) {
                calc0 = new ManifoldOrbitCalc(statPoint, new PhasePoint(array[0]), Orbit.BACKWARD_DIR);
                factory0 = new ManifoldGeomFactory(calc0);
                RPnDataModule.PHASESPACE.join(factory0.geom());

                calc1 = new ManifoldOrbitCalc(statPoint, new PhasePoint(array[1]), Orbit.BACKWARD_DIR);
                factory1 = new ManifoldGeomFactory(calc1);
                RPnDataModule.PHASESPACE.join(factory1.geom());

            }

            if (lambda2 > 0.) {
                calc0 = new ManifoldOrbitCalc(statPoint, new PhasePoint(array[2]), Orbit.FORWARD_DIR);
                factory0 = new ManifoldGeomFactory(calc0);
                RPnDataModule.PHASESPACE.join(factory0.geom());

                calc1 = new ManifoldOrbitCalc(statPoint, new PhasePoint(array[3]), Orbit.FORWARD_DIR);
                factory1 = new ManifoldGeomFactory(calc1);
                RPnDataModule.PHASESPACE.join(factory1.geom());

            } else if (lambda2 < 0.) {
                calc0 = new ManifoldOrbitCalc(statPoint, new PhasePoint(array[2]), Orbit.BACKWARD_DIR);
                factory0 = new ManifoldGeomFactory(calc0);
                RPnDataModule.PHASESPACE.join(factory0.geom());

                calc1 = new ManifoldOrbitCalc(statPoint, new PhasePoint(array[3]), Orbit.BACKWARD_DIR);
                factory1 = new ManifoldGeomFactory(calc1);
                RPnDataModule.PHASESPACE.join(factory1.geom());

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }






//    public void select(RPnPhaseSpaceAbstraction phaseSpace, RealVector coords) {
//        RpGeometry found = phaseSpace.find(coords);
//       	if (found != null){
//	    if (found.viewingAttr().isSelected()) {
//		found.viewingAttr().setSelected(false);
//		phaseSpace.setSelectedGeom(null);
//	    }
//	    else {
//		found.viewingAttr().setSelected(true);
//		if (phaseSpace.getSelectedGeom() != null)
//		    phaseSpace.getSelectedGeom().viewingAttr().setSelected(false);
//		phaseSpace.setSelectedGeom(found);
//	    }
//	    //        phaseSpace.update(found);
//	    phaseSpace.update();
//	}
//    }
}


