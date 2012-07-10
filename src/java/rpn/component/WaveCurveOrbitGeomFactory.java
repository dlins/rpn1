/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.WaveCurveOrbit;
import rpnumerics.WaveCurveOrbitCalc;
import wave.multid.view.ViewingAttr;

public class WaveCurveOrbitGeomFactory extends OrbitGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public WaveCurveOrbitGeomFactory(WaveCurveOrbitCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    




    @Override
      protected ViewingAttr selectViewingAttr() {
        int family = (((WaveCurveOrbit) this.geomSource()).getFamilyIndex());//TODO REMOVE

        if (family == 1) {
            return new ViewingAttr(Color.red);
        }
        if (family == 0) {
            return new ViewingAttr(Color.blue);
        }


        return null;

      }


//    @Override
//    public String toXML() {
//        StringBuffer str = new StringBuffer();
//        RealVector firstPoint = new RealVector(((RarefactionOrbitCalc) rpCalc()).getStart());
//
//        String direction = "forward\"";
//        str.append("<COMMAND name=\"rarefaction");
//        System.out.println("Direcao: "+((RarefactionOrbitCalc) rpCalc()).getDirection());
//
//        if (((RarefactionOrbitCalc) rpCalc()).getDirection() == OrbitGeom.BACKWARD_DIR) {
//            direction = "backward\"";
//
//        }
//
////        if (((RarefactionOrbitCalc) rpCalc()).getDirection()== OrbitGeom.BOTH_DIR) {
////            direction = "both\"";
////        }
//
//        str.append(direction);
//        str.append(" inputpoint=\""+firstPoint.toString()+"\" family=\""+ ((RarefactionOrbit)geomSource()).getFamilyIndex()+"\" "+">\n");
//        str.append(((Orbit) geomSource()).toXML());
//        str.append("</COMMAND>\n");
//        return str.toString();
//    }

    @Override
    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
