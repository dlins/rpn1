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

    public RpGeometry createGeomFromSource() {
        Orbit orbit = (Orbit) geomSource();
        
        return new OrbitGeom(MultidAdapter.converseOrbitToCoordsArray(orbit), this);
    }


}
