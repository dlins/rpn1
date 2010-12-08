/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.List;
import wave.ode.ODESolver;

public class ShockCurveCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    private PhasePoint start_;
    private int timeDirection_;
    private String methodName_;
    private String flowName_;
    private int familyIndex_;

    //
    // Constructors/Initializers
    //
    public ShockCurveCalc(PhasePoint point, int timeDirection) {
        start_ = point;
        timeDirection_ = timeDirection;
    }

    public ShockCurveCalc(String methodName, String flowName, PhasePoint point, int familyIndex, int timeDirection) {

        methodName_ = methodName;

        start_ = point;
        timeDirection_ = timeDirection;
        familyIndex_ = familyIndex;
        methodName_ = "ContinuationShockMethod";//TODO Put the correct method name

    }

    //
    // Accessors/Mutators
    //
    public int tDirection() {
        return timeDirection_;
    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {

        return calc();

    }



    private static HugoniotCurve concat(HugoniotCurve backward, HugoniotCurve forward) {
        // opposite time directions assumed...
        List<HugoniotSegment> result = new ArrayList<HugoniotSegment>();//OrbitPoint[backward.getPoints().length +
//                forward.getPoints().length - 1];

        for(int i= backward.segments().size()-1;i >0;i--){

            result.add((HugoniotSegment)backward.segments().get(i));
        }

        result.addAll(forward.segments());
        return new HugoniotCurve(forward.getXZero(), result);

    }


    public RpSolution calc() throws RpException {

        if (timeDirection_ == 0) {

            HugoniotCurve resultForward = (HugoniotCurve) calc(methodName_, flowName_, start_, familyIndex_, 1);
            HugoniotCurve resultBackward = (HugoniotCurve) calc(methodName_, flowName_, start_, familyIndex_, -1);
//            Orbit resultComplete = ShockCurve.concat(resultBackward, resultForward);
            HugoniotCurve completeCurve = concat(resultBackward,resultForward);


              if (resultBackward == null || resultForward == null) {
                throw new RpException("Error in native layer");
            }



            return completeCurve;
        }

        RpSolution result = calc(methodName_, flowName_, start_, familyIndex_, timeDirection_);

          if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution calc(String methodName, String flowName, PhasePoint initialpoint, int familyIndex, int timeDirection) throws RpException;

    public String getCalcMethodName() {
        return methodName_;

    }
}
