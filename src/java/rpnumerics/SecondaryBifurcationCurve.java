/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import wave.util.RealSegment;


public class SecondaryBifurcationCurve extends BifurcationCurve {
    //
    // Members
    //


    public SecondaryBifurcationCurve(List<RealSegment> hSegments) {
        super(hSegments);
    }


    public SecondaryBifurcationCurve(List<RealSegment> hSegments, List<RealSegment> rightSegments) {
        super(hSegments, rightSegments);

    }
}
