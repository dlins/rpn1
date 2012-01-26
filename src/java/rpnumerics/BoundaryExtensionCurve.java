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

//    private List hugoniotSegments_;
//    public DoubleContactCurve(List<HugoniotSegment> hSegments) {
//        super(hSegments);//, new ViewingAttr(Color.RED));
////        hugoniotSegments_ = hSegments;
//
//    }
    public BoundaryExtensionCurve(List<RealSegment> leftSegments, List<RealSegment> rightSegments) {
//        super(hSegments, rightSegments);//, new ViewingAttr(Color.RED));

        super(rightSegments);//, new ViewingAttr(Color.RED));


    }
}
