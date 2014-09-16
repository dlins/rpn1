
/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import wave.util.RealSegment;

public interface WaveCurveBranch extends RpDiagramCalc{

    List<WaveCurveBranch> getBranchsList();
    OrbitPoint getReferencePoint() throws RpException;
    void setReferencePoint(OrbitPoint referencePoint);
    List<OrbitPoint> getBranchPoints();
    
    double getSpeed(OrbitPoint point);
    
    double [] getCoordByArcLength(double x) throws  RpException;
    
    
    List<RealSegment> segments();
    

}
