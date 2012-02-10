/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;

public class IntegralCurveCalc extends OrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    

    //
    // Constructors/Initializers
    //
    public IntegralCurveCalc(PhasePoint point,int familyIndex) {
        super(new OrbitPoint(point), familyIndex, 0);
       
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
