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

    public WaveCurveOrbit(OrbitPoint[] points, int family, int increase) {
        super(points, increase);
        System.out.println("CTR de WaveCurveOrbit --------------- points.length : " +points.length);
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

    public List<WaveCurveOrbit> getSubCurvesList() {

        List<WaveCurveOrbit> result = new ArrayList<WaveCurveOrbit>();

        result.add(this);

        return result;

    }


    @Override
    public List<RealSegment> segments() {
        return segments_;
    }


}
