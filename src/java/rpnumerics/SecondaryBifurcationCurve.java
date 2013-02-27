/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.List;
import wave.util.RealSegment;
import wave.util.RealVector;


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

    // --- Acrescentei este método em 06FEV2013
    @Override
    public List<RealVector> correspondentPoints(RealVector pMarca) {

        List<RealVector> correspondent = new ArrayList();
        int i = findClosestSegment(pMarca);
        RealVector p = secondPointDCOtherVersion(i);
        correspondent.add(p);

        return correspondent;
    }
    // ---

}
