/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.CurveConfiguration;

public class WaveCurveCalc extends WaveCurveOrbitCalc {

    public static int DOMAIN = 0;
    public static int BOUNDARY = 1;
    public static int INFLECTION = 2;
    //
    // Constants
    //
    // Constructors/Initializers
    //

    public WaveCurveCalc(PhasePoint point, int familyIndex, int timeDirection, int origin, int edge) {

        super(new OrbitPoint(point), familyIndex, timeDirection);

    }

    public WaveCurveCalc(PhasePoint input, CurveConfiguration waveCurveConfiguration) {

        super(new OrbitPoint(input), Integer.parseInt(waveCurveConfiguration.getParam("family")), Integer.parseInt(waveCurveConfiguration.getParam("direction")));

        configuration_ = ((CurveConfiguration)RPNUMERICS.getConfiguration("wavecurve")).clone();

    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    @Override
    public RpSolution calc() throws RpException {
        WaveCurve result;

        result = (WaveCurve) nativeCalc(getStart(), (CurveConfiguration)RPNUMERICS.getConfiguration("wavecurve"));

       

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc(OrbitPoint initialPoint, CurveConfiguration config);

}
