/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.util.Iterator;
import java.util.Observable;
import rpn.component.RpGeometry;
import rpnumerics.RPnCurve;
import rpnumerics.TransitionalLine;

public class TransitionalCurveConfig extends Observable {

    //
    // Members
    //
    //
    // Constructors
    //
    public TransitionalCurveConfig() {

        Iterator geomObjIterator = UIController.instance().getActivePhaseSpace().getGeomObjIterator();

        while (geomObjIterator.hasNext()) {
            RpGeometry geom = (RpGeometry) geomObjIterator.next();

            RPnCurve curve = (RPnCurve) geom.geomFactory().geomSource();

            if (curve instanceof TransitionalLine) {

                TransitionalLine line = (TransitionalLine) curve;

                System.out.println("Linha: " + line.getName());

            }

        }

    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
