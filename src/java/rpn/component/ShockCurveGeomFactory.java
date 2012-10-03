/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;

public class ShockCurveGeomFactory extends WaveCurveOrbitGeomFactory {
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
    public RpGeometry createGeomFromSource() {
        ShockCurve shockCurve = (ShockCurve) geomSource();

        return new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(shockCurve.getPoints()), this);
    }


     @Override
      protected ViewingAttr selectViewingAttr() {
        int family = (((ShockCurve) this.geomSource()).getFamilyIndex());


        if (family == 1) {
            return new ViewingAttr(Color.red);
        }
        if (family == 0) {
            return new ViewingAttr(Color.blue);
        }


        return null;

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
