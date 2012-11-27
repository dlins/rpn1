/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.CompositeCalc;
import rpnumerics.CompositeCurve;

public class CompositeGeomFactory extends WaveCurveOrbitGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public CompositeGeomFactory(CompositeCalc calc) {
        super(calc);
    }
    //
    // Accessors/Mutators
    //
    //
    // Methods
    //

    @Override
    public RpGeometry createGeomFromSource() {

        CompositeCurve compositeCurve = (CompositeCurve) geomSource();

        return new CompositeGeom(MultidAdapter.converseOrbitPointsToCoordsArray(compositeCurve.getPoints()), this);


    }


  

//    public String toXML() {
//        StringBuffer str = new StringBuffer();
//
//
//        RealVector firstPoint = new RealVector(((OrbitCalc) rpCalc()).getStart());
//
//        String direction = "forward\"";
//        str.append("<COMMAND name=\"composite");
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
//    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
