/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class IntegralCurveCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    private PhasePoint start_;
      private int familyIndex_;

    //
    // Constructors/Initializers
    //
    public IntegralCurveCalc(PhasePoint point,int familyIndex) {
        start_ = point;
        familyIndex_=familyIndex;
    }
    //
    // Methods
    //

    public RpSolution recalc() throws RpException {
        return calc();

    }

    public RpSolution calc() throws RpException {

        IntegralCurve result;

        RarefactionOrbit resultForward = (RarefactionOrbit) calc(start_, familyIndex_,20);
        RarefactionOrbit resultBackward = (RarefactionOrbit) calc(start_, familyIndex_,22);

        if (resultBackward == null || resultForward == null) {
            throw new RpException("Error in native layer");
        }


        Orbit resultComplete = Orbit.concat(resultBackward, resultForward);


        result = new IntegralCurve(resultComplete.getPoints(), familyIndex_);



        if (result == null) {
            throw new RpException("Error in native layer");
        }
        result.setFamilyIndex(familyIndex_);

        //** acrescentei isso (Leandro)
        RPnCurve.lista.add(result);
        System.out.println("Tamanho da lista: " + RPnCurve.lista.size());
        //***

        return result;


    }

    private native RpSolution calc(PhasePoint initialpoint, int family,int timeDirection) throws RpException;

   
}
