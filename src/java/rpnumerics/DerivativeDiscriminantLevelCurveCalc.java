/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class DerivativeDiscriminantLevelCurveCalc extends CharacteristicPolynomialLevelCalc {
    private final int u_;

    public DerivativeDiscriminantLevelCurveCalc(int u,ContourParams params) {
        super(params);
        configuration_=RPNUMERICS.getConfiguration("levelcurve");
        configuration_.setParamValue("resolution", RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));
        u_=u;
    }

    public RpSolution calc() throws RpException {

        CharacteristicsPolynomialCurve result = (CharacteristicsPolynomialCurve) calcNative(u_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;

    }


    private native RpSolution calcNative(int u);

   
}
