/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

public class CoincidenceExtensionCurve extends BifurcationCurve{
    //
    // Members
    //

    public CoincidenceExtensionCurve(List<HugoniotSegment> leftSegments,List<HugoniotSegment> rightSegments) {
        super(leftSegments,rightSegments);
//        super(rightSegments);
//        super(leftSegments);

    }
  
}
