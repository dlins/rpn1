/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import wave.util.RealSegment;

public class BoundaryExtensionCurve extends BifurcationCurve {// RPnCurve implements RpSolution {
    //
    // Members
    //
    public BoundaryExtensionCurve(List<RealSegment> leftSegments, List<RealSegment> rightSegments) {
        super(leftSegments,rightSegments);

    }

    public BoundaryExtensionCurve(List<RealSegment> segments) {
        super(segments);
    }


    /*
    @Override
    public String toXML() {


        StringBuffer buffer = new StringBuffer();

        buffer.append(super.toXML());

        BoundaryExtensionCurveCalc boundaryExtensionCurveCalc = (BoundaryExtensionCurveCalc) rpCalc();

        buffer.append("curvefamily=\"" + boundaryExtensionCurveCalc.getCurveFamily()
                + "\"" + " domainfamily=\"" + boundaryExtensionCurveCalc.getDomainFamily()
                + "\"" + " characteristic=\"" + boundaryExtensionCurveCalc.getCharacteristicWhere()
                + "\"" + " edge=\"" + boundaryExtensionCurveCalc.getEdge()
                + "\"" + " edgeresolution=\"" + boundaryExtensionCurveCalc.getEdgeResolution() + "\""
                + ">\n");

        buffer.append(((BifurcationCurve) geomSource()).toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();


    }
     *
     */

}
