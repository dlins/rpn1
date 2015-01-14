/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import rpn.controller.phasespace.*;
import wave.multid.Space;
import rpn.component.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class RPnLeftPhaseSpaceAbstraction extends RPnPhaseSpaceAbstraction {
    //
    // Constants
    //
    //
    // Members
    //

  
    //
    // Constructors
    //
    public RPnLeftPhaseSpaceAbstraction(String id, Space domain, PhaseSpaceState state) {
        super(id, domain, state);
       
    }

    @Override
    public void update() {

        Iterator geomList = getGeomObjIterator();
        List removeList =
                new ArrayList();
        List joinList = new ArrayList();
        while (geomList.hasNext()) {
            RpGeometry geom = (RpGeometry) geomList.next();
            RpGeomFactory factory = geom.geomFactory();

            if (factory.isGeomOutOfDate()) {
                removeList.add(geom);
                if (factory instanceof BifurcationCurveGeomFactory) {
                    joinList.add(((BifurcationCurveGeomFactory) factory).leftGeom());
                    //Update right side after that

                } else {
                    joinList.add(factory.geom());
                    factory.setGeomOutOfDate(false);
                    //Update finished
                }

            }
        }
        for (int i = 0; i < removeList.size(); i++) {
            super.remove((RpGeometry) removeList.get(i));
        }
        for (int i = 0; i < joinList.size(); i++) {
            super.join((RpGeometry) joinList.get(i));
        }
    }

   
}
