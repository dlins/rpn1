/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

public class CompositeCurve extends SegmentedCurve {
    //
    // Members
    //

    public CompositeCurve(List<HugoniotSegment> hSegments) {
        super(hSegments);


    }

    private static List createSingleSegmentList(List<HugoniotSegment> leftSeg, List<HugoniotSegment> rightSeg) {

        System.out.println("Tamanho left: "+leftSeg.size());
        System.out.println("Tamanho right: "+rightSeg.size());

        for (HugoniotSegment hugoniotSegment : leftSeg) {
            hugoniotSegment.setIntType(16);
        }
        for (HugoniotSegment hugoniotSegment : rightSeg) {
            hugoniotSegment.setIntType(15);
        }

        if (leftSeg.addAll(rightSeg)) {

            return leftSeg;
        } else {
            return null;
        }

    }
}
