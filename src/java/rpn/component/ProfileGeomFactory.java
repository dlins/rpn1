/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpn.controller.RpController;
import rpn.controller.ProfileController;
import rpnumerics.*;

public class ProfileGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Constructors/Initializers
    //
    public ProfileGeomFactory(ConnectionOrbitCalc calc) {
        super(calc);
    }

    protected RpController createUI() {
        return new ProfileController();
    }

    //
    // Methods
    //
    public RpGeometry createGeomFromSource() {
        ConnectionOrbit orbit = (ConnectionOrbit)geomSource();
        return new ProfileGeom(MultidAdapter.converseOrbitToCoordsArray(orbit.orbit()), this);
    } 
}
