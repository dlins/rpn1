/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;

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

        int family = (((Orbit) this.geomSource()).getFamilyIndex());

        System.out.println("Family Index: " + family);
        if (family == 1) {
            return new ViewingAttr(Color.red);
        }
        if (family == 0) {
            return new ViewingAttr(Color.blue);
        }

        return new ViewingAttr(Color.white);
    }

    protected RpGeometry createGeomFromSource() {
        Orbit orbit = (Orbit) geomSource();

        return new OrbitGeom(MultidAdapter.converseOrbitToCoordsArray(orbit), this);
    }

    public String toXML() {
        StringBuffer str = new StringBuffer();
        String timedir = "pos";
        if (((OrbitCalc) rpCalc()).getDirection() == OrbitGeom.BACKWARD_DIR) {
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
