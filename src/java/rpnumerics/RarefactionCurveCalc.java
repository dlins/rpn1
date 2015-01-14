/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class RarefactionCurveCalc extends WaveCurveOrbitCalc implements RpCalculation {
    private int edge_;
    //
    // Constants
    //
    //
    // Members
    //
    private final boolean fromBoundary_;

    // Constructors/Initializers
    //
    public RarefactionCurveCalc(PhasePoint point, int familyIndex,int timeDirection) {

        super(new OrbitPoint(point), familyIndex,timeDirection);
        fromBoundary_=false;
    }
    
    
    public RarefactionCurveCalc(PhasePoint point, int familyIndex, int timeDirection, int edge) {

        super(new OrbitPoint(point), familyIndex, timeDirection);
        edge_=edge;
        fromBoundary_=true;

    }

    //
    // Accessors/Mutators
    //
   
    //
    // Methods
    //
    @Override
    public RpSolution recalc() throws RpException {
        return calc();

    }

    @Override
    public RpSolution calc() throws RpException {
        RarefactionCurve result;

        if(fromBoundary_) {
            result=(RarefactionCurve)  boundaryNativeCalc(getStart(), getFamilyIndex(), getDirection(), edge_);
        }
        else {
            result = (RarefactionCurve) calc("methodName_", "flowName_", getStart(), getFamilyIndex(), getDirection());
        }

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        
        return result;

    }

    private  RarefactionCurve concat(RarefactionCurve backward, RarefactionCurve forward) {
        // opposite time directions assumed...
        OrbitPoint[] swap = new OrbitPoint[backward.getPoints().length
                + forward.getPoints().length - 1];

        for (int i = 0, j = backward.getPoints().length - 1; i < swap.length; i++) {
            if (i >= backward.getPoints().length) {
                swap[i] = (OrbitPoint) forward.getPoints()[i - backward.getPoints().length + 1];
            } else {
                swap[i] = backward.getPoints()[j--];

            }
        }

        return new RarefactionCurve(swap, getFamilyIndex(), Orbit.BOTH_DIR);

    }
 

    private native RpSolution boundaryNativeCalc(OrbitPoint initialPoint, int family, int timeDirection, int edge) throws RpException; 

    private native RpSolution calc(String methodName, String flowName, PhasePoint initialpoint, int familyIndex, int timeDirection) throws RpException;


}
