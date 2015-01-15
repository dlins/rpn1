/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.Iterator;
import java.util.List;

public class WaveCurveRRegions implements RpSolution {

    private List<WaveCurve> branchList_;
    

    public WaveCurveRRegions(List<WaveCurve> waveCurveList) {
        
        branchList_=waveCurveList;

    }


    public Iterator<WaveCurve> getCurvesIterator() {
        
        return branchList_.iterator();

    }
    
    
    public WaveCurve getWaveCurve(int index){
        return branchList_.get(index);
    }


    @Override
    public String toXML() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toMatlab() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
