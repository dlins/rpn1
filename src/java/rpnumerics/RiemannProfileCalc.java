  /*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.List;
import wave.util.RealVector;

public class RiemannProfileCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    private OrbitPoint start_;
    private int timeDirection_;
    private RealVector[] poincareSection_;
    private Area area_;
    private List<OrbitPoint> forwardList_;
    private List<OrbitPoint>  backwardList_;

    //
    // Constructors/Initializers
    //
    public RiemannProfileCalc(Area area, WaveCurve forwardCurve, WaveCurve backwardCurve) {

        area_=area;



        List<WaveCurveBranch> forwardBranch = forwardCurve.getBranchsList();


        forwardList_ = new ArrayList<OrbitPoint>();

        for (WaveCurveBranch waveCurveBranch : forwardBranch) {


            WaveCurveOrbit orbit = (WaveCurveOrbit)waveCurveBranch;


            OrbitPoint[] points = orbit.getPoints();


            for (OrbitPoint orbitPoint : points) {
                forwardList_.add(orbitPoint);
            }

        }

          List<WaveCurveBranch> backwardBranch = backwardCurve.getBranchsList();

        backwardList_=new ArrayList<OrbitPoint>();

        for (WaveCurveBranch waveCurveBranch : backwardBranch) {


            WaveCurveOrbit orbit = (WaveCurveOrbit)waveCurveBranch;


            OrbitPoint[] points = orbit.getPoints();


            for (OrbitPoint orbitPoint : points) {
                backwardList_.add(orbitPoint);
            }

        }


    }


    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {

        RealVector pmin = area_.getDownLeft();
        RealVector pmax = area_.getTopRight();


        RpSolution result = nativeCalc(pmin, pmax, forwardList_, backwardList_);

        if (result==null){
            throw new RpException("Error in native layer");
        }


        return result;
    }


    private native RpSolution nativeCalc (RealVector pmin,RealVector pmax, List<OrbitPoint> forwardWaveCurve,List<OrbitPoint> backwardWaveCurve ) throws RpException;

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
