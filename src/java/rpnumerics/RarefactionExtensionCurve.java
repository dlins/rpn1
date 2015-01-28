/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import wave.util.RealSegment;

public class RarefactionExtensionCurve extends BifurcationCurve {
    //
    // Members
    //

    public RarefactionExtensionCurve(List<RealSegment> hSegments, List<RealSegment> rightSegments) {
        super(hSegments,rightSegments);


    }

    private static List createSingleSegmentList(List<RealSegment> leftSeg, List<RealSegment> rightSeg) {

        System.out.println("Tamanho left: " + leftSeg.size());
        System.out.println("Tamanho right: " + rightSeg.size());
       

        if (leftSeg.addAll(rightSeg)) {

            return leftSeg;
        } else {
            return null;
        }

    }

    public String toXML() {

        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < segments().size(); i++) {

            RealSegment realSegment = (RealSegment) segments().get(i);
            buffer.append(realSegment.toXML());
        }
        return buffer.toString();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*

     @Override
    public String toXML() {
        StringBuilder str = new StringBuilder();

        RarefactionExtensionCalc calc = (RarefactionExtensionCalc) rpCalc();

        RealVector firstPoint = new RealVector(calc.getStart());

        String commandName = geomSource().getClass().getName();

        commandName = commandName.toLowerCase();

        commandName = commandName.replaceAll(".+\\.", "");

        str.append("<COMMAND name=\"" + commandName + "\"");

        if (calc.getIncrease() != Orbit.BOTH_DIR) {
            String direction = "forward\"";

            if (calc.getIncrease() == Orbit.BACKWARD_DIR) {
                direction = "backward\"";

            }
            str.append(" direction=\"");
            str.append(direction+" ");
        }




        str.append(calc.getParams().toString() + "\"" + " inputpoint=\"" + firstPoint.toString() + "\" curvefamily=\"" + calc.getCurveFamily() + "\" domainfamily =\""
                + calc.getDomainFamily() + "\" " + "characteristic=\""+ calc.getCharacteristic()+"\""+ ">\n");
        str.append(((RarefactionExtensionCurve) geomSource()).toXML());
        str.append("</COMMAND>\n");
        return str.toString();


    }*/

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   
}
