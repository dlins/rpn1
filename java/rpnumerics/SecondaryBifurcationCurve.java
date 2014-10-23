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
        System.out.println("Usou o CTOR apenas com hSegments ...");
    }


    public SecondaryBifurcationCurve(List<RealSegment> hSegments, List<RealSegment> rightSegments) {
        super(hSegments, rightSegments);
        System.out.println("Usou o CTOR com hSegments e rightSegments ...");

    }
}
