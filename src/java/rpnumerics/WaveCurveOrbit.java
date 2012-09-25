/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.List;
import rpn.component.MultidAdapter;
import wave.util.RealSegment;

public class WaveCurveOrbit extends Orbit implements WaveCurveBranch, RpSolution {

    private int familyIndex_;
    private List<WaveCurveBranch> orbitList_;
    private ArrayList<RealSegment> segments_ = new ArrayList();
    private int curveType_;
    private int curveIndex_;
    private boolean initialSubCurve_;

    public WaveCurveOrbit(OrbitPoint[] points, int family, int increase) {
        super(points, increase);
        familyIndex_ = family;
        orbitList_= new ArrayList<WaveCurveBranch>();
        orbitList_.add(this);

        segments_ = MultidAdapter.converseCoordsArrayToRealSegments(MultidAdapter.converseRPnCurveToCoordsArray(this));
        
    }

    public int getFamilyIndex() {
        return familyIndex_;
    }




  

    public List<WaveCurveBranch>getCurves() {

        return orbitList_;

    }

    public int getCurveType() {
        return curveType_;
    }

    public int getCurveIndex() {
        return curveIndex_;
    }

    public void setCurveIndex(int curveIndex_) {
        this.curveIndex_ = curveIndex_;
    }



    public void setCurveType(int curveType_) {
        this.curveType_ = curveType_;
    }

    public boolean isInitialSubCurve() {
        return initialSubCurve_;
    }

    public void setInitialSubCurve(boolean initialSubCurve_) {
        this.initialSubCurve_ = initialSubCurve_;
    }





    public List<WaveCurveBranch> getBranchsList() {

        List<WaveCurveBranch> result = new ArrayList<WaveCurveBranch>();

        result.add(this);

        return result;

    }


    @Override
    public List<RealSegment> segments() {
        return segments_;
    }

    


}
