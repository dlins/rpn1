/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;

public class DiscriminantPointLevelCalc extends DiscriminantLevelCurveCalc {

    private RealVector startPoint_;

    public DiscriminantPointLevelCalc(RealVector point,  ContourParams params) {
        super(params);
        startPoint_ = point;
        configuration_ = RPNUMERICS.getConfiguration("levelcurve");

    }

    @Override
    public RpSolution calc() throws RpException {
        CharacteristicsPolynomialCurve result = (CharacteristicsPolynomialCurve) calcNative(startPoint_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;

    }

    public RealVector getStartPoint() {
        return startPoint_;


    }

    public void setStartPoint(RealVector startPoint) {
        startPoint_ = new RealVector(startPoint);
    }

    private native RpSolution calcNative( RealVector point);
}
