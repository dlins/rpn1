/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;
import wave.multid.view.*;

public class ManifoldGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Constructors/Initializers
    //
    public ManifoldGeomFactory(ManifoldOrbitCalc calc) {
        super(calc);
    }

    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {
        ManifoldOrbit orbit = (ManifoldOrbit) geomSource();
        return new ManifoldGeom(MultidAdapter.converseOrbitToCoordsArray(orbit.getOrbit()), this);
    }

    public String toXML() {
        StringBuffer str = new StringBuffer();
//        String timedir = "pos";
//        if (((ManifoldOrbitCalc) rpCalc()).tDirection() == OrbitGeom.BACKWARD_DIR) {
//            timedir = "neg";
//        }
//
//        //TODO Insert plugin Name
////        str.append("<MANIFOLDCALC timedirection=\"" + timedir + "\"" + " initialpoint=\"" + ((ManifoldOrbit) geomSource()).getFirstPoint() + "\"" + " calcready=\"" + rpn.parser.RPnDataModule.RESULTS + "\"" + " flowname=\"" + ((ManifoldOrbitCalc) rpCalc()).getFlow().getName() + "\"" + " methodname=\"" + ((ManifoldOrbitCalc) rpCalc()).getCalcMethodName() + "\"" + ">\n");
//
//
//
//        str.append(((ManifoldOrbit) geomSource()).getStationaryPoint().toXML(true));
//        str.append(((ManifoldOrbit) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));
//        str.append("</MANIFOLDCALC>\n");
        return str.toString();
    }
}
