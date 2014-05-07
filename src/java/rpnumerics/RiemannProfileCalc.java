  /*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.Configuration;
import java.util.List;
import wave.util.RealVector;

public class RiemannProfileCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    private Area area_;
    private List<FundamentalCurve> forwardList_;
    private List<FundamentalCurve> backwardList_;
    private Configuration configuration_;
    private int[] waveCurvesID_;

    //
    // Constructors/Initializers
    //
    public RiemannProfileCalc(Area area, WaveCurve forwardCurve, WaveCurve backwardCurve) {

     
//        List<WaveCurveBranch> forwardBranch = forwardCurve.getBranchsList();
//        forwardList_ = new ArrayList<FundamentalCurve>();
//
//        for (WaveCurveBranch waveCurveBranch : forwardBranch) {
//
//
//            for (WaveCurveBranch waveCurveBranch2 : waveCurveBranch.getBranchsList()) {
//
//                FundamentalCurve orbit = (FundamentalCurve) waveCurveBranch2;
//
//                forwardList_.add(orbit);
//
//            }
//
//        }
//
//        List<WaveCurveBranch> backwardBranch = backwardCurve.getBranchsList();
//
//        backwardList_ = new ArrayList<FundamentalCurve>();
//
//        for (WaveCurveBranch waveCurveBranch : backwardBranch) {
//
//
//            for (WaveCurveBranch waveCurveBranch2 : waveCurveBranch.getBranchsList()) {
//
//                FundamentalCurve orbit = (FundamentalCurve) waveCurveBranch2;
//
//                backwardList_.add(orbit);
//
//            }
//
//        }



//        String className = getClass().getSimpleName().toLowerCase();
//
//        String curveName = className.replace("calc", "");
//
//        configuration_ = new CommandConfiguration(curveName);
//
//        configuration_.setParamValue("forwardwavecurve", String.valueOf(forwardCurve.getId()));
//        configuration_.setParamValue("backwardwavecurve", String.valueOf(backwardCurve.getId()));



    }

    public RiemannProfileCalc(Area selectedArea, int[] waveCurvesID) {
           area_ = selectedArea;
           waveCurvesID_=waveCurvesID;
    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public Configuration getConfiguration() {
        return configuration_;
    }

    public RpSolution calc() throws RpException {

        RealVector pmin = area_.getDownLeft();
        RealVector pmax = area_.getTopRight();


        RpSolution result = nativeCalc(pmin, pmax, waveCurvesID_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }


        return result;
    }

    private native RpSolution nativeCalc(RealVector pmin, RealVector pmax,int [] waveCurveIdArray) throws RpException;

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RpSolution recalc(List<Area> area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
