/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.RarefactionOrbit;
import rpnumerics.RarefactionOrbitCalc;

public class RarefactionOrbitGeomFactory extends WaveCurveOrbitGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public RarefactionOrbitGeomFactory(RarefactionOrbitCalc calc) {
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

        RarefactionOrbit orbit = (RarefactionOrbit) geomSource();


        return new RarefactionGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbit.getPoints()), this);

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

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
