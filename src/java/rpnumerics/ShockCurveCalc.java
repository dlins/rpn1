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
    private final int edge_;
    private final boolean fromBoundary_;

    //
    // Constructors/Initializers
    //
    public ShockCurveCalc(PhasePoint point, int family, int increase, double tolerance) {
        super(new OrbitPoint(point),family,  increase);
        newtonTolerance_=tolerance;
        fromBoundary_=false;
        edge_=0;

    }
    
    
    public ShockCurveCalc(PhasePoint point, int family, int increase, double tolerance,int edge) {
        super(new OrbitPoint(point),family,  increase);
        newtonTolerance_=tolerance;
        edge_=edge;
        fromBoundary_=true;

    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {

        return calc();

    }


    @Override
    public RpSolution calc() throws RpException {

        ShockCurve result ;
        
        if(fromBoundary_){
            result= (ShockCurve) boundaryCalc(getStart(), getFamilyIndex(), getDirection(),edge_);
        }
        
        else {
            result  = (ShockCurve)calc("methodName_", newtonTolerance_, getStart(), getFamilyIndex(), getDirection());
        }
        
        if (result == null) {
            
            throw new RpException("Error in native layer");
        }
        
        
        System.out.print(result.getReferencePoint().getEigenValues().length);
        
        return result;
    }

    private native RpSolution calc(String methodName, double newtonTolerance, PhasePoint initialpoint, int familyIndex, int timeDirection) throws RpException;
    private native RpSolution boundaryCalc(PhasePoint initialpoint, int familyIndex, int timeDirection,int edge) throws RpException;

   

}
