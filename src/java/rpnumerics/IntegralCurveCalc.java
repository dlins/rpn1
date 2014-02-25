/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.CurveConfiguration;

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
        
        
         CurveConfiguration  config  = (CurveConfiguration)RPNUMERICS.getConfiguration("fundamentalcurve");
        
        configuration_= config.clone();
        
        String [] parameterToKeep  = {"family","level"};
        
        configuration_.keepParameters(parameterToKeep);
        
        

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


        result.setReferencePoint(getStart());
        
        return result;


    }

    private native RpSolution calc(PhasePoint initialpoint, int family) throws RpException;
}
