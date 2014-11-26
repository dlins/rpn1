/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class DiscriminantLevelCurveCalc extends CharacteristicPolynomialLevelCalc {


    public DiscriminantLevelCurveCalc(double level, ContourParams params) {
        super(level, params);
        configuration_ = RPNUMERICS.getConfiguration("levelcurve");
        configuration_.setParamValue("resolution", RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));

    }

    protected DiscriminantLevelCurveCalc(ContourParams params) {
        super(params);
        configuration_ = RPNUMERICS.getConfiguration("levelcurve");
        configuration_.setParamValue("resolution", RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));

    }


    @Override
    public RpSolution calc() throws RpException {

        CharacteristicsPolynomialCurve result = (CharacteristicsPolynomialCurve) calcNative(getLevel());

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;

    }


    private native RpSolution calcNative(double level);


}
