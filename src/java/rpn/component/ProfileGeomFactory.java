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

    public String toXML() {
        StringBuffer str = new StringBuffer();

        str.append("<CONNECTIONORBITCALC"+" calcready=\""+ rpn.parser.RPnDataModule.RESULTS+"\""+" flowname=\"" + RPNUMERICS.getViscousProfileData().getFlowName() + "\""+" methodname=\"" + ((ConnectionOrbitCalc) rpCalc()).getCalcMethodName() + "\""+">\n");
        str.append(((ConnectionOrbit)geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));
        str.append("</CONNECTIONORBITCALC>\n");
        return str.toString();
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
