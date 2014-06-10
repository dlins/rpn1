  /*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.Configuration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import wave.util.RealVector;

public class RiemannProfileCalc implements RpCalculation,RpDiagramCalc {
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
    private RealVector pointOnSecondWaveCurve_;
    private RealVector referencePointOfFirstWaveCurve_;

    int firstWaveCurveID_;
    int secondWaveCurveID_;

    private final boolean isAllProfile_;

    //
    // Constructors/Initializers
    //
    public RiemannProfileCalc(WaveCurve firstWaveCurve, WaveCurve secondWaveCurve, RealVector pointOnSecondWaveCurve) {

        firstWaveCurveID_ = firstWaveCurve.getId();
        secondWaveCurveID_ = secondWaveCurve.getId();

        pointOnSecondWaveCurve_ = pointOnSecondWaveCurve;
        referencePointOfFirstWaveCurve_ = firstWaveCurve.getReferencePoint().getCoords();

        isAllProfile_ = true;

    }

    public RiemannProfileCalc(Area selectedArea, int[] waveCurvesID) {
        area_ = selectedArea;
        waveCurvesID_ = waveCurvesID;

        isAllProfile_ = false;
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

        RpSolution result = null;

        if (isAllProfile_) {
            
            result= nativeAllProfileCalc(firstWaveCurveID_, secondWaveCurveID_, referencePointOfFirstWaveCurve_, pointOnSecondWaveCurve_);

        } else {
            RealVector pmin = area_.getDownLeft();
            RealVector pmax = area_.getTopRight();

           result= nativeCalc(pmin, pmax, waveCurvesID_);

        }

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc(RealVector pmin, RealVector pmax, int[] waveCurveIdArray) throws RpException;

    private native RpSolution nativeAllProfileCalc(int firstWaveCurveID, int secondWaveCurveID, RealVector firstWaveCurveRefPoint, RealVector pointOnSecondWaveCurve) throws RpException;

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RpSolution recalc(List<Area> area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RpSolution createDiagramSource() {
        try {
            return calc();
        } catch (RpException ex) {
            Logger.getLogger(RiemannProfileCalc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public RpSolution updateDiagramSource() {
        try {
            return calc();
        } catch (RpException ex) {
            Logger.getLogger(RiemannProfileCalc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }
}
