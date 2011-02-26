/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

public class ExtensionCurve extends BifurcationCurve{// RPnCurve implements RpSolution {
    //
    // Members
    //

//    private List hugoniotSegments_;

//    public DoubleContactCurve(List<HugoniotSegment> hSegments) {
//        super(hSegments);//, new ViewingAttr(Color.RED));
////        hugoniotSegments_ = hSegments;
//
//    }


     public ExtensionCurve(List<HugoniotSegment> hSegments,List<HugoniotSegment> rightSegments) {
        super(hSegments,rightSegments);//, new ViewingAttr(Color.RED));
//        hugoniotSegments_ = hSegments;

    }


}
