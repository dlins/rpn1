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


//    @Override
//    public String toString() {
//        StringBuilder stringBuilder = new StringBuilder();
//
//        for (WaveCurveBranch waveCurveBranch : branchList_) {
//
//
//            for (WaveCurveBranch waveCurveBranch2 : waveCurveBranch.getBranchsList()) {
//
//                FundamentalCurve orbit = (FundamentalCurve) waveCurveBranch2;
//
//                stringBuilder.append("--------Inicio branch-------------"+orbit.getCurveType()+"\n");
//
//                for (int i = 0; i < orbit.getPoints().length; i++) {
//
//                    stringBuilder.append(orbit.getPoints()[i] + " " + orbit.getPoints()[i].getCorrespondingCurveIndex() + " " + orbit.getPoints()[i].getCorrespondingPointIndex()+"\n");
//
//                }
//
//                stringBuilder.append("--------Fim branch-------------\n");
//            }
//        }
//        return stringBuilder.toString();
//
//    }




    @Override
    public String toXML() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
