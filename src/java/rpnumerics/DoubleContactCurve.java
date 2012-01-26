/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import wave.util.RealSegment;


public class DoubleContactCurve extends BifurcationCurve {
    //
    // Members
    //


    public DoubleContactCurve(List<RealSegment> hSegments) {
        super(hSegments);
    }


    public DoubleContactCurve(List<RealSegment> hSegments, List<RealSegment> rightSegments) {
        super(hSegments, rightSegments);//, new ViewingAttr(Color.RED));

    }
}
