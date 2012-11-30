/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.CommandConfiguration;
import rpn.configuration.Configuration;
import rpn.configuration.ConfigurationProfile;

public class IntegralCurveCalc extends WaveCurveOrbitCalc implements RpCalculation {

    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public IntegralCurveCalc(PhasePoint point, int familyIndex) {
        super(new OrbitPoint(point), familyIndex, 0);

        String className = getClass().getSimpleName().toLowerCase();

        String curveName = className.replace("calc", "");

        configuration_ = new CommandConfiguration(curveName);

        configuration_.setParamValue("family", String.valueOf(familyIndex));
        configuration_.setParamValue("inputpoint", point.toString());

    }
    //
    // Methods
    //

    @Override
    public RpSolution calc() throws RpException {

        IntegralCurve result;
        result = (IntegralCurve) calc(getStart(), getFamilyIndex());

        if (result == null) {
            throw new RpException("Error in native layer");
        }


        return result;


    }

    private native RpSolution calc(PhasePoint initialpoint, int family) throws RpException;
}
