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


public class DoubleContactCurve extends BifurcationCurve {
    //
    // Members
    //


    public DoubleContactCurve(List<RealSegment> hSegments) {
        super(hSegments);
    }


    public DoubleContactCurve(List<RealSegment> hSegments, List<RealSegment> rightSegments) {
        super(hSegments, rightSegments);

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
