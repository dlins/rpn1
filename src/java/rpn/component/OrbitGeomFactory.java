/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpn.controller.OrbitController;
import rpn.controller.RpController;
import rpnumerics.*;
import wave.multid.CoordsArray;
import wave.multid.model.MultiPoint;
import wave.multid.view.ViewingAttr;
import wave.util.RealVector;

public class OrbitGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public OrbitGeomFactory(OrbitCalc calc) {
        super(calc);
    }

    public OrbitGeomFactory(OrbitCalc calc,Orbit orbit) {
        super(calc,orbit);
    }
    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    protected ViewingAttr selectViewingAttr() {

        return new ViewingAttr(Color.white);
    }

     @Override
    protected RpController createUI() {
        return new OrbitController();
    }

    @Override
    public RpGeometry createGeomFromSource() {
        System.out.println("create geom from source");
        Orbit orbit = (Orbit) geomSource();

        OrbitPoint firstPoint = orbit.firstPoint();
        CoordsArray coords = new CoordsArray(firstPoint);
        MultiPoint startPoint= new MultiPoint(coords, selectViewingAttr());
        
        OrbitGeom orbitGeom = new OrbitGeom(MultidAdapter.converseOrbitToCoordsArray(orbit), this);
        
        orbitGeom.setStarPoint(startPoint);
        
        return orbitGeom;
    }


}
