/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class RarefactionCurveCalc extends WaveCurveOrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    // Constructors/Initializers
    //
    public RarefactionCurveCalc(PhasePoint point, int familyIndex,int timeDirection) {

        super(new OrbitPoint(point), familyIndex,timeDirection);
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


    // ----- 30JAN : Dan pediu para fazer rarefação em ambas as direções
    // ----- Incluí o método concat(...)
    @Override
    public RpSolution calc() throws RpException {
        RarefactionCurve result;

        if(getDirection()== Orbit.BOTH_DIR) {
            RarefactionCurve forward  = (RarefactionCurve) calc("methodName_", "flowName_", getStart(), getFamilyIndex(), Orbit.FORWARD_DIR);
            RarefactionCurve backward = (RarefactionCurve) calc("methodName_", "flowName_", getStart(), getFamilyIndex(), Orbit.BACKWARD_DIR);
            result =  concat(backward, forward);
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
    // -----


    // --- Método calc() original
//    @Override
//    public RpSolution calc() throws RpException {
//
//        RarefactionCurve result;
//
//        result = (RarefactionCurve) calc("methodName_", "flowName_", getStart(), getFamilyIndex(), getDirection());
//
//
//        if (result == null) {
//            throw new RpException("Error in native layer");
//        }
//
//
//        return result;
//
//    }





    private native RpSolution calc(String methodName, String flowName, PhasePoint initialpoint, int familyIndex, int timeDirection) throws RpException;


}
