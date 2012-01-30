/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;
import wave.util.RealVector;

public class ShockCurveGeomFactory extends OrbitGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //
    public ShockCurveGeomFactory(ShockCurveCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {
        ShockCurve shockCurve = (ShockCurve) geomSource();

        return new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(shockCurve.getPoints()), this);
    }

//    public String toXML() {//TODO Implement
//         StringBuffer str = new StringBuffer();
//        RealVector firstPoint = ((OrbitCalc) rpCalc()).getStart();
//
//        String direction = "forward\"";
//        str.append("<COMMAND name=\"shock");
//        System.out.println("Direcao: "+((OrbitCalc) rpCalc()).getDirection());
//
//        if (((OrbitCalc) rpCalc()).getDirection() == OrbitGeom.BACKWARD_DIR) {
//            direction = "backward\"";
//
//        }
//
////        if (((RarefactionOrbitCalc) rpCalc()).getDirection()== OrbitGeom.BOTH_DIR) {
////            direction = "both\"";
////        }
//
//        str.append(direction);
//        str.append(" inputpoint=\""+firstPoint.toString()+"\" family=\""+ ((Orbit)geomSource()).getFamilyIndex()+"\" "+">\n");
//        str.append(((Orbit) geomSource()).toXML());
//        str.append("</COMMAND>\n");
//        return str.toString();
//
//
//

//    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
