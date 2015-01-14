/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;

public class EigenValuePointLevelCalc extends EigenValueLevelCalc {

    private RealVector startPoint_;

    public EigenValuePointLevelCalc(RealVector point, int family, ContourParams params) {
        super(family, params);
        startPoint_ = point;
        configuration_ = RPNUMERICS.getConfiguration("levelcurve");

    }

    @Override
    public RpSolution calc() throws RpException {
        EigenValueCurve result = (EigenValueCurve) calcNative(getFamily(), startPoint_);

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

    private native RpSolution calcNative(int family, RealVector point);
}
