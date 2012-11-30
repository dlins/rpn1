/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.CommandConfiguration;
import rpn.configuration.Configuration;
import rpn.configuration.ConfigurationProfile;
import wave.util.RealVector;

public class PointLevelCalc extends LevelCurveCalc {

    private RealVector startPoint_;

    public PointLevelCalc(RealVector point, int family, ContourParams params) {
        super(family, params);
        startPoint_ = point;


        String className = getClass().getSimpleName().toLowerCase();

        String curveName = className.replace("calc", "");

        configuration_ = new CommandConfiguration(curveName);
        configuration_.setParamValue("family", String.valueOf(family));
        configuration_.setParamValue("level", String.valueOf(family));





    }

    @Override
    public RpSolution calc() throws RpException {
        LevelCurve result = (LevelCurve) calcNative(getFamily(), startPoint_, getParams().getResolution());

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

    private native RpSolution calcNative(int family, RealVector point, int[] resolution);
}
