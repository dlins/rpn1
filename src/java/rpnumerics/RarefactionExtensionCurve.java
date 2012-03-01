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
//        super(createSingleSegmentList(hSegments, rightSegments));

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

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < segments().size(); i++) {

            RealSegment realSegment = (RealSegment) segments().get(i);
            buffer.append(realSegment.toXML());
        }
        return buffer.toString();
    }
}
