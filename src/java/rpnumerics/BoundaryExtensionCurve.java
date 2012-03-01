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

//        super(rightSegments);


    }
}
