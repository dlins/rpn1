/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

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

    public RpSolution recalc() throws RpException {
        return calc();

    }

    public RpSolution calc() throws RpException {

        IntegralCurve result;

        RarefactionOrbit resultForward = (RarefactionOrbit) calc(getStart(), getFamilyIndex(),20);
        RarefactionOrbit resultBackward = (RarefactionOrbit) calc(getStart(), getFamilyIndex(),22);

        if (resultBackward == null || resultForward == null) {
            throw new RpException("Error in native layer");
        }


        Orbit resultComplete = Orbit.concat(resultBackward, resultForward,getFamilyIndex());


        result = new IntegralCurve(resultComplete.getPoints(), resultComplete.getFamilyIndex(),0);



        if (result == null) {
            throw new RpException("Error in native layer");
        }
       

        //** acrescentei isso (Leandro)
        RPnCurve.lista.add(result);
        System.out.println("Tamanho da lista: " + RPnCurve.lista.size());
        //***

        return result;


    }

    private native RpSolution calc(PhasePoint initialpoint, int family,int timeDirection) throws RpException;

   
}
