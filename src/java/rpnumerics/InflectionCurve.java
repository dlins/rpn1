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

public class InflectionCurve extends BifurcationCurve {

    //
    // Members
    //
    public InflectionCurve(List<RealSegment> hSegments) {
        super(hSegments);
        
    }
    
    
    
  
    @Override
    public List<RealVector> correspondentPoints(RealVector pMarca) {

        List<RealVector> correspondent = new ArrayList();
        int i = findClosestSegment(pMarca);
        RealVector p = secondPointDCOtherVersion(i);
        correspondent.add(p);

        return correspondent;
    }
    // ---

    @Override
    public String toMatlab(int curveIndex) {

        StringBuilder buffer = new StringBuilder();
        buffer.append("%%\nclose all;clear all;\n");
        //buffer.append(RpCalcBasedGeomFactory.createMatlabColorTable());
        buffer.append(toMatlabData(0));

        buffer.append("%%\n% begin plot x y\n");
        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
        buffer.append(createMatlabPlotLoop(0, 1, 0));

        return buffer.toString();
    }
    
    
    
    
    
}
