/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.List;

public class ShockCurveCalc extends OrbitCalc implements RpCalculation {
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
    public ShockCurveCalc(PhasePoint point, int family, int increase) {
        super(new OrbitPoint(point), family, increase);
    }



    //
    // Methods
    //
    public RpSolution recalc() throws RpException {

        return calc();

    }

    private static HugoniotCurve concat(HugoniotCurve backward, HugoniotCurve forward) {
        // opposite time directions assumed...
        List<HugoniotSegment> result = new ArrayList<HugoniotSegment>();//OrbitPoint[backward.getPoints().length +
//                forward.getPoints().length - 1];

        for (int i = backward.segments().size() - 1; i > 0; i--) {

            result.add((HugoniotSegment) backward.segments().get(i));
        }

        result.addAll(forward.segments());
        return new HugoniotCurve(forward.getXZero(), result);

    }

    public RpSolution calc() throws RpException {

//        if (getDirection() == 0) {
//
//            ShockCurve resultForward = (ShockCurve)calc("methodName_", newtonTolerance_, getStart(), getFamilyIndex(), 20);
//            ShockCurve resultBackward = (ShockCurve) calc("methodName_", newtonTolerance_, getStart(), getFamilyIndex(), 22);
////            Orbit resultComplete = ShockCurve.concat(resultBackward, resultForward);
//            ShockCurve completeCurve = concat(resultBackward, resultForward);
//
//
//            if (resultBackward == null || resultForward == null) {
//                throw new RpException("Error in native layer");
//            }
//
//            //** acrescentei isso (Leandro)
//            RPnCurve.lista.add((RPnCurve) completeCurve);
//            System.out.println("Tamanho da lista: " + RPnCurve.lista.size());
//            //***
//
//            return completeCurve;
//        }

        RpSolution result = calc("methodName_", newtonTolerance_, getStart(), getFamilyIndex(), getDirection());

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution calc(String methodName, double newtonTolerance, PhasePoint initialpoint, int familyIndex, int timeDirection) throws RpException;

   

}
