/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

public class SubInflectionExtensionCurve extends BifurcationCurve {// RPnCurve implements RpSolution {
    //
    // Members
    //
    public SubInflectionExtensionCurve(List<HugoniotSegment> hSegments, List<HugoniotSegment> rightSegments) {
//        super(hSegments,rightSegments);//, new ViewingAttr(Color.RED));
        super(rightSegments);//, new ViewingAttr(Color.RED));
//        hugoniotSegments_ = hSegments;

    }
}
