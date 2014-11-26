/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class EnvelopeCurveCalc extends ContourCurveCalc {

    //
    // Constructors
    //

    private int whereIsConstant_;
    private int numberOfSteps_;

    public EnvelopeCurveCalc(ContourParams params,int whereIsConstant,int numberOfSteps) {
        super(params);
        whereIsConstant_=whereIsConstant;
        numberOfSteps_=numberOfSteps;
        
        configuration_=RPNUMERICS.getConfiguration("envelopecurve");

    }

    public RpSolution calc() throws RpException {

        EnvelopeCurve result;

        result = (EnvelopeCurve) nativeCalc(whereIsConstant_,numberOfSteps_);

          if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;

    }

    public RpSolution recalc() throws RpException {

        return calc();
    }

    private native RpSolution nativeCalc(int whereIsConstant,int numberOfSteps) throws RpException;

}
