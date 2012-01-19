/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;


public class DoubleContactCurve extends BifurcationCurve {
    //
    // Members
    //

    public DoubleContactCurve(List<HugoniotSegment> hSegments) {
        super(hSegments);
    }

    public DoubleContactCurve(List<HugoniotSegment> hSegments, List<HugoniotSegment> rightSegments) {
        super(hSegments, rightSegments);//, new ViewingAttr(Color.RED));

    }
}
