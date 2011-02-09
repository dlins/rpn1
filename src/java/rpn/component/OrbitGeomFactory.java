/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

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
   

    protected RpGeometry createGeomFromSource() {
        Orbit orbit = (Orbit) geomSource();

        return new OrbitGeom(MultidAdapter.converseOrbitToCoordsArray(orbit), this);
    }

    public String toXML() {
        StringBuffer str = new StringBuffer();
        String timedir = "pos";
        if (((OrbitCalc) rpCalc()).tDirection() == OrbitGeom.BACKWARD_DIR) {
            timedir = "neg";
        }
        str.append("<ORBITCALC timedirection=\"" + timedir + "\"" + " initialpoint=\"" + ((OrbitCalc) rpCalc()).getStart() + "\"" + " calcready=\"" + rpn.parser.RPnDataModule.RESULTS + "\"" + ">\n");
        str.append(((Orbit) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));
        str.append("</ORBITCALC>\n");
        return str.toString();
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
