/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class HugoniotContinuationCurveCalc extends WaveCurveOrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    //
    // Constructors/Initializers
    //
    public HugoniotContinuationCurveCalc(PhasePoint point, int increase) {
        super(new OrbitPoint(point),0,  increase);


    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {

        return calc();

    }


    public RpSolution calc() throws RpException {

        WaveCurve result = (WaveCurve) calc(getStart(), getDirection());//TODO Wave Curve sera um container para curvas desse tipo

        if (result == null) {
            throw new RpException("Error in native layer");
        }
        
         result.setReferencePoint(getStart());

        return result;
    }

    private native RpSolution calc(PhasePoint initialpoint, int timeDirection) throws RpException;

   

}
