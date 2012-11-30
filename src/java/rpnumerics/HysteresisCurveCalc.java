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

public class HysteresisCurveCalc extends ContourCurveCalc {

    private int family_;

    //
    // Constructors/Initializers
    //
    public HysteresisCurveCalc(ContourParams params, int family) {
        super(params);
        family_ = family;


        String className = getClass().getSimpleName().toLowerCase();

        String curveName = className.replace("calc", "");

        configuration_ = new CommandConfiguration(curveName);
        configuration_.setParamValue("family", String.valueOf(family));

    }

    @Override
    public RpSolution calc() throws RpException {

        int resolution[] = getParams().getResolution();
        HysteresisCurve result = (HysteresisCurve) nativeCalc(family_, resolution);

        if (result == null) {
            throw new RpException("Error in native layer");
        }


        return result;
    }

    public int getFamily() {
        return family_;
    }

    private native RpSolution nativeCalc(int family,
            int[] resolution) throws RpException;
}
