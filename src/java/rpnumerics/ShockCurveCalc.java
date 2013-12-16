/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class ShockCurveCalc extends WaveCurveOrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //
    
    private double newtonTolerance_;

    //
    // Constructors/Initializers
    //
    public ShockCurveCalc(PhasePoint point, int family, int increase, double tolerance) {
        super(new OrbitPoint(point),family,  increase);
        newtonTolerance_=tolerance;

    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {

        return calc();

    }


    public RpSolution calc() throws RpException {

        ShockCurve result = (ShockCurve)calc("methodName_", newtonTolerance_, getStart(), getFamilyIndex(), getDirection());
        
        if (result == null) {
            throw new RpException("Error in native layer");
        }
        
        result.setReferencePoint(getStart());
        
        return result;
    }

    private native RpSolution calc(String methodName, double newtonTolerance, PhasePoint initialpoint, int familyIndex, int timeDirection) throws RpException;

   

}
