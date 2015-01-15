/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class CompositeCurve extends FundamentalCurve implements RpSolution {
  
    //
    // Members
    //

    public CompositeCurve(OrbitPoint [] rarefaction,int increase,int familyIndex) {
        super(rarefaction,familyIndex,increase);


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

    public String toMatlab() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
